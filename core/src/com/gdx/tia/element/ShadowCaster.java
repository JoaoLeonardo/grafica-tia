package com.gdx.tia.element;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.gdx.tia.TacticalInfiltrationAction;

public class ShadowCaster {

    private int key;

    private Vector2 correctedPosition;

    private Sprite sprite;

    private Vector2 movementDirection;

    public ShadowCaster(Sprite sprite) {
        this.sprite = sprite;
        this.correctedPosition = new Vector2(this.sprite.getX(), this.sprite.getY());
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
        return correctedPosition;
    }

}
