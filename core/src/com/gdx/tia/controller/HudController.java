package com.gdx.tia.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.element.Agent;
import com.gdx.tia.screens.GameScreen;

public class HudController implements ActionController {

    static HudController ref;

    private final int UI_ELEMENTS_PADDING = 10;

    private Sprite heartSprite;
    private BitmapFont font;
    private BitmapFont fontShadow;

    @Override
    public void create() {
        ref = this;
        font = new BitmapFont(Gdx.files.internal("ui/tia.fnt"));
        fontShadow = new BitmapFont(Gdx.files.internal("ui/tia-black.fnt"));
        heartSprite = TacticalInfiltrationAction.assetManager.get("ui/ui-sheet.txt", TextureAtlas.class).createSprite("heart");
    }

    @Override
    public void drawElements(Batch batch) {
        if (AgentController.ref.getAgent().alive) {
            this.drawHealthbar(batch);
            this.drawScore(batch);
        }
    }

    public void drawHealthbar(Batch batch) {
        final float onCameraMinX =  GameScreen.ref.getCamera().position.x - GameScreen.ref.getScreenCenter().x;
        final float onCameraMaxY =  GameScreen.ref.getCamera().position.y + GameScreen.ref.getScreenCenter().y;
        final float healthbarX = onCameraMinX + UI_ELEMENTS_PADDING;
        final float healthbarY = onCameraMaxY - heartSprite.getHeight() - UI_ELEMENTS_PADDING;

        for (int i = 0; i < AgentController.ref.getAgent().healthbar; i++) {
            heartSprite.setPosition(healthbarX + heartSprite.getWidth() * i, healthbarY);
            heartSprite.draw(batch);
        }
    }

    public void drawScore(Batch batch) {
        final float onCameraMaxX = GameScreen.ref.getCamera().position.x + GameScreen.ref.getScreenCenter().x;
        final float onCameraMaxY = GameScreen.ref.getCamera().position.y + GameScreen.ref.getScreenCenter().y;

        Agent agent = AgentController.ref.getAgent();
        String score = (agent.canTradeScore ? "(Q) " : "") + agent.getScore() + "";

        // razão para (tentar) manter o mesmo alinhamento horizontal independente do tamanho da string ;)
        int scoreCorrection = (score.length() + (score.length() % 2)) / 2;

        float scoreX = onCameraMaxX - (font.getScaleX() * score.length() * (UI_ELEMENTS_PADDING + scoreCorrection)) - UI_ELEMENTS_PADDING;
        float scoreY = onCameraMaxY - UI_ELEMENTS_PADDING;

        fontShadow.draw(batch, score, scoreX+2, scoreY);
        fontShadow.draw(batch, score, scoreX-2, scoreY);
        fontShadow.draw(batch, score, scoreX, scoreY-2);
        fontShadow.draw(batch, score, scoreX, scoreY+2);
        font.draw(batch, score, scoreX, scoreY);
    }

    public void dispose() { font.dispose(); }

}
