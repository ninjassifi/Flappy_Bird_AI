package com.ninjasifi.flappybird;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Train extends Thread {
    int bestBird;
    int min;
    int max;
    float[] input;
    float[] out;
    // The copy of the first net
    Bird bird;

    // List of all the nets
    ArrayList<Bird> birds;


    public Train(Bird bird, int min, int max) {
        this.bestBird = 0;
        this.min = min;
        this.max = max;
        this.bird = bird;
        bird.net.setFitness(0);
        birds = new ArrayList<>(max - min);
        for (int i = 0; i < max; i++) {
            birds.add(i, new Bird(new Vector2(100, 100), bird.net));
        }
    }

    // Put all the code here for training
    public void run() {
        /*
    Create bird
    Create pipe

    loop(for every bird){

    loop(all ticks){
    Move pipe
    Move bird
    Check collision
    Check if pipe needs to reset

    if bird dead or too long:
    exit loop
    otherwise:
    give bird reward

    }

    if was best bird:
    best bird = this bird
    }
         */
        input = new float[gameclass.layers[0]];

        for (int i = 0; i < max; i++) {

            bird = birds.get(i);

            // Mutate
            bird.net.mutate();

            // Actual training

            // Create pipe
            Rectangle pipe = new Rectangle(1, new Vector2(gameclass.screenX, 0));

            //TODO: check if training works
            // then make the training random (once the bird clears it, reset the pos)

            bird.dir = new Vector2(0, -5);

            // Train for 100 ticks
            for (int tick = 0; tick < 1000; tick++) {
                pipe.tick();
                bird.tick();

                if(pipe.pos.x < 0){
                    pipe.reset();
                    pipe.pos.y = (float) (Math.random() * (gameclass.screenY - pipe.height -100));
                }
                float[] out = bird.ai(pipe);
                if(out[0] > 0){
                    bird.pos.y += 150;
                } else if (bird.pos.y > gameclass.screenY || bird.pos.y < 0) {
                    bird.dead = true;
                } else {
                    bird.dead = bird.checkCollision(pipe);
                }

                if (bird.dead){
                    break;
                }

                bird.net.addFitness(1);
            }
            if(this.bird.net.compareTo(birds.get(bestBird).net)){
                bestBird = i;
            }
            birds.set(i, bird);
        }
    }

    public Bird getBestBird() {
        //TODO: fix bug that makes bird.reset.y = to more than screenY
        Bird bird = birds.get(bestBird);
        bird.reset();
        bird.pos.y = 100;
        return bird;
    }
}