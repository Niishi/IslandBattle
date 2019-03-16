package com.example.islandbattle;

import android.content.Context;
import android.util.Log;

import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

import javax.microedition.khronos.opengles.GL10;

public class Soldier {

    private Sprite2D sprite2D;
    private SpriteText spriteText;
    private float count;
    private City targetCity;
    private City ownerCity;
    private boolean isExpedition = false;
    private boolean isArrived = false;
    private float x, y;
    private int speed = 5;

    Soldier(City city, Sprite2D sprite2D, SpriteText spriteText){
        this.ownerCity = city;
        this.sprite2D = sprite2D;
        this.spriteText = spriteText;
        this.count = 0;
    }

    Soldier(City city, float x, float y, City target, float count){
        this.ownerCity = city;
        this.x = x;
        this.y = y;
        targetCity = target;
        isExpedition = true;
        this.count = count;
        HyperMotion2D.soldierList.add(this);
    }

    void init(GL10 gl, Context context){
        this.sprite2D = new Sprite2D();
        this.sprite2D.setTexture(gl, context.getResources(), R.drawable.soldier);
        this.sprite2D._pos._x = x;
        this.sprite2D._pos._y = y;
        setSize();
        spriteText = new SpriteText();
        spriteText.setTexture(gl, context.getResources(),R.drawable.number);
        spriteText._width = Constant.SPRITE_TEXT_WIDTH;
        spriteText._texWidth = Constant.SPRITE_TEXT_WIDTH;
        spriteText._pos._x = x + sprite2D._width + 10;
        spriteText._pos._y = sprite2D._pos._y;
    }

    void draw(GL10 gl, Context context){
        if(sprite2D == null) init(gl, context);
        sprite2D.draw(gl);
        spriteText.draw(gl, (int)count, 0.7f);
        if(!isExpedition && ownerCity.getState() != Castle.EMPTY_CASTLE) {
            count += ownerCity.addCount;
            setSize();
        }
        if(targetCity != null){
            move();
        }
    }

    private final int MARGIN = 5;

    void move(){
        double theta = Math.atan2(targetCity.sprite._pos._y - this.y, targetCity.sprite._pos._x- this.x);
        this.y += speed * Math.sin(theta);
        this.x += speed * Math.cos(theta);
        this.sprite2D._pos._y += speed * Math.sin(theta);
        this.sprite2D._pos._x += speed * Math.cos(theta);
        this.spriteText._pos._y += speed * Math.sin(theta);
        this.spriteText._pos._x += speed * Math.cos(theta);
        if(hit(targetCity.sprite)){
            attackTargetCity();
        }
        for(Soldier otherSoldier : HyperMotion2D.soldierList){
            if(this.ownerCity.getState() != otherSoldier.ownerCity.getState()){
                if(hit(otherSoldier.sprite2D)){
                    attackSoldier(otherSoldier);
                }
            }
        }
    }
    private void attackTargetCity(){
        this.isArrived = true;
        float targetCount = targetCity.soldier.getCount();
        if(this.ownerCity.getState() != targetCity.getState()){
            if(count > targetCount){
                targetCity.soldier.setCount(count - targetCount);
                targetCity.setState(ownerCity.getState());
                if(this.ownerCity.getState() != City.FRIEND_CASTLE){
                    HyperMotion2D.cpus[this.ownerCity.getState() - 2].cities.add(targetCity);
                }
            }else{
                targetCity.soldier.setCount(targetCount - count);
            }
        }else{
            targetCity.soldier.setCount(targetCount + count);
        }
        this.targetCity = null;
    }

    private void attackSoldier(Soldier otherSoldier){
        float targetCount = otherSoldier.getCount();
        if(count > targetCount){
            otherSoldier.isArrived = true;
            this.setCount(count - targetCount);
        }else{
            this.isArrived = true;
            otherSoldier.setCount(targetCount - count);
        }
    }

    private boolean hit(Sprite2D s){
        if(s == null) return false;
        return Math.abs(this.x - s._pos._x) <= MARGIN &&
                Math.abs(this.y - s._pos._y) <= MARGIN;
    }


    private void setSize(){
        if(count > 500){
            sprite2D._width = 128;
            sprite2D._height = 128;
            speed = 2;
        }else if(count > 100){
            sprite2D._width = 96;
            sprite2D._height = 96;
            speed = 5;
        }else{
            sprite2D._width = 64;
            sprite2D._height = 64;
            speed = 8;
        }
    }

    public float getCount(){
        return count;
    }
    public void setCount(float newCount){
        count = newCount;
    }
    public boolean isKieru(){
        return isExpedition && isArrived;
    }

    public void shift(float diffX, float diffY){
        if(sprite2D == null) return;
        sprite2D._pos._x += diffX;
        sprite2D._pos._y += diffY;
        if(spriteText == null) return;
        spriteText._pos._x += diffX;
        spriteText._pos._y += diffY;
        x += diffX;
        y += diffY;
    }
}
