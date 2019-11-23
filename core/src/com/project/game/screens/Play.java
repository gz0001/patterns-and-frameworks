package com.project.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project.game.entities.NPC;
import com.project.game.entities.Player;

public class Play implements Screen {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private float mapWidth, mapHeight, viewWidth = Gdx.graphics.getWidth(), viewHeight = Gdx.graphics.getHeight();

	private Player player;
	private NPC npc;

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("maps/new-map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, viewWidth, viewHeight);
		camera.update();

		player = new Player(new Sprite(new Texture("images/wizard.png")),
				(TiledMapTileLayer) map.getLayers().get(0));

		npc = new NPC(new Sprite(new Texture("images/wizard.png")), (TiledMapTileLayer) map.getLayers().get(0));

		mapWidth = player.getCollisionLayer().getWidth() * player.getCollisionLayer().getTileWidth();
		mapHeight = player.getCollisionLayer().getHeight() * player.getCollisionLayer().getTileHeight();

		player.setPosition(0, mapHeight - player.getHeight());
		npc.setPosition(16 * 5, mapHeight - npc.getHeight());
		camera.position.set(mapHeight / 2, mapWidth / 2, 0);
		camera.update();

		Gdx.input.setInputProcessor(player);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float plPosX = player.getX() + player.getWidth() / 2, plPosY = player.getY() + player.getHeight() / 2;

		// Camera move right or left:
		if (plPosX >= viewWidth / 2 && plPosX <= mapWidth - (viewWidth / 2)) {
			camera.position.x = plPosX;
		}

		// Camera move up or down:
		if (plPosY <= mapHeight - (viewHeight / 2) && plPosY >= viewHeight / 2) {
			camera.position.y = plPosY;
		}

		camera.update();
		renderer.setView(camera);
		renderer.render();

		renderer.getBatch().begin();
		player.draw(renderer.getBatch());
		npc.draw(renderer.getBatch());
		renderer.getBatch().end();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		player.getTexture().dispose();

	}

}
