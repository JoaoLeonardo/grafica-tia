package com.gdx.tia.effects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gdx.tia.element.World;
import com.gdx.tia.screens.GameScreen;

public class PostProcessingResource {

    public void apply(Batch batch) {
        applyShadows(batch);
    }

    private void applyShadows(Batch batch) {
        FrameBuffer fbo = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                (int) GameScreen.ref.getCamera().viewportWidth,
                (int) GameScreen.ref.getCamera().viewportHeight,
                false
        );

        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowShader();

        fbo.begin();
        batch.setShader(shader);
        for (Sprite caster : World.currentStage.getShadowCasters()) {
            caster.draw(batch);
        }
        batch.setShader(null);
        fbo.end();
    }

}
