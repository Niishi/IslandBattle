package com.example.islandbattle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.islandbattle.roxiga.hypermotion2d.Sprite2D;
import com.example.islandbattle.roxiga.hypermotion2d.SpriteText;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class HyperMotion2D implements GLSurfaceView.Renderer {

    private Context _context;
    public int _width;
    public int _height;
    public boolean _touch;

    private final int CASTLE_NUMBER = 8;
    private Castle[] castles = new Castle[CASTLE_NUMBER];

    //@Override
    //コンストラクタ
    public HyperMotion2D(Context context)
    {
        _context = context;
    }

    //@Override
    //描画できるように毎フレーム呼び出される関数
    public void onDrawFrame(GL10 gl)
    {
        // 描画用バッファをクリア
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT
                | GL10.GL_DEPTH_BUFFER_BIT);

        drawCastle(gl);
    }

    //⑪画面の比率
    private float getRatio()
    {
        return (float)_width/600.0f;
    }

    //@Override
    //サーフェイスのサイズ変更時に呼ばれる
    public void onSurfaceChanged(
            GL10 gl, int width, int height)
    {
    }

    //@Override
    //サーフェイスが生成される際・または再生成される際に呼ばれる
    public void onSurfaceCreated(
            GL10 gl, EGLConfig config)
    {
        //背景色
        gl.glClearColor(0.6f,0.8f,1.0f,1.0f);
        // ディザを無効化
        gl.glDisable(GL10.GL_DITHER);
        // 深度テストを有効に
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //テクスチャ機能ON
        gl.glEnable(GL10.GL_TEXTURE_2D);
        //透明可能に
        gl.glEnable(GL10.GL_ALPHA_TEST);
        //ブレンド可能に
        gl.glEnable(GL10.GL_BLEND);
        //色のブレンド方法
        gl.glBlendFunc(GL10.GL_SRC_ALPHA,
                GL10.GL_ONE_MINUS_SRC_ALPHA);

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 2; j++){
                Sprite2D sprite = new Sprite2D();
                sprite.setTexture(gl, _context.getResources(), R.drawable.castles);
                int x = (int)(Math.random() * 400) + 400 * j;
                int y = (int)(Math.random() * 400) + 400 * i;
                sprite._pos._x = x;
                sprite._pos._y = y;
                sprite._texWidth = 400;
                sprite._width = 128;
                sprite._height = 128;

                Sprite2D soldierSprite = new Sprite2D();
                soldierSprite.setTexture(gl, _context.getResources(), R.drawable.soldier);
                soldierSprite._pos._x = x + sprite._width + 10;
                soldierSprite._pos._y = y + 50;
                soldierSprite._width = 64;
                soldierSprite._height = 64;

                SpriteText spriteText = new SpriteText();
                spriteText.setTexture(gl, _context.getResources(),R.drawable.number);
                spriteText._width = 49;
                spriteText._texWidth = 49;
                spriteText._pos._x = x + sprite._width + 10;
                spriteText._pos._y = sprite._pos._y;
                castles[2*i+j] = new Castle(sprite, soldierSprite, spriteText, x, y);
            }
        }

    }

    private void drawCastle(GL10 gl){
        for(Castle castle : castles){
            castle.draw(gl);
        }
    }

    public void actionDown(float x,float y)
    {
    }

    public void actionMove(float x,float y)
    {
    }

    public void actionUp(float x,float y)
    {
        for(Castle castle : castles){
            if(castle.sprite.hit(x, y)) castle.select();
        }
    }
}
