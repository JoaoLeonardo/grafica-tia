package com.gdx.tia.element;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.enums.Direction;
import com.gdx.tia.screens.GameScreen;

public class ShadowCaster {

    private int key;

    private Vector2 correctedPosition;

    private Sprite sprite;

    private Vector2 movementDirection;

    private boolean correctCamera;

    public ShadowCaster(Sprite sprite) {
        create(sprite);
    }

    public ShadowCaster(Sprite sprite, boolean correctCamera) {
        this.correctCamera = correctCamera;
        create(sprite);
    }

    private void create(Sprite sprite) {
        this.sprite = sprite;
        this.correctedPosition = new Vector2(this.sprite.getX(), this.sprite.getY());
        this.movementDirection = Direction.HALT.displacementVector;
        this.register();
    }

    private void register() {
        key = TacticalInfiltrationAction.ref.getShadowsResource().registerAsCaster(this);
    }

    public void update(Sprite sprite, Vector2 movementDirection) {
        this.sprite = sprite;
        this.movementDirection = movementDirection;
        TacticalInfiltrationAction.ref.getShadowsResource().updateCaster(key, this);
    }

    public void remove() {
        TacticalInfiltrationAction.ref.getShadowsResource().removeCaster(key);
    }

    public Sprite getSprite() { return sprite; }

    public Vector2 getPosition() {
        correctedPosition.x = this.sprite.getX() + this.movementDirection.x * 4;
        correctedPosition.y = this.sprite.getY() - this.movementDirection.y * 4;

        if (correctCamera) {
            correctedPosition.y = correctedPosition.y + (GameScreen.ref.getCamera().position.y - this.sprite.getY()) * 2;
        }

        return correctedPosition;
    }

}
