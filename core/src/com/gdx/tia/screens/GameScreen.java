package com.gdx.tia.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.element.Stage;
import com.gdx.tia.element.World;
import com.gdx.tia.enums.Direction;
import com.gdx.tia.shaders.ShaderResource;

public class GameScreen implements Screen {

    public static GameScreen ref;

    private final int DIR_LEN2 = Direction.values().length * 2;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private MapObjects objects;
    private MapObjects hazardGround;
    private MapObjects enemySpawn;
    private RectangleMapObject playerSpawn;

    private World gameWorld;

    private Vector2 screenCenter;
    private Vector2 mDirInterval;

    private Sound theme;

    private ShaderResource shaderResource;

    public GameScreen() {
        ref = this;
        loadAssets();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        map = new TmxMapLoader().load("tiles/outdoors.tmx");
        shaderResource = new ShaderResource();

        objects = map.getLayers().get(2).getObjects();
        hazardGround = map.getLayers().get(3).getObjects();
        playerSpawn = map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class).get(0);
        enemySpawn = map.getLayers().get(5).getObjects();

        renderer = new OrthogonalTiledMapRenderer(map);
        screenCenter = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        mDirInterval = new Vector2();

        gameWorld = new Stage(renderer.getBatch(), this);
        gameWorld.create();

        theme = TacticalInfiltrationAction.assetManager.get("effects/stage-music.ogg");
        playTheme();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // renderiza o mapa
        camera.update();
        renderer.setView(camera);
        renderer.render();

        // renderiza os elementos do mundo
        renderer.getBatch().begin();

        if (TacticalInfiltrationAction.assetManager.update()) gameWorld.render();
        renderer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        screenCenter = new Vector2((float) width / 2, (float) height / 2);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        updateInterval();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { theme.pause(); }

    @Override
    public void dispose() {
        map.dispose();
        theme.dispose();
        renderer.dispose();
        gameWorld.dispose();
    }

    public OrthographicCamera getCamera() { return camera; }
    public Vector2 getScreenCenter() { return screenCenter; }

    public boolean hasCollidedWithMap(Rectangle boundingRectangle, boolean checkGround) {
        if (checkObjectCollision(boundingRectangle, objects)) return true;

        if (checkGround) {
            return checkObjectCollision(boundingRectangle, hazardGround);
        }

        return false;
    }

    private boolean checkObjectCollision(Rectangle boundingRectangle, MapObjects objects) {
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, boundingRectangle)) {
                return true;
            }
        }
        return false;
    }


    public void updateInterval() {
        // intervalo do mouse entre uma direção e outra = L / Nd * 2
        // onde L = Largura do eixo e Nd = número de direções
        mDirInterval.x = (float) Gdx.graphics.getWidth() / DIR_LEN2;
        mDirInterval.y = (float) Gdx.graphics.getHeight() / DIR_LEN2;
    }

    public void playTheme() {
        long id = theme.play(0.1f);
        theme.setLooping(id, true);
    }

    private void loadAssets() {
        TacticalInfiltrationAction.assetManager.load("effects/gunshot.ogg", Sound.class);
        TacticalInfiltrationAction.assetManager.load("effects/enemy-gunshot.ogg", Sound.class);
        TacticalInfiltrationAction.assetManager.load("effects/stage-music.ogg", Sound.class);
        TacticalInfiltrationAction.assetManager.load("sprites/bullet.png", Texture.class);
        TacticalInfiltrationAction.assetManager.load("sprites/text-bubble.png", Texture.class);
    }

    public RectangleMapObject getPlayerSpawn() { return playerSpawn; }

    public MapObjects getEnemySpawn() { return enemySpawn; }

    public Vector2 getMouseInterval() { return mDirInterval; }

    public ShaderResource getShaderResource() { return this.shaderResource; }
}
