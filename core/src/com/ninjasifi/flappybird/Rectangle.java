package com.ninjasifi.flappybird;

import com.badlogic.gdx.math.Vector2;

public class Rectangle extends Entity{
    Vector2 reset;
    Rectangle(float speed, Vector2 pos) {
        super(pos);
        this.width = 100;
        this.height = 100;
        texture = gameclass.pipeTexture;
        this.dir = new Vector2(-speed, 0);
        reset = pos;
    }
    public void reset() {
        this.pos = reset;
    }
    public void tick(){
        addDir();
    }
}
