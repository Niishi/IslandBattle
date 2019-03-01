package com.example.islandbattle;

import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

import javax.microedition.khronos.opengles.GL10;

public class Soldier {

    private Sprite2D sprite2D;
    private SpriteText spriteText;
    private float score;

    Soldier(Sprite2D sprite2D, SpriteText spriteText){
        this.sprite2D = sprite2D;
        this.spriteText = spriteText;
        this.score = 0;
    }

    void draw(GL10 gl){
        sprite2D.draw(gl);
        spriteText.draw(gl, (int)score, 0.7f);
        score += 0.05f;
    }
}
