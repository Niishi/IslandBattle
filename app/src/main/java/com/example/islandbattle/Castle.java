package com.example.islandbattle;

import android.content.Context;

import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Castle extends City{


    Castle(Sprite2D sprite, Sprite2D soldierSprite,SpriteText spriteText, int x, int y){
        super(sprite, soldierSprite, spriteText, x, y);
    }

}
