package com.gdx.tia.controller;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.element.Enemy;
import com.gdx.tia.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class EnemyController implements ActionController {

    public static EnemyController ref;

    public TextureAtlas enemyAtlas;
    public Sound gunshotSound;

    private final int MAX_WAVE_LENGTH = 8;

    private final ArrayList<Enemy> activeEnemies = new ArrayList<>();
    private Pool<Enemy> enemyPool;

    private int currentWaveLength;

    @Override
    public void create() {
        ref = this;

        enemyAtlas = new TextureAtlas("sprites/enemy.txt");

        enemyPool = new Pool<Enemy>() {
            @Override
            protected Enemy newObject() {
                return new Enemy();
            }
        };
        currentWaveLength = 0;

        gunshotSound = TacticalInfiltrationAction.assetManager.get("effects/enemy-gunshot.ogg", Sound.class);
    }

    @Override
    public void drawElements(Batch batch) {
        if (isCleared()) spawnNewWave();
        else renderWave(batch);
    }

    private void renderWave(Batch batch) {
        for (int i = 0; i < activeEnemies.size(); i++) {
            Enemy activeEnemy = activeEnemies.get(i);

            if (activeEnemy.alive) {
                // renderiza o inimigo
                activeEnemy.update();

                if (!activeEnemy.hasBeenHit) activeEnemy.getSprite().draw(batch);
                else activeEnemy.hasBeenHit = false;
            } else if (activeEnemy.particleTimer != 0) {
                activeEnemy.playParticleEffect(batch);
            } else {
                // libera o inimigo da pool/lista de ativos
                enemyPool.free(this.activeEnemies.remove(i));
            }
        }
    }

    public void spawnNewWave() {
        Array<RectangleMapObject> spawnList = GameScreen.ref.getEnemySpawn().getByType(RectangleMapObject.class);

        // incrementa o número da wave (se necessário)
        if (currentWaveLength < MAX_WAVE_LENGTH) this.currentWaveLength++;

        // adiciona a wave na lista de inimigos ativos
        while (activeEnemies.size() < currentWaveLength) {
            Enemy freshEnemy = enemyPool.obtain();
            RectangleMapObject spawn = spawnList.random();
            spawnList.removeValue(spawn, true);

            freshEnemy.init(spawn.getRectangle().x, spawn.getRectangle().y);
            activeEnemies.add(freshEnemy);
        }
    }

    public List<Enemy> getActiveEnemies() { return activeEnemies; }

    public boolean isCleared() { return activeEnemies.size() <= 0; }

    public float getAgentX() { return AgentController.ref.getAgentSprite().getX(); }
    public float getAgentY() { return AgentController.ref.getAgentSprite().getY(); }

}
