package com.example.islandbattle;

import android.content.Context;

import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public abstract class City {
    Sprite2D sprite;
    Soldier soldier;
    ArrayList<Soldier> soldiers;

    private int state = EMPTY_CASTLE;
    public static final int EMPTY_CASTLE = 0;
    public static final int FRIEND_CASTLE = 1;
    public static final int ENEMY_CASTLE = 2;

    public boolean isSelected = false;

    float addCount = 0.05f;
    float addMoney = 0.05f;
    float addWheat = 0.05f;

    City(Sprite2D sprite, Sprite2D soldierSprite, SpriteText spriteText, int x, int y) {
        this.sprite = sprite;
        soldier = new Soldier(this, soldierSprite, spriteText);
        soldiers = new ArrayList<>();
    }

    void draw(GL10 gl, Context context) {
        sprite.draw(gl);
        soldier.draw(gl, context);
        for (int i = soldiers.size() - 1; i >= 0; i--) {
            if (soldier.isKieru()) {//敵部隊と接触を受け事前にやられている可能性があるため
                soldiers.remove(soldier);
                HyperMotion2D.soldierList.remove(soldier);
            }

            Soldier soldier = soldiers.get(i);
            soldier.draw(gl, context);

            if (soldier.isKieru()) {//上のdraw()の中で部隊がやられた可能性があるため
                soldiers.remove(soldier);
                HyperMotion2D.soldierList.remove(soldier);
            }
        }
    }

    void select() {
        isSelected = !isSelected;
        if (isSelected) sprite._texX = 400 * 3;
        else sprite._texX = 400 * FRIEND_CASTLE;
    }

    void attack(City city) {
        float count = soldier.getCount();
        float expeditionCount = 0;

        if (this.state == FRIEND_CASTLE) {
            if (HyperMotion2D.wheat > count / 2)
                expeditionCount = count / 2;
            else
                expeditionCount = HyperMotion2D.wheat;
            HyperMotion2D.wheat -= expeditionCount;
        }else {
            float wheat = HyperMotion2D.cpus[state - 2].wheat;
            if (wheat > count / 2)
                expeditionCount = count / 2;
            else
                expeditionCount = wheat;
            HyperMotion2D.cpus[state - 2].wheat -= expeditionCount;
        }

        soldier.setCount(count - expeditionCount);
        soldiers.add(new Soldier(this, sprite._pos._x, sprite._pos._y, city, expeditionCount));
    }

    void setState(int state) {
        this.state = state;
        sprite._texX = 400 * state;
    }

    public void shift(float diffX, float diffY) {
        sprite._pos._x += diffX;
        sprite._pos._y += diffY;
        this.soldier.shift(diffX, diffY);
        for (Soldier soldier : soldiers) {
            soldier.shift(diffX, diffY);
        }
    }


    int getState() {
        return state;
    }
}
