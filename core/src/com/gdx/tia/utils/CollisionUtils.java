package com.gdx.tia.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.gdx.tia.element.AliveEntity;
import com.gdx.tia.enums.Direction;
import com.gdx.tia.enums.HitAcc;

import java.util.Random;


public class CollisionUtils {

    private static final Random random = new Random();

    public static Direction getDiversionDirection(Direction direction) {
        switch (direction) {
            case UP: return random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
            case DOWN: return random.nextBoolean() ? Direction.RIGHT : Direction.LEFT;
            case RIGHT: return random.nextBoolean() ? Direction.DOWN : Direction.UP;
            case LEFT: return random.nextBoolean() ? Direction.UP : Direction.DOWN;
            case UPRIGHT: return random.nextBoolean() ? Direction.LEFT : Direction.DOWN;
            case UPLEFT: return random.nextBoolean() ? Direction.RIGHT : Direction.DOWN;
            case DOWNRIGHT: return random.nextBoolean() ? Direction.LEFT : Direction.UP;
            case DOWNLEFT: return random.nextBoolean() ? Direction.RIGHT : Direction.UP;
        }
        return Direction.HALT;
    }

    public static boolean checkHit(Sprite bullet, AliveEntity entity, HitAcc accuracy) {
        if (!entity.alive) return false;

        boolean hit = false;

        switch (accuracy) {
            case PRECISE:
                hit = precisionHit(bullet.getBoundingRectangle(), entity.sprite.getBoundingRectangle());
                break;
            case LOOSE:
                hit = looseHit(bullet.getBoundingRectangle(), entity.sprite.getBoundingRectangle());
                break;
        }

        if (hit) entity.decreaseHealth();

        return hit;
    }

    private static boolean precisionHit(Rectangle r1, Rectangle r2) {
        return r1.overlaps(r2);
    }

    private static boolean looseHit(Rectangle r1, Rectangle r2) {
        r2.setWidth(r2.width * 1.5f);
        r2.setHeight(r2.height * 1.5f);
        return r1.overlaps(r2);
    }

}
