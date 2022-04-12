package com.gdx.tia.enums;

import com.badlogic.gdx.math.Vector2;

public enum Direction {

    UP(new Vector2(0, 1)),
    UPLEFT(new Vector2(-1, 1)),
    LEFT(new Vector2(-1, 0)),
    DOWNLEFT(new Vector2(-1, -1)),
    DOWN(new Vector2(0, -1)),
    DOWNRIGHT(new Vector2(1, -1)),
    RIGHT(new Vector2(1, 0)),
    UPRIGHT(new Vector2(1, 1)),
    HALT(new Vector2(0, 0));

    public Vector2 displacementVector;

    Direction(Vector2 displacement) {
        displacementVector = displacement;
    }

    public static Direction getDirectionByDisplacement(Vector2 displacement) {
        for (Direction direction : values()) {
            if (direction.displacementVector.equals(displacement)) return direction;
        }
        return Direction.HALT;
    }

}
