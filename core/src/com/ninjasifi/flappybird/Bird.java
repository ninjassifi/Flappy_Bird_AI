package com.ninjasifi.flappybird;
import com.badlogic.gdx.math.Vector2;

public class Bird extends Entity {
    //float[] input = new float[gameclass.inputNodes];
    //float[] out = new float[gameclass.outputNodes];
    public boolean dead;
    NeuralNetwork net;
    Bird(Vector2 pos, NeuralNetwork net) {
        super(pos);
        dir = new Vector2(0, 0);
        texture = gameclass.birdTexture;
        width = 32;
        height = 24;
        this.net = net;
    }
    public void tick(){
        addDir();
        //dead = checkCollision();
    }
    public void reset(){
        pos = reset;
        dead = false;
        //net.setFitness(0);
    }
    public boolean checkCollision(Rectangle pipe){
        // If it is more than that x and less than that x + width
        if(this.pos.x > pipe.pos.x && this.pos.x < pipe.pos.x + pipe.width)
            // AND it's less than other y plus height and more than other pos y
            if(this.pos.y + height < pipe.pos.y + height && this.pos.y > pipe.pos.y)
                return true;
        if(this.pos.x + width > pipe.pos.x && this.pos.x + width < pipe.pos.x + pipe.width)
            if(this.pos.y + height < pipe.pos.y + pipe.height && this.pos.y > pipe.pos.y)
                return true;

        return false;
    }
    public float[] ai(Rectangle pipe){
        // 1st is bird y - pipe opening y
        // 2nd is bird x - pipe x
        float[] input = {pos.y - (pipe.pos.y + pipe.height), pos.x - pipe.pos.x};
        return net.feedForward(input);
    }
}
