package com.ninjasifi.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    public Texture texture;
    public Vector2 pos;
    public final Vector2 reset;
    public Vector2 dir;
    public int width;
    public int height;
    Entity(Vector2 pos){
        this.pos = pos;
        reset = pos;
    }
    public void draw(SpriteBatch batch){
        batch.draw(texture, pos.x, pos.y, width, height);
    }
    public Vector2 center(Vector2 pos){
        pos.x -= width / 2;
        pos.y -= height / 2;
        return pos;
    }
    public void setDir(Vector2 dir){
        this.dir = dir;
    }
    public void addDir(){
        pos.x += dir.x;
        pos.y += dir.y;
    }
}
