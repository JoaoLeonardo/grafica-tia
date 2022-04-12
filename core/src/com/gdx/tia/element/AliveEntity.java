package com.gdx.tia.element;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class AliveEntity {

    public Sprite sprite;

    public int healthbar;
    public boolean alive;

    public boolean hasBeenHit;

    public Sprite getSprite() { return sprite; }

    public void decreaseHealth() {
        if (alive) healthbar--;
        alive = healthbar > 0;
        hasBeenHit = true;
    }

    public void revive() { alive = true; }

}
