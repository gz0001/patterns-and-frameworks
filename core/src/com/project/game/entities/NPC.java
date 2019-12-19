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
	
	private Player curPlayer;

	public NPC(Sprite sprite, TiledMapTileLayer collisionLayer, Player player) {
		super(sprite);
		this.collisionLayer = collisionLayer;

		curPlayer = player;
		tileWidth = collisionLayer.getTileWidth();
		tileHeight = collisionLayer.getTileHeight();
		mapWidth = collisionLayer.getWidth() * tileWidth;
		mapHeight = collisionLayer.getHeight() * tileHeight;
		path = new PathFinding(collisionLayer);

		
		path.findPath(relativePos(), relativePlayerPos());
		System.out.println(path);
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}
	
	private Vector2 relativePlayerPos() {
		return new Vector2((float) Math.round(curPlayer.getX() / 16), (float) Math.round(curPlayer.getY() / 16));
	}
	
	
	private Vector2 relativePos() {
		return new Vector2((float) Math.round(getX() / 16), (float) Math.round(getY() / 16));
	}
	

	private void update(float deltaTime) {
		Vector2 relativePos = new Vector2(16 * 40, mapHeight - getHeight() - (16 * 10));
		
		//System.out.println("Player pos: " + relativePlayerPos().toString());

		// Gdx.app.log(getX() + "", getY() + "");

		/*
		if (getX() <= des.x) {
			setX(getX() + speed * deltaTime);
		}

		if (getY() >= des.y) {
			setY(getY() - speed * deltaTime);
		} else {

		}
		*/

	}
}
