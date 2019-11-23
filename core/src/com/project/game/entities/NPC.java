package com.project.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;


public class NPC extends Sprite {

	private Vector2 velocity = new Vector2();

	private float speed = 120 * 2, increment;

	private TiledMapTileLayer collisionLayer;

	private PathFinding path;

	private float tileWidth, tileHeight, mapWidth, mapHeight, viewWidth = Gdx.graphics.getWidth(),
			viewHeight = Gdx.graphics.getHeight();

	public NPC(Sprite sprite, TiledMapTileLayer collisionLayer) {
		super(sprite);
		this.collisionLayer = collisionLayer;

		tileWidth = collisionLayer.getTileWidth();
		tileHeight = collisionLayer.getTileHeight();
		mapWidth = collisionLayer.getWidth() * tileWidth;
		mapHeight = collisionLayer.getHeight() * tileHeight;
		path = new PathFinding(collisionLayer);

		Vector2 start = new Vector2(0.0f, 0.0f), end = new Vector2(3.0f, 1.0f);
		path.findPath(start, end);

		System.out.println("done");
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float deltaTime) {
		Vector2 des = new Vector2(16 * 40, mapHeight - getHeight() - (16 * 10));

		// Gdx.app.log(getX() + "", getY() + "");

		if (getX() <= des.x) {
			setX(getX() + speed * deltaTime);
		}

		if (getY() >= des.y) {
			setY(getY() - speed * deltaTime);
		} else {

		}

	}
}
