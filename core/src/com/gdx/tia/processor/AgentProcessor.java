package com.gdx.tia.processor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.controller.AgentController;
import com.gdx.tia.controller.BulletController;
import com.gdx.tia.enums.Direction;
import com.gdx.tia.screens.GameScreen;
import com.gdx.tia.screens.MainMenuScreen;

public class AgentProcessor implements InputProcessor {

    private final int MOVEMENT_SPEED = 300;

    private final AgentController agentController;

    private final Vector2 movementDirection;
    public final Vector2 position;

    private Direction mouseDirection;

    public AgentProcessor(AgentController agentController) {
        Rectangle spawn = GameScreen.ref.getPlayerSpawn().getRectangle();

        this.position = new Vector2(spawn.x, spawn.y);
        this.movementDirection = new Vector2(Direction.HALT.displacementVector);
        this.agentController = agentController;
    }

    public void update() {
        float speed = MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
        float futureX = movementDirection.x * speed;
        float futureY = movementDirection.y * speed;
        Rectangle rectangle = agentController.getAgentSprite().getBoundingRectangle();

        // É ZERO QUANDO MUDA A REGIÃO
        rectangle.x += futureX;
        rectangle.y += futureY;

        if (!GameScreen.ref.hasCollidedWithMap(rectangle, true)) {
            position.x += futureX;
            position.y += futureY;
            agentController.getAgentSprite().setPosition(position.x, position.y);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W: movementDirection.y = 1; break; // UP
            case Input.Keys.A: movementDirection.x = -1; break; // LEFT
            case Input.Keys.S: movementDirection.y = -1; break; // DOWN
            case Input.Keys.D: movementDirection.x = 1; break; // RIGHT
            case Input.Keys.Q: AgentController.ref.getAgent().tradeScore(); break; // Ação especial
            case Input.Keys.ESCAPE: AgentController.ref.getAgent().kill(); break; // Menu
            default:
                this.position.add(Direction.HALT.displacementVector);
                break; // NONE
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W: if (movementDirection.y == 1) movementDirection.y = 0; break;
            case Input.Keys.A: if (movementDirection.x == -1) movementDirection.x = 0; break;
            case Input.Keys.S: if (movementDirection.y == -1) movementDirection.y = 0; break;
            case Input.Keys.D: if (movementDirection.x == 1) movementDirection.x = 0; break;
            default: break;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Input.Buttons.LEFT == button)
            BulletController.ref.addActiveBullet(position, mouseDirection, getGunshotFx(), true);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        final float xCenter = GameScreen.ref.getScreenCenter().x;
        final float yCenter = GameScreen.ref.getScreenCenter().y;

        Vector2 diffVector = new Vector2(
                screenX > (xCenter + getMouseInterval().x) ? 1 : (screenX < (xCenter - getMouseInterval().x) ? -1 : 0),
                screenY < (yCenter - getMouseInterval().y) ? 1 : (screenY > (yCenter + getMouseInterval().y) ? -1 : 0)
        );

        mouseDirection = Direction.getDirectionByDisplacement(diffVector);
        agentController.setAgentSprite(mouseDirection.name());
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private Sound getGunshotFx() {
        return TacticalInfiltrationAction.assetManager.get("effects/gunshot.ogg");
    }

    private Vector2 getMouseInterval() { return GameScreen.ref.getMouseInterval(); }

    public  Vector2 getMovementDirection() { return movementDirection; }

}
