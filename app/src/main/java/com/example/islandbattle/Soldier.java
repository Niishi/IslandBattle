package com.example.islandbattle;

import android.content.Context;
import android.util.Log;

import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

import javax.microedition.khronos.opengles.GL10;

public class Soldier {

    private Sprite2D sprite2D;
    private SpriteText spriteText;
    private float score;
    private Castle targetCastle;
    private Castle ownerCastle;
    private boolean isExpedition = false;
    private boolean isArrived = false;
    private float x, y;
    private final static int SPEED = 5;

    Soldier(Castle castle, Sprite2D sprite2D, SpriteText spriteText){
        this.ownerCastle = castle;
        this.sprite2D = sprite2D;
        this.spriteText = spriteText;
        this.score = 0;
    }

    Soldier(Castle castle, int x, int y, Castle target, float score){
        this.ownerCastle = castle;
        this.x = x;
        this.y = y;
        targetCastle = target;
        isExpedition = true;
        this.score = score;
    }

    void init(GL10 gl, Context context){
        this.sprite2D = new Sprite2D();
        this.sprite2D.setTexture(gl, context.getResources(), R.drawable.soldier);
        this.sprite2D._pos._x = x;
        this.sprite2D._pos._y = y;
        this.sprite2D._width = 64;
        this.sprite2D._height = 64;
        spriteText = new SpriteText();
        spriteText.setTexture(gl, context.getResources(),R.drawable.number);
        spriteText._width = 49;
        spriteText._texWidth = 49;
        spriteText._pos._x = x + sprite2D._width + 10;
        spriteText._pos._y = sprite2D._pos._y;
    }

    void draw(GL10 gl, Context context){
        if(sprite2D == null) init(gl, context);
        sprite2D.draw(gl);
        spriteText.draw(gl, (int)score, 0.7f);
        if(!isExpedition && ownerCastle.getState() != Castle.EMPTY_CASTLE) {
            score += 0.05f;
        }
        if(targetCastle != null){
            move();
        }
    }

    private final int MARGIN = 5;

    void move(){
        double theta = Math.atan2(targetCastle.sprite._pos._y - this.y, targetCastle.sprite._pos._x- this.x);
        this.y += SPEED * Math.sin(theta);
        this.x += SPEED * Math.cos(theta);
        this.sprite2D._pos._y += SPEED * Math.sin(theta);
        this.sprite2D._pos._x += SPEED * Math.cos(theta);
        this.spriteText._pos._y += SPEED * Math.sin(theta);
        this.spriteText._pos._x += SPEED * Math.cos(theta);
        if(Math.abs(this.x - targetCastle.sprite._pos._x) <= MARGIN &&
                Math.abs(this.y - targetCastle.sprite._pos._y) <= MARGIN){
            this.isArrived = true;
            if(this.ownerCastle.getState() != targetCastle.getState()){
                float targetScore = targetCastle.soldier.getScore();
                if(score > targetScore){
                    targetCastle.soldier.setScore(score - targetScore);
                    targetCastle.setState(ownerCastle.getState());
                }else{
                    targetCastle.soldier.setScore(targetScore - score);
                }
            }
            this.targetCastle = null;
        }
    }

    public float getScore(){
        return score;
    }
    public void setScore(float newScore){
        score = newScore;
    }

    public boolean isKieru(){
        return isExpedition && isArrived;
    }
}
