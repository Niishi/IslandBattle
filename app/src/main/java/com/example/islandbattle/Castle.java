package com.example.islandbattle;

import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

import javax.microedition.khronos.opengles.GL10;

public class Castle {
    Sprite2D sprite;
    Soldier soldier;
    private int x,y;

    private int state = EMPTY_CASTLE;
    private static final int EMPTY_CASTLE   = 0;
    private static final int FRIEND_CASTLE  = 1;
    private static final int ENEMY_CASTLE   = 2;

    Castle(Sprite2D sprite, Sprite2D soldierSprite,SpriteText spriteText, int x, int y){
        this.sprite = sprite;
        soldier = new Soldier(soldierSprite, spriteText);
        this.x = x;
        this.y = y;
    }

    void draw(GL10 gl){
        sprite.draw(gl);
        soldier.draw(gl);
    }

    void select(){
        state = FRIEND_CASTLE;
        sprite._texX = 400;
    }
}
