package com.gdx.tia.element;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.gdx.tia.controller.*;
import com.gdx.tia.enums.HitAcc;
import com.gdx.tia.screens.GameScreen;
import com.gdx.tia.utils.CollisionUtils;

public class Stage extends World {

    AgentController agentController;
    BulletController bulletController;
    EnemyController enemyController;
    HudController hudController;

    public Stage(Batch batch, GameScreen gameScreen) {
        super(batch, gameScreen);
    }

    @Override
    public Vector2 getPlayerPosition() {
        return new Vector2(agentController.getAgentSprite().getX(), agentController.getAgentSprite().getY());
    }

    @Override
    public void create() {
        super.create();

        agentController = new AgentController();
        bulletController = new BulletController();
        enemyController = new EnemyController();
        hudController = new HudController();

        actionControllerList.add(bulletController);
        actionControllerList.add(agentController);
        actionControllerList.add(enemyController);
        actionControllerList.add(hudController);

        super.notifyCreation();
    }

    @Override
    public void render() {
        if (agentController.getAgent().alive) super.render();
        else super.complete();
    }

    @Override
    public boolean hasCollidedWithAliveEntity(Bullet bullet) {
        if (bullet.boundByPlayer) {
            for (Enemy enemy : enemyController.getActiveEnemies()) {
                if (CollisionUtils.checkHit(bullet.getBulletSprite(), enemy, HitAcc.LOOSE))
                    return true;
            }
        } else {
            Agent player = agentController.getAgent();
            return CollisionUtils.checkHit(bullet.getBulletSprite(), player, HitAcc.PRECISE);
        }

        return false;
    }

    @Override
    public void dispose() {
        agentController.dispose();
        hudController.dispose();
    }

}
