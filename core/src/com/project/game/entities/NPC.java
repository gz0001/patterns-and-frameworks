package com.project.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class NPC extends Sprite {

	private Vector2 velocity = new Vector2();

	private float speed = 60, increment, range = 0.4f;

	private TiledMapTileLayer collisionLayer;

	private PathFinding pathHelper;
	
	private Vector2 lastPlayerPos;

	private ArrayList<Vector2> paths;

	private int curIndex = 1;

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
		pathHelper = new PathFinding(collisionLayer);

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

	private boolean inPoint(Vector2 des) {
		return Math.abs(getX() - des.x * 16) <= range && Math.abs(getY() - des.y * 16) <= range;
		
	}

	private void update(float deltaTime) {
		Vector2 curPlayerPos = relativePlayerPos();
		
		if(getBoundingRectangle().overlaps(curPlayer.getBoundingRectangle())) {
			System.out.println("Player caught");
		}
		
		if (paths == null) {
			lastPlayerPos = curPlayerPos;
			paths = pathHelper.findPath(relativePos(), lastPlayerPos);
		

		} else {
			
			// Detect player moved
			if(lastPlayerPos.dst(curPlayerPos) != 0) {
				lastPlayerPos = curPlayerPos;
				paths = pathHelper.findPath(relativePos(), lastPlayerPos);
				curIndex = 1;
			}
			
			Vector2 move = paths.get(curIndex);

			if (getX() < (move.x * 16) + range) {
				setX(getX() + speed * deltaTime);
			} else if (getX() > (move.x * 16) - range) {
				setX(getX() - speed * deltaTime);
			} else {
				setX(move.x * 16);
			}

			if (getY() > (move.y * 16) + range) {
				setY(getY() - speed * deltaTime);
			} else if (getY() < (move.y * 16) - range) {
				setY(getY() + speed * deltaTime);
			} else {
				setY(move.y * 16);
			}

			if (inPoint(move) && curIndex < paths.size() - 1 && paths.size() > 1) {
				curIndex += 1;
				System.out.println(paths.get(curIndex));
			}
			
			

		}

		// System.out.println("Player pos: " + relativePlayerPos().toString());

		// Gdx.app.log(getX() + "", getY() + "");

		/*
		 * if (getX() <= des.x) { setX(getX() + speed * deltaTime); }
		 * 
		 * if (getY() >= des.y) { setY(getY() - speed * deltaTime); } else {
		 * 
		 * }
		 */

	}
}
