package com.gdx.tia.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.controller.AgentController;
import com.gdx.tia.enums.Direction;
import com.gdx.tia.screens.GameScreen;

public class Bullet implements Pool.Poolable {

    private final int MOVEMENT_SPEED = 400;

    private final Vector2 position;

    private final Sprite bulletSprite;

    private Vector2 movementDirection;

    public boolean active;
    public boolean boundByPlayer;

    public ShaderProgram shader;

    public Bullet() {
        position = new Vector2();
        movementDirection = Direction.RIGHT.displacementVector;
        bulletSprite = new Sprite(TacticalInfiltrationAction.assetManager.get("sprites/bullet.png", Texture.class));
        bulletSprite.scale(1.2f);
        active = false;
        boundByPlayer = false;
        shader = new ShaderProgram(Gdx.files.internal("shaders/glow.vsh"), Gdx.files.internal("shaders/glow.fsh"));
        ShaderProgram.pedantic = false;
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
        this.shader.setUniformMatrix("uni_projTrans", GameScreen.ref.getCamera().projection);
        if (!active) return;

        final float mvSpeed = MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
        position.add(movementDirection.x * mvSpeed, movementDirection.y * mvSpeed);
        bulletSprite.setPosition(position.x, position.y);

        active = !GameScreen.ref.hasCollidedWithMap(bulletSprite.getBoundingRectangle(), false);

        if (active) {
            batch.setShader(shader);
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

    public Sprite getBulletSprite() { return bulletSprite; }
}
