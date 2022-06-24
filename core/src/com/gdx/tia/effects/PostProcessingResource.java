package com.gdx.tia.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gdx.tia.interfaces.EffectResource;
import com.gdx.tia.screens.GameScreen;

public class PostProcessingResource implements EffectResource {

    private ShadowsResource shadowsResource;

    private SpriteBatch postBatch;

    private Vector2 resolution;
    private Vector2 center;

    public PostProcessingResource() {
        shadowsResource = new ShadowsResource();
        postBatch = new SpriteBatch();
        resolution = new Vector2();
        center = new Vector2();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void resize(int width, int height) {
        resolution.x = width != 0 ? width : 1024;
        resolution.y = height != 0 ? height : 720;
        shadowsResource.resize(width, height);
    }

    public void apply(Batch mainBatch) {
        // ajusta a projeção da batch
        postBatch.setProjectionMatrix(GameScreen.ref.getCamera().combined);

        // recalcula o centro da tela
        calculateCenter();

        // aplica o efeito de sombras para todos os casters do World
        Texture shadows = this.shadowsResource.renderShadows(postBatch, resolution, center);
        mainBatch.draw(shadows, -1, 1, 2, -2);

        mainBatch.flush();
    }

    private void calculateCenter() {
        Vector3 cameraPosition = GameScreen.ref.getCamera().position;
        center.x = (cameraPosition.x != 0 ? cameraPosition.x : resolution.x) - resolution.x / 2f;
        center.y = (cameraPosition.x != 0 ? cameraPosition.y : resolution.y) - resolution.y / 2f;
    }

    @Override
    public void dispose() {
        postBatch.dispose();
    }

    public ShadowsResource getShadowsResource() { return shadowsResource; }

}
