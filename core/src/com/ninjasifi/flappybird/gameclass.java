package com.ninjasifi.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class gameclass extends ApplicationAdapter {
	static Texture birdTexture;
	static Texture pipeTexture;
	public static int screenX = 600;
	public static int screenY = 480;
	static int threads = 1;
	static int generations = 10;
	static int inputNodes = 3;
	static int outputNodes = 3;
	// In order to add layers, just add a number in the middle and add a comma
	static int[] layers = new int[] {inputNodes, 5, 5 , outputNodes};
	ArrayList<Bird> birds;

	SpriteBatch batch;
	Bird bird;
	Rectangle pipe;

	@Override
	public void create () {
		// Rendering
		batch = new SpriteBatch();
		birdTexture = new Texture("flappybird.png");
		pipeTexture = new Texture("pipe.png");

		// The pipe
		pipe = new Rectangle(0, new Vector2(300, 0));

		// Birds
		NeuralNetwork net = new NeuralNetwork(layers);
		bird = new Bird(new Vector2(100, 100), net);
		for (int i = 0; i < generations; i++) {
			long startTime = System.currentTimeMillis();
			try {
				bird = train(10000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			long elapsedTime = System.currentTimeMillis() - startTime;
			System.out.println("ms generation took: "+ " (" + elapsedTime + "ms)");
			bird.reset();
			bird.net.setFitness(0);
		}
		System.out.println(bird.pos);

		bird.setDir(new Vector2(0, -5));
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.3f, 0.3f, 0.6f, 0f);

		// rectangle.pos = mouse.pos
		pipe.pos = pipe.center(new Vector2(Gdx.input.getX(), screenY - Gdx.input.getY()));

		// gravity of bird
		bird.tick();
		float[] out = bird.ai(pipe);
		if(out[0] > 0)
			bird.pos.y += 150;
		System.out.println(bird.pos.y);
		if(bird.pos.y > screenY) {
			bird.pos.y = screenY - bird.height;
		} else if (bird.pos.y < 0) {
			bird.pos.y = 0;
		}
		batch.begin();
		pipe.draw(batch);
		if(!bird.checkCollision(pipe))
			bird.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		birdTexture.dispose();
	}
	private Bird train(int population) throws InterruptedException {
		int bestBird = 0;
		// Multi threading
		ArrayList<Train> threads = new ArrayList<>(gameclass.threads);
		for (int i = 0; i < gameclass.threads; i++) {
			threads.add(new Train(bird, (i * population) / gameclass.threads, (i + 1) * population / gameclass.threads));
			threads.get(i).start();
		}
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < gameclass.threads; i++) {
			threads.get(i).join();
		}
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("join: "+ " (" + elapsedTime + "ms)");
		for (int i = 0; i < gameclass.threads; i++) {
			float currentFitness = threads.get(i).getBestBird().net.getFitness();
			float bestFitness = threads.get(bestBird).getBestBird().net.getFitness();
			if(currentFitness > bestFitness){
				//System.out.println(trains.get(i).getBestNet().getFitness());
				bestBird = i;
			}
		}
		//System.out.println(threads.get(bestBird).getBestBird().net.getFitness());
		return threads.get(bestBird).getBestBird();
	}
}
