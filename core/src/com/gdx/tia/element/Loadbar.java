package com.gdx.tia.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Loadbar {

    private ShapeRenderer shapeRenderer;

    private float progress;

    public Loadbar(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void update(float progress) {
        this.progress = progress;
        this.draw();
    }

    public void draw() {
        ScreenUtils.clear(1, 1, 1, 1);

        float width = Gdx.graphics.getWidth() / 2f;
        float height = Gdx.graphics.getHeight() / 20f;
        float startX = Gdx.graphics.getWidth() / 2.0f - width / 2;
        float startY = Gdx.graphics.getHeight() / 2.0f - height / 2;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(startX, startY, width, height);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(startX + 5, startY + 5, (width - 10) * progress, height - 10);
        shapeRenderer.end();
    }

    public void dispose() {
        this.shapeRenderer.dispose();
    }

}
