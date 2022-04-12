package com.gdx.tia.element;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.gdx.tia.enums.AgentSpeech;
import com.gdx.tia.screens.MainMenuScreen;
import com.gdx.tia.utils.SpeechUtils;

public class Agent extends AliveEntity {

    private TextureAtlas agentAtlas;

    private int score;
    public boolean canTradeScore;

    private SpeechUtils speechUtils;

    public Agent() {
        agentAtlas = new TextureAtlas("sprites/agent.txt");
        speechUtils = new SpeechUtils();
        start();
    }

    public void setSprite(String region, Vector2 position) {
        sprite = agentAtlas.createSprite(region);
        sprite.setPosition(position.x, position.y);
        sprite.getBoundingRectangle().setPosition(position);
    }

    public void start() {
        healthbar = 3;
        score = 0;
        alive = true;
        canTradeScore = false;
        speechUtils.reset();
    }

    public void decreaseHealth() {
        if (!hasBeenHit) super.decreaseHealth();
        if (alive) canTradeScore = score >= 200;
        else MainMenuScreen.ref.setScore(getScore());
    }

    public void increaseScoreByKill() {
        score += 5;
        canTradeScore = score >= 200 && healthbar < 3;
    }

    public void tradeScore() {
        if (canTradeScore) {
            score -= 100;
            healthbar++;
            canTradeScore = false;
        }
    }

    public void speak(Batch batch, Vector2 agentPosition) {
        if (speechUtils.currentLine == null)
            speechUtils.currentLine = AgentSpeech.getSpeech(
                    score, healthbar, speechUtils.hasSpoken, speechUtils.random.nextBoolean()
            );
        speechUtils.speak(batch, agentPosition);
    }

    public int getScore() { return score; }

    public void dispose() { agentAtlas.dispose(); }
}
