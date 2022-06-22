package com.gdx.tia.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.gdx.tia.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class ShaderResource {

    private ShaderProgram glowShader;
    private ShaderProgram shadowMapShader;
    private ShaderProgram shadowDrawShader;

    List<ShaderProgram> postProcessShaders;

    public ShaderResource() {
        ShaderProgram.pedantic = false;
        this.createShaders();
        this.setProjection();
    }

    private void createShaders() {
        this.glowShader = new ShaderProgram(
                Gdx.files.local("shaders/glow.vsh"),
                Gdx.files.local("shaders/glow.fsh")
        );
        this.shadowMapShader = new ShaderProgram(
                Gdx.files.local("shaders/shadow.vsh"),
                Gdx.files.local("shaders/shadow-map.fsh")
        );
        this.shadowDrawShader = new ShaderProgram(
                Gdx.files.local("shaders/shadow.vsh"),
                Gdx.files.local("shaders/shadow-draw.fsh")
        );

        this.postProcessShaders = new ArrayList<>();
        this.postProcessShaders.add(this.shadowMapShader);
        this.postProcessShaders.add(this.shadowDrawShader);
    }

    private void setProjection() {
        Matrix4 projection = GameScreen.ref.getCamera().projection;
        this.glowShader.setUniformMatrix("u_projTrans", projection);
        this.shadowMapShader.setUniformMatrix("u_projTrans", projection);
        this.shadowDrawShader.setUniformMatrix("u_projTrans", projection);
    }

    public ShaderProgram getGlowShader() {
        return glowShader;
    }
    public ShaderProgram getShadowMapShader() { return shadowMapShader; }
    public ShaderProgram getShadowDrawShader() { return shadowDrawShader; }

    public List<ShaderProgram> getPostProcessShaders() { return postProcessShaders; }

}
