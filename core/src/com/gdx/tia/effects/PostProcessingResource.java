package com.gdx.tia.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gdx.tia.element.World;
import com.gdx.tia.screens.GameScreen;

public class PostProcessingResource {

    private SpriteBatch postBatch;

    private OrthographicCamera postCamera;

    private FrameBuffer occlusionFBO;
    private FrameBuffer shadowMapFBO;
    private FrameBuffer shadowDrawFBO;

    public PostProcessingResource() {
        postBatch = new SpriteBatch();
        //postCamera = new OrthographicCamera();
        shadowMapFBO = getNewFBO();
        shadowDrawFBO = getNewFBO();
        occlusionFBO = getNewFBO();
    }

    public void apply(Batch mainBatch) {
        // ajusta a projeção da batch
        postBatch.setProjectionMatrix(GameScreen.ref.getCamera().combined);

        // aplica o efeito de sombras para todos os casters do World
        Texture shadows = this.applyShadows();
        mainBatch.draw(shadows, -1, 1, 2, -2);

        // limpa os assets utilizados
        dispose();
    }

    private Texture applyShadows() {
        Texture occlusionMap = mapOcclusion();
        Texture shadowMap = mapShadows(occlusionMap);
        return shadowMap;
        //return drawShadows(shadowMap);
    }

    private Texture mapOcclusion() {
        occlusionFBO.begin();

        postBatch.begin();
        for (Sprite caster : World.currentStage.getShadowCasters()) caster.draw(postBatch);
        postBatch.end();

        occlusionFBO.end();
        return occlusionFBO.getColorBufferTexture();
    }

    private Texture mapShadows(Texture occlusionMap) {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowMapShader();
        if (!shader.isCompiled()) System.out.println(shader.getLog());

        shadowMapFBO.begin();

        Gdx.gl.glClearColor(255f,255f,255f,1.0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        float mx = GameScreen.ref.getCamera().position.x - Gdx.graphics.getWidth() / 2f;
        float my = GameScreen.ref.getCamera().position.y - Gdx.graphics.getHeight() / 2f;

        postBatch.begin();
        postBatch.setShader(shader);
        shader.setUniformf("u_resolution", occlusionMap.getWidth(), occlusionMap.getHeight());
        postBatch.draw(occlusionMap, mx, my, occlusionMap.getWidth(), occlusionMap.getHeight());
        postBatch.setShader(null);
        postBatch.end();

        shadowMapFBO.end();
        return shadowMapFBO.getColorBufferTexture();
    }

    private Texture drawShadows(Texture shadowMap) {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowDrawShader();
        if (!shader.isCompiled()) System.out.println(shader.getLog());

        Vector3 lightLocation = translateCoord(GameScreen.ref.getLightLocation());

        shadowDrawFBO.begin();

        float mx = GameScreen.ref.getCamera().position.x - Gdx.graphics.getWidth() / 2f;
        float my = GameScreen.ref.getCamera().position.y - Gdx.graphics.getHeight() / 2f;

        postBatch.begin();
        postBatch.setShader(shader);
        shader.setUniformf("u_resolution", 1024, 720);
        postBatch.draw(shadowMap, mx, my, shadowMap.getWidth(), shadowMap.getHeight());
        postBatch.setShader(null);
        postBatch.end();

        shadowDrawFBO.end();
        return shadowDrawFBO.getColorBufferTexture();
    }

    private FrameBuffer getNewFBO() {
        return new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 720, false);
    }

    private Vector3 translateCoord(Vector2 position) {
        return GameScreen.ref.getCamera().project(new Vector3(position.x, position.y, 0));
    }

    private void dispose() {
        shadowMapFBO.dispose();
        shadowDrawFBO.dispose();
    }

}
