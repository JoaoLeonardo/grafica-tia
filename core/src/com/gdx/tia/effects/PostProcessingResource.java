package com.gdx.tia.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gdx.tia.element.World;
import com.gdx.tia.screens.GameScreen;

public class PostProcessingResource {

    private SpriteBatch postProcBatch;

    public PostProcessingResource() {
        postProcBatch = new SpriteBatch();
    }

    public void apply(Batch batch) {
        postProcBatch = (SpriteBatch) batch;
        applyShadows();
    }

    private void applyShadows() {
        Texture texture = new Texture("textures/black.png");
        /*FrameBuffer shadowsFB = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                (int) GameScreen.ref.getCamera().viewportWidth,
                (int) GameScreen.ref.getCamera().viewportHeight,
                false
        );*/

        ShaderProgram shader = GameScreen.ref.getShaderResource().getShadowShader();

        //shadowsFB.begin();

        postProcBatch.setShader(shader);
        for (Sprite caster : World.currentStage.getShadowCasters()) {
            postProcBatch.draw(texture,
                    caster.getX() - caster.getWidth() * 1.5f,
                    caster.getY() - caster.getHeight()/2,
                    caster.getWidth() * 4,
                    caster.getHeight() * 2);
        }
        postProcBatch.setShader(null);

        //shadowsFB.end();
    }

    // passar a fonte de luz para o fragment shader
    // passar o pixel central do caster

}
