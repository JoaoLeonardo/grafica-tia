package com.gdx.tia.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.element.Loadbar;

public class LoadingScreen implements Screen {

    private Screen nextScreen;

    private Loadbar loadbar;

    public LoadingScreen(Screen nextScreen) {
        this.nextScreen = nextScreen;
    }

    @Override
    public void show() {
        loadbar = new Loadbar(new ShapeRenderer());
    }

    @Override
    public void render(float delta) {
        if (!TacticalInfiltrationAction.assetManager.update())
            loadbar.update(TacticalInfiltrationAction.assetManager.getProgress());
        else {
            TacticalInfiltrationAction.ref.changeScreen(nextScreen);
        }
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { dispose(); }

    @Override
    public void dispose() {
        loadbar.dispose();
    }

}
