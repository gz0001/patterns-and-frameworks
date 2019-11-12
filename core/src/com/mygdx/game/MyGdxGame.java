package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bucketImg;
	Texture dropImg;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	BitmapFont font;
	long lastDropTime;
	int score;
	

	@Override
	public void create() {
		batch = new SpriteBatch();

		dropImg = new Texture("drop.png");
		bucketImg = new Texture("bucket.png");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		raindrops = new Array<Rectangle>();
		spawnRaindrop();
		
		score = 0;
		font = new BitmapFont();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void resize(int width, int height) {
		System.out.println(width);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		font.draw(batch,"Score: " + String.valueOf(score), 10, 460);
		batch.draw(bucketImg, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
			batch.draw(dropImg, raindrop.x, raindrop.y);
		}
		batch.end();
		
		
		 // process user input
	      if(Gdx.input.isTouched()) {
	         Vector3 touchPos = new Vector3();
	         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	         camera.unproject(touchPos);
	         bucket.x = touchPos.x - 64 / 2;
	      }
	      if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 500 * Gdx.graphics.getDeltaTime();
	      if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 500 * Gdx.graphics.getDeltaTime();
	      if(Gdx.input.isKeyPressed(Keys.UP)) bucket.y += 500 * Gdx.graphics.getDeltaTime();
	      if(Gdx.input.isKeyPressed(Keys.DOWN)) bucket.y -= 500 * Gdx.graphics.getDeltaTime();
	      // make sure the bucket stays within the screen bounds
	      if(bucket.x < 0) bucket.x = 0;
	      if(bucket.x > 800 - 64) bucket.x = 800 - 64;
	      
	   // check if we need to create a new raindrop
	      if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
	      
	      // move the raindrops, remove any that are beneath the bottom edge of
	      // the screen or that hit the bucket. In the latter case we play back
	      // a sound effect as well.
	      for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
	         Rectangle raindrop = iter.next();
	         raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
	         if(raindrop.y + 64 < 0) iter.remove();
	         
	         if(raindrop.overlaps(bucket)) {
	            iter.remove();
	            score++;
	         }
	      }

	}
	
	@Override
	   public void dispose() {
	      // dispose of all the native resources
	      dropImg.dispose();
	      bucketImg.dispose();
	      batch.dispose();
	   }
}
