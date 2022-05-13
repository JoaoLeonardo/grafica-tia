package com.gdx.tia.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.controller.AgentController;
import com.gdx.tia.enums.Direction;
import com.gdx.tia.screens.GameScreen;

public class Bullet implements Pool.Poolable {

    private final int MOVEMENT_SPEED = 0;

    private final Vector2 position;

    private final Sprite bulletSprite;

    private Vector2 movementDirection;

    public boolean active;
    public boolean boundByPlayer;

    public Bullet() {
        position = new Vector2();
        movementDirection = Direction.RIGHT.displacementVector;
        bulletSprite = new Sprite(TacticalInfiltrationAction.assetManager.get("sprites/bullet.png", Texture.class));
        bulletSprite.scale(1.2f);
        active = false;
        boundByPlayer = false;
    }

    public void init(float initialX, float initialY, Direction direction, boolean shotByPlayer) {
        if (direction == null) direction = Direction.RIGHT;

        active = true;
        boundByPlayer = shotByPlayer;

        int offsetX = getOffsetX(direction);
        int offsetY = getOffsetY(direction);
        position.set(initialX + offsetX, initialY + offsetY);

        if (Direction.HALT.equals(direction)) direction = Direction.RIGHT;
        movementDirection = direction.displacementVector;
    }

    public void update(Batch batch) {
        if (!active) return;

        final float mvSpeed = MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
        position.add(movementDirection.x * mvSpeed, movementDirection.y * mvSpeed);
        bulletSprite.setPosition(position.x, position.y);

        active = !GameScreen.ref.hasCollidedWithMap(bulletSprite.getBoundingRectangle(), false);

        if (active) {
            batch.setShader(getShader());
            getShader().setUniformf("u_glowColor", 255.0f, 0.0f, 0.0f, 1.0f);
            Vector3 screenCoord = getScreenCoord();
            getShader().setUniformf("u_glowSource", screenCoord.x + 2, screenCoord.y + 2);

            if (!getShader().isCompiled()) {
                throw new GdxRuntimeException("Couldn't compile shader: " + getShader().getLog());
            }

            bulletSprite.draw(batch);
            batch.setShader(null);

            if (World.currentStage.hasCollidedWithAliveEntity(this)) {
                active = false;
                if (boundByPlayer) AgentController.ref.getAgent().increaseScoreByKill();
            }
        }
    }

    @Override
    public void reset() {
        position.set(0, 0);
        active = false;
    }

    private int getOffsetX(Direction direction) {
        switch (direction) {
            case UPLEFT:
            case UP: return 7;
            case RIGHT:
            case UPRIGHT: return 12;
            case DOWN: return 3;
            case DOWNLEFT: return 10;
            case DOWNRIGHT: return 14;
        }
        return 0;
    }

    private int getOffsetY(Direction direction) { return Direction.DOWNRIGHT.equals(direction) ? 12 : 16; }

    private Vector3 getScreenCoord() {
        return GameScreen.ref.getCamera().project(new Vector3(position.x, position.y, 0));
    }

    public Sprite getBulletSprite() { return bulletSprite; }

    public ShaderProgram getShader() { return GameScreen.ref.getShaderResource().getGlowShader(); }
}
