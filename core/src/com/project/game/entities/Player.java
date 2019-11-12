package com.project.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

	private Vector2 velocity = new Vector2();
	
	private float speed = 60 * 2;
	
	private float gravity = 60 * 1.8f;
	
	public Player(Sprite sprite) {
		super(sprite);
		
	}
	
	@Override
	public void draw(Batch batch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}

	private void update(float deltaTime) {
		setX(getX() + velocity.x * deltaTime);
		setY(getY() + velocity.y * deltaTime);

	}
	
}
