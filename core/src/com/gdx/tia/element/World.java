package com.gdx.tia.element;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.controller.ActionController;
import com.gdx.tia.effects.PostProcessingResource;
import com.gdx.tia.screens.GameScreen;
import com.gdx.tia.screens.LoadingScreen;
import com.gdx.tia.screens.MainMenuScreen;

import java.util.ArrayList;
import java.util.List;

public abstract class World implements ApplicationListener {

    public static World currentStage;

    private final PostProcessingResource postProcessingResource;

    private Batch batch;

    private GameScreen gameScreen;

    private List<Sprite> shadowCasters;

    public List<ActionController> actionControllerList;

    public World(Batch batch, GameScreen gameScreen) {
        this.batch = batch;
        this.gameScreen = gameScreen;
        this.postProcessingResource = new PostProcessingResource();
    }

    @Override
    public void create() {
        currentStage = this;
        actionControllerList = new ArrayList<>();
    }

    public void notifyCreation() {
        for (ActionController actionController : actionControllerList) actionController.create();
    }

    @Override
    public void render() {
        this.shadowCasters = new ArrayList<>();
        for (ActionController actionController : actionControllerList) actionController.drawElements(batch);
        gameScreen.getCamera().position.set(getPlayerPosition(), 0);
        this.postProcessingResource.apply(batch);
    }

    abstract boolean hasCollidedWithAliveEntity(Bullet bullet);

    @Override
    public void resize(int width, int height) {
        this.postProcessingResource.resize();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    public void complete(World nextWorld) {
        dispose();
        nextWorld.create();
    }

    public void complete() {
        TacticalInfiltrationAction.ref.setScreen(new LoadingScreen(MainMenuScreen.ref));
        dispose();
    }

    public abstract Vector2 getPlayerPosition();

    public List<Sprite> getShadowCasters() { return shadowCasters; }

    public void registerAsShadowCaster(Sprite caster) { shadowCasters.add(caster); }

    @Override
    public void dispose() {
        this.postProcessingResource.dispose();
    }
}
