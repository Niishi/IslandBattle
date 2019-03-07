package com.example.islandbattle;

import android.content.Context;

import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Castle {
    Sprite2D sprite;
    Soldier soldier;
    ArrayList<Soldier> soldiers;
    private int x,y;


    private int state = EMPTY_CASTLE;
    public static final int EMPTY_CASTLE   = 0;
    public static final int FRIEND_CASTLE  = 1;
    public static final int ENEMY_CASTLE   = 2;

    public boolean isSelected = false;

    Castle(Sprite2D sprite, Sprite2D soldierSprite,SpriteText spriteText, int x, int y){
        this.sprite = sprite;
        soldier = new Soldier(this, soldierSprite, spriteText);
        soldiers = new ArrayList<>();
        this.x = x;
        this.y = y;
    }

    void draw(GL10 gl, Context context){
        sprite.draw(gl);
        soldier.draw(gl, context);
        for(int i = soldiers.size() - 1; i >= 0; i--){
            Soldier soldier = soldiers.get(i);
            soldier.draw(gl, context);
            if(soldier.isKieru()) soldiers.remove(soldier);
        }
    }

    void select(){
        isSelected = !isSelected;
    }

    void atack(Castle castle){
        float score = soldier.getScore();
        soldier.setScore(score/2);
        soldiers.add(new Soldier(this, this.x, this.y, castle, score/2));
    }

    void setState(int state){
        this.state = state;
        sprite._texX = 400 * state;
    }

    int getState(){
        return  state;
    }
}
