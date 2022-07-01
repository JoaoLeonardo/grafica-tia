package com.gdx.tia.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.gdx.tia.element.ShadowCaster;
import com.gdx.tia.interfaces.EffectResource;
import com.gdx.tia.screens.GameScreen;

import java.util.HashMap;

public class ShadowsResource implements EffectResource {

    private static int SHADOW_LOOKUP_NB = 42;

    private HashMap<Integer, ShadowCaster> shadowCasters;

    private FrameBuffer occlusionFBO;
    private FrameBuffer shadowMapFBO;
    private FrameBuffer shadowDrawFBO;

    public ShadowsResource() {
        shadowCasters = new HashMap<>();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public Texture renderShadows(SpriteBatch batch, Vector2 resolution, Vector2 center) {
        TextureRegion occlusionMap = mapOcclusion(batch);
        occlusionMap.flip(false, true);

        Texture shadowMap = mapShadows(occlusionMap, batch, resolution, center);
        shadowMap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shadowMap.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        Texture shadowsFBOTex = drawShadows(shadowMap, batch, resolution, center);
        shadowDrawFBO.dispose();
        return shadowsFBOTex;
    }

    private TextureRegion mapOcclusion(SpriteBatch batch) {
        occlusionFBO.begin();
        batch.begin();

        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0f,0f,0f,0f);

        for (ShadowCaster caster : shadowCasters.values())
            batch.draw(caster.getSprite(), caster.getPosition().x, caster.getPosition().y + SHADOW_LOOKUP_NB);

        batch.end();
        occlusionFBO.end();
        return new TextureRegion(occlusionFBO.getColorBufferTexture());
    }

    private Texture mapShadows(TextureRegion occlusionMap, SpriteBatch batch, Vector2 resolution, Vector2 center) {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowMapShader();
        if (!shader.isCompiled()) System.out.println(shader.getLog());

        shadowMapFBO.begin();
        batch.begin();

        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0f,0f,0f,0f);

        batch.setShader(shader);
        shader.setUniformf("u_resolution", resolution.x, resolution.y);
        shader.setUniformf("u_lookup_range", SHADOW_LOOKUP_NB);
        batch.draw(occlusionMap, center.x, center.y, occlusionMap.getRegionWidth(), occlusionMap.getRegionHeight());
        batch.setShader(null);

        batch.end();
        shadowMapFBO.end();

        return shadowMapFBO.getColorBufferTexture();
    }

    private Texture drawShadows(Texture shadowMap, SpriteBatch batch, Vector2 resolution, Vector2 center) {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowDrawShader();
        if (!shader.isCompiled()) System.out.println(shader.getLog());

        shadowDrawFBO.begin();
        batch.begin();

        batch.setShader(shader);
        shader.setUniformf("u_resolution", resolution.x, resolution.y);
        batch.draw(shadowMap,  center.x, center.y, resolution.x, resolution.y);
        batch.setShader(null);

        batch.end();
        shadowDrawFBO.end();
        return shadowDrawFBO.getColorBufferTexture();
    }

    private FrameBuffer getNewFBO(Vector2 resolution) {
        return new FrameBuffer(Pixmap.Format.RGBA8888, (int) resolution.x, (int) resolution.y, false);
    }

    public int registerAsCaster(ShadowCaster caster) {
        int newKey = shadowCasters.size();
        while (shadowCasters.containsKey(newKey)) newKey++;
        shadowCasters.put(newKey, caster);
        return newKey;
    }

    public void updateCaster(int key, ShadowCaster caster) { shadowCasters.put(key, caster); }

    public void removeCaster(int key) { shadowCasters.remove(key); }

    public void clearCasters() { shadowCasters.clear(); }

    @Override
    public void resize(int width, int height) {
        Vector2 res = new Vector2(width, height);
        shadowMapFBO = getNewFBO(res);
        shadowDrawFBO = getNewFBO(res);
        occlusionFBO = getNewFBO(res);
    }

    @Override
    public void dispose() {
        occlusionFBO.dispose();
        shadowMapFBO.dispose();
        shadowDrawFBO.dispose();
    }

}
