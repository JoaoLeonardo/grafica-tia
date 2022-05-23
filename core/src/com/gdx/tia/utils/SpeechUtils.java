package com.gdx.tia.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.gdx.tia.TacticalInfiltrationAction;

import java.util.Random;

public class SpeechUtils {

    private static int BUBBLE_PADDING = 16;
    private static int BUBBLE_HALF_PADDING = 8;

    private NinePatch textBubblePatch;
    private GlyphLayout glyphLayout;
    private BitmapFont font;

    private float speechTimer;
    private float speechCooldown;

    public String currentLine;

    public boolean hasSpoken;

    public Random random;

    public SpeechUtils() {
        Texture txtBubbleTexture = TacticalInfiltrationAction.assetManager.get(
                "sprites/text-bubble.png", Texture.class);

        textBubblePatch = new NinePatch(txtBubbleTexture, 12, 12, 7, 7);
        glyphLayout = new GlyphLayout();

        font = new BitmapFont(Gdx.files.internal("ui/tia-black.fnt"));
        font.getData().setScale(0.7f, 0.7f);

        random = new Random();
    }

    public void speak(Batch batch, Vector2 speakerPos) {
        speechCooldown += Gdx.graphics.getDeltaTime();

        if (currentLine == null) return;
        if (hasSpoken && speechCooldown < 10) return;

        if (speechTimer < 3) {
            speechTimer += Gdx.graphics.getDeltaTime();

            final float posX = speakerPos.x + 24;
            final float posY = speakerPos.y + 24;

            glyphLayout.setText(font, currentLine);
            textBubblePatch.draw(batch, posX, posY,
                    glyphLayout.width + BUBBLE_PADDING, 32 + BUBBLE_PADDING);
            font.draw(batch, glyphLayout, posX + BUBBLE_HALF_PADDING, posY + 32 + BUBBLE_HALF_PADDING);
        } else {
            currentLine = null;
            hasSpoken = true;
            speechTimer = 0;
            speechCooldown = 0;
        }
    }

    public void reset() {
        speechTimer = 0;
        speechCooldown = 0;
        hasSpoken = false;
    }

}
