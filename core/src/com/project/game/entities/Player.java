package com.project.game.entities;

import java.awt.print.Printable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {

	private Vector2 velocity = new Vector2();

	private float speed = 100 * 2, increment;

	private TiledMapTileLayer collisionLayer;

	public Player(Sprite sprite, TiledMapTileLayer collisionLayer) {
		super(sprite);
		this.collisionLayer = collisionLayer;

	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float deltaTime) {

		float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(),
				titleHeight = collisionLayer.getTileHeight(), mapWidth = collisionLayer.getWidth() * tileWidth,
				mapHeight = collisionLayer.getHeight() * titleHeight;
		boolean collisionX = false, collisionY = false;

		setX(getX() + velocity.x * deltaTime);

		// calculate the increment for step in #collidesLeft() and #collidesRight()
		increment = collisionLayer.getTileWidth();
		increment = getWidth() < increment ? getWidth() / 2 : increment / 2;

		if (velocity.x < 0) // going left
			collisionX = collidesLeft();
		else if (velocity.x > 0) // going right
			collisionX = collidesRight();
		
		// react to x collision or out of map
		if (collisionX || getX() < 0 || getX() > mapWidth - getWidth()) {
			setX(oldX);
			velocity.x = 0;
		}

		setY(getY() + velocity.y * deltaTime);

		// calculate the increment for step in #collidesBottom() and #collidesTop()
		increment = collisionLayer.getTileHeight();
		increment = getHeight() < increment ? getHeight() / 2 : increment / 2;

		if (velocity.y < 0) // going down
			collisionY = collidesBottom();
		else if (velocity.y > 0) // going up
			collisionY = collidesTop();
		
		// react to y collision
		if (collisionY || getY() < 0 || getY() > mapHeight - getHeight()) {
			setY(oldY);
			velocity.y = 0;
		}

	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()),
				(int) (y / collisionLayer.getTileHeight()));
		// System.out.println("Cell: " + cell.getTile().getId());
		return cell != null && cell.getTile() != null && cell.getTile().getId() == 248;
	}

	public boolean collidesRight() {
		for (float step = 0; step <= getHeight(); step += increment)
			if (isCellBlocked(getX() + getWidth(), getY() + step))
				return true;
		return false;
	}

	public boolean collidesLeft() {
		for (float step = 0; step <= getHeight(); step += increment)
			if (isCellBlocked(getX(), getY() + step))
				return true;
		return false;
	}

	public boolean collidesTop() {
		for (float step = 0; step <= getWidth(); step += increment)
			if (isCellBlocked(getX() + step, getY() + getHeight()))
				return true;
		return false;

	}

	public boolean collidesBottom() {
		for (float step = 0; step <= getWidth(); step += increment)
			if (isCellBlocked(getX() + step, getY()))
				return true;
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.W:
			velocity.y += speed;
			break;
		case Keys.S:
			velocity.y -= speed;
			break;
		case Keys.A:
			velocity.x -= speed;
			break;
		case Keys.D:
			velocity.x += speed;

		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.A:
		case Keys.D:
			velocity.x = 0;
		case Keys.W:
		case Keys.S:
			velocity.y = 0;

		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return super.getX();
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return super.getY();
	}

}
