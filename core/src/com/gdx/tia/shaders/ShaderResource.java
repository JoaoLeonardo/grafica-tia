package com.gdx.tia.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.gdx.tia.screens.GameScreen;

public class ShaderResource {

    private ShaderProgram glowShader;

    public ShaderResource() {
        ShaderProgram.pedantic = false;
        this.createShaders();
        this.setProjection();
    }

    private void createShaders() {
        this.glowShader = new ShaderProgram(
                Gdx.files.internal("shaders/glow.vsh"),
                Gdx.files.internal("shaders/glow.fsh")
        );
    }

    private void setProjection() {
        Matrix4 projection = GameScreen.ref.getCamera().projection;
        this.glowShader.setUniformMatrix("u_projTrans", projection);
    }

    public ShaderProgram getGlowShader() {
        return glowShader;
    }

}
