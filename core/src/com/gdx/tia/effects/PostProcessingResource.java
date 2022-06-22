package com.gdx.tia.effects;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Pixmap;
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

    public PostProcessingResource() {
        postBatch = new SpriteBatch();
        shadowMapFBO = getNewFBO();
    }

    public void apply(Batch mainBatch) {
        postBatch.setProjectionMatrix(GameScreen.ref.getCamera().combined);

        // aplica o efeito de sombras para todos os casters do World
        FrameBuffer shadowsFBO = this.mapShadows();
        mainBatch.draw(shadowsFBO.getColorBufferTexture(), -1, 1, 2, -2);
        shadowsFBO.dispose();
    }

    private FrameBuffer mapShadows() {
        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowShader();
        shader.setUniformMatrix("u_projTrans", GameScreen.ref.getCamera().combined);

        shadowMapFBO.begin();
        postBatch.begin();

        postBatch.setShader(shader);
        for (Sprite caster : World.currentStage.getShadowCasters()) caster.draw(postBatch);
        postBatch.setShader(null);

        postBatch.end();
        shadowMapFBO.end();

        return shadowMapFBO;
    }

    private FrameBuffer getNewFBO() {
        return new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 720, false);
    }

}
