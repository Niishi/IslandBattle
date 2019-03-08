package com.example.islandbattle;

import com.example.islandbattle.City;
import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

public class Farm extends City {
    Farm(Sprite2D sprite, Sprite2D soldierSprite, SpriteText spriteText, int x, int y){
        super(sprite, soldierSprite, spriteText, x, y);
        super.addCount = 0.025f;
        super.addWheat = 0.1f;
    }
}
