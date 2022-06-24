package com.gdx.tia.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gdx.tia.element.World;
import com.gdx.tia.screens.GameScreen;

public class PostProcessingResource {

    private SpriteBatch postBatch;

    private OrthographicCamera postCamera;

    private Vector2 resolution;
    private Vector2 center;

    private FrameBuffer occlusionFBO;
    private FrameBuffer shadowMapFBO;
    private FrameBuffer shadowDrawFBO;

    public PostProcessingResource() {
        postBatch = new SpriteBatch();
        postCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        resolution = new Vector2();
        center = new Vector2();
        resize();
    }

    public void apply(Batch mainBatch) {
        // ajusta a projeção da batch
        postBatch.setProjectionMatrix(GameScreen.ref.getCamera().combined);

        // recalcula o centro da tela
        calculateCenter();

        // aplica o efeito de sombras para todos os casters do World
        Texture shadows = this.applyShadows();
        mainBatch.draw(shadows, -1, 1, 2, -2);

        // limpa os assets utilizados nesse loop
        renderDispose();
    }

    private void calculateCenter() {
        Vector3 cameraPosition = GameScreen.ref.getCamera().position;
        center.x = (cameraPosition.x != 0 ? cameraPosition.x: resolution.x) - resolution.x / 2f;
        center.y = (cameraPosition.x != 0 ? cameraPosition.y: resolution.y) - resolution.y / 2f;
    }

    private Texture applyShadows() {
        TextureRegion occlusionMap = mapOcclusion();

        Texture shadowMapTex = mapShadows(occlusionMap);

        return drawShadows(shadowMapTex);
    }

    private TextureRegion mapOcclusion() {
        occlusionFBO.begin();

        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        postCamera.setToOrtho(false, occlusionFBO.getWidth(), occlusionFBO.getHeight());

        postCamera.translate(center.x - 256/2f, center.y - 256/2f);

        postCamera.update();

        postBatch.setProjectionMatrix(postCamera.combined);

        postBatch.setShader(null);
        postBatch.begin();

        for (Sprite caster : World.currentStage.getShadowCasters()) caster.draw(postBatch);

        postBatch.end();

        occlusionFBO.end();
        return new TextureRegion(occlusionFBO.getColorBufferTexture());
    }

    private Texture mapShadows(TextureRegion occlusionMap) {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowMapShader();
        if (!shader.isCompiled()) System.out.println(shader.getLog());

        shadowMapFBO.begin();

        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        postBatch.setShader(shader);

        postBatch.begin();

        shader.setUniformf("u_resolution", 256, 256);

        postCamera.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
        postBatch.setProjectionMatrix(postCamera.combined);

        postBatch.draw(occlusionMap.getTexture(), 0, 0, 256, shadowMapFBO.getHeight());

        postBatch.end();

        shadowMapFBO.end();

        return shadowMapFBO.getColorBufferTexture();
    }

    private Texture drawShadows(Texture shadowMap) {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowDrawShader();
        if (!shader.isCompiled()) System.out.println(shader.getLog());

        postCamera.setToOrtho(false);
        postBatch.setProjectionMatrix(postCamera.combined);

        postBatch.setShader(shader);
        postBatch.begin();
        shader.setUniformf("u_resolution", 256, 256);
        postBatch.setColor(Color.RED);
        postBatch.draw(shadowMap, center.x-256/2f, center.y-256/2f, 256, 256);
        postBatch.end();

        postBatch.setColor(Color.WHITE);
        return shadowDrawFBO.getColorBufferTexture();
    }

    public void resize() {
        resolution.x = Gdx.graphics.getWidth() != 0 ? Gdx.graphics.getWidth() : 1024;
        resolution.y = Gdx.graphics.getHeight() != 0 ? Gdx.graphics.getHeight() : 720;
        shadowMapFBO = getNewFBO();
        shadowDrawFBO = getNewFBO();
        occlusionFBO = getNewFBO();
    }

    private FrameBuffer getNewFBO() {
        return new FrameBuffer(Pixmap.Format.RGBA8888, (int) resolution.x, (int) resolution.y, false);
    }

    private void renderDispose() {
        //occlusionFBO.dispose();
        //shadowMapFBO.dispose();
        shadowDrawFBO.dispose();
    }

    public void dispose() {
        postBatch.dispose();
        occlusionFBO.dispose();
        shadowMapFBO.dispose();
        shadowDrawFBO.dispose();
    }

}
