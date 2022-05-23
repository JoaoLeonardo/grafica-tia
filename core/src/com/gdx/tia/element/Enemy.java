package com.gdx.tia.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.gdx.tia.controller.BulletController;
import com.gdx.tia.controller.EnemyController;
import com.gdx.tia.enums.Direction;
import com.gdx.tia.screens.GameScreen;
import com.gdx.tia.utils.CollisionUtils;

import java.util.Random;

public class Enemy extends AliveEntity implements Pool.Poolable {

    private final static int MOVEMENT_SPEED = 100;

    // espaço entre uma direção e outra (de 30 à -30)
    private final static int X_SENSITIVITY = 30;
    private final static int Y_SENSITIVITY = 30;

    private Vector2 position;

    private Direction dodgeDirection;
    private boolean isDiverting;
    private float dodgeTimer;

    private float gunTimerMax;
    private float gunTimer;

    private Random random;

    private ParticleEffect particleEffect;
    public float particleTimer;

    public Enemy() {
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.local("effects/enemy-hit.p"), Gdx.files.local("effects"));
        particleEffect.start();
        random = new Random();

        reset();
    }

    public void init(float initialX, float initialY) {
        sprite = EnemyController.ref.enemyAtlas.createSprite(Direction.HALT.name());
        position = new Vector2(initialX + 50, initialY);
        gunTimerMax = random.nextFloat() + 0.5f;
        gunTimer = 0;
        particleTimer = 0;
        revive();
    }

    public void update() {
        // descobre a direção do sprite em relação ao agente
        Direction direction = getSpriteDirectionRelativeToTarget();

        // seta o sprite e a movimentação de acordo com a direção descoberta
        sprite = EnemyController.ref.enemyAtlas.createSprite(direction.name());

        if (!isDiverting) {
            // segue o agente
            moveToTarget(direction);

            // atira
            gunTimer += Gdx.graphics.getDeltaTime();
            if (gunTimer > gunTimerMax) {
                gunTimer = 0;
                BulletController.ref.addActiveBullet(
                        position, direction, EnemyController.ref.gunshotSound, false
                );
            }
        } else {
            // desvia do objeto
            dodgeObject();
        }
    }

    public void moveToTarget(Direction direction) {
        Rectangle enemyRectangle = moveRectangle(direction.displacementVector);

        // verifica a colisão com objetos do mapa
        if (GameScreen.ref.hasCollidedWithMap(enemyRectangle, true)) {
            this.isDiverting = true;
            this.dodgeTimer = 0;
            // NÃO É A DIREÇÃO INVERSA
            dodgeDirection = CollisionUtils.getDiversionDirection(direction);
            dodgeObject();
        } else {
            position.set(enemyRectangle.x, enemyRectangle.y);
            sprite.setPosition(position.x, position.y);
        }
    }

    public void dodgeObject() {
        Rectangle enemyRectangle = moveRectangle(dodgeDirection.displacementVector);

        dodgeTimer += Gdx.graphics.getDeltaTime();
        this.isDiverting = GameScreen.ref.hasCollidedWithMap(enemyRectangle, true) || dodgeTimer < 1;

        position.set(enemyRectangle.x, enemyRectangle.y);
        sprite.setPosition(enemyRectangle.x, enemyRectangle.y);
    }

    public Rectangle moveRectangle(Vector2 displacement) {
        // movimenta o retângulo de acordo com o delta e velocidade
        float mSpeed = MOVEMENT_SPEED * Gdx.graphics.getDeltaTime();
        Rectangle enemyRectangle = sprite.getBoundingRectangle();
        float xDisp = displacement.x * mSpeed;
        float yDisp = displacement.y * mSpeed;
        enemyRectangle.setPosition(position.x + xDisp, position.y + yDisp);
        return enemyRectangle;
    }

    public Direction getSpriteDirectionRelativeToTarget() {
        final float xDiff = position.x - EnemyController.ref.getAgentX();
        final float yDiff = EnemyController.ref.getAgentY() - position.y;

        Vector2 diffVector = new Vector2(
                xDiff > X_SENSITIVITY ? -1 : (xDiff < -X_SENSITIVITY ? 1 : 0),
                yDiff > Y_SENSITIVITY ? 1 : (yDiff < -Y_SENSITIVITY ? -1 : 0)
        );
        return Direction.getDirectionByDisplacement(diffVector);
    }

    public void startParticleEffect() {
        particleEffect.reset();
        particleTimer = 0.1f;
    }

    public void playParticleEffect(Batch batch) {
        if (particleTimer > 0) {
            particleTimer += Gdx.graphics.getDeltaTime();
            particleEffect.getEmitters().first().setPosition(sprite.getX(), sprite.getY());
            particleEffect.draw(batch, Gdx.graphics.getDeltaTime());

            if (particleTimer > 1) particleTimer = 0;
        }
    }

    @Override
    public void decreaseHealth() {
        super.decreaseHealth();
        if (!alive) startParticleEffect();
    }

    @Override
    public void reset() {
        healthbar = 2;
        alive = false;
    }

    public void dispose() {
        particleEffect.dispose();
    }

}
