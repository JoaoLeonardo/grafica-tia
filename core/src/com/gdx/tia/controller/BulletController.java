package com.gdx.tia.controller;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.gdx.tia.element.Bullet;
import com.gdx.tia.enums.Direction;

import java.util.ArrayList;

public class BulletController implements ActionController {

    public static BulletController ref;

    private ArrayList<Bullet> activeBullets;
    private final Pool<Bullet> bulletPool = new Pool<Bullet>() {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };

    @Override
    public void create() {
        ref = this;
        activeBullets = new ArrayList<>();
    }

    @Override
    public void drawElements(Batch batch) {
        for (Bullet activeBullet : activeBullets) activeBullet.update(batch);
    }

    public void addActiveBullet(Vector2 origin, Direction direction, Sound sound, boolean shotByPlayer) {
        // adiciona uma bala na lista de balas ativas
        Bullet freshBullet = bulletPool.obtain();
        freshBullet.init(origin.x, origin.y, direction, shotByPlayer);
        activeBullets.add(freshBullet);

        // toca o efeito de tiro
        playVfx(sound);

        // remove as balas inativas da pool
        removeInactiveBullets();
    }

    public void removeInactiveBullets() {
        Bullet item;

        // limpa o array de balas ativas (removendo as balas inativas)
        for (int i = activeBullets.size(); --i >= 0; ) {
            item = activeBullets.get(i);

            if (!item.active) {
                activeBullets.remove(i);
                bulletPool.free(item);
            }
        }
    }

    public void playVfx(Sound sound) {
        if (sound != null) {
            long id = sound.play(0.5f);
            sound.setLooping(id, false);
        }
    }

}
