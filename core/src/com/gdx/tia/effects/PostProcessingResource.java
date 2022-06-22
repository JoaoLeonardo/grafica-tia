package com.gdx.tia.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gdx.tia.element.World;
import com.gdx.tia.screens.GameScreen;

public class PostProcessingResource {

    private SpriteBatch postBatch;

    private FrameBuffer shadowMapFBO;
    private FrameBuffer shadowDrawFBO;

    public PostProcessingResource() {
        postBatch = new SpriteBatch();
        shadowMapFBO = getNewFBO();
        shadowDrawFBO = getNewFBO();
    }

    public void apply(Batch mainBatch) {
        // ajusta a projeção da batch
        postBatch.setProjectionMatrix(GameScreen.ref.getCamera().combined);
        postBatch.begin();

        // aplica o efeito de sombras para todos os casters do World
        Texture shadows = this.applyShadows();
        mainBatch.draw(shadows, -1, 1, 2, -2);

        // limpa os assets utilizados
        postBatch.end();
        dispose();
    }

    private Texture applyShadows() {
        Texture shadowMap = mapShadows();
        return drawShadows(shadowMap);
    }

    private Texture mapShadows() {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowMapShader();

        shadowMapFBO.begin();

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        postBatch.setShader(shader);
        for (Sprite caster : World.currentStage.getShadowCasters()) caster.draw(postBatch);
        postBatch.setShader(null);

        shadowMapFBO.end();
        return shadowMapFBO.getColorBufferTexture();
    }

    private Texture drawShadows(Texture shadowMap) {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowDrawShader();
        shader.setUniformf("u_lightSource", GameScreen.ref.getScreenCenter().x, GameScreen.ref.getScreenCenter().y);

        float mx = GameScreen.ref.getCamera().position.x - Gdx.graphics.getWidth() / 2f;
        float my = GameScreen.ref.getCamera().position.y - Gdx.graphics.getHeight() / 2f;

        shadowDrawFBO.begin();

        postBatch.setShader(shader);
        postBatch.draw(shadowMap, mx, my, shadowMap.getWidth(), shadowMap.getHeight());
        postBatch.setShader(null);

        shadowDrawFBO.end();
        return shadowDrawFBO.getColorBufferTexture();
    }

    private FrameBuffer getNewFBO() {
        return new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 720, false);
    }

    private void dispose() {
        shadowMapFBO.dispose();
        shadowDrawFBO.dispose();
    }

}
