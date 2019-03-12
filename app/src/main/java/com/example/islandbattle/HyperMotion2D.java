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

    private final int CITY_ROW_COUNT = 6;
    private final int CITY_COLUMN_COUNT = 6;
    private final int CASTLE_NUMBER = CITY_ROW_COUNT * CITY_COLUMN_COUNT;
    private City[] cities = new City[CASTLE_NUMBER];

    City selectedCity;

    private static final int CPU_COUNT = 1;
    static CPU[] cpus = new CPU[CPU_COUNT];

    private Sprite2D moneySprite = new Sprite2D();
    private float money = 0;
    private SpriteText moneySpriteText = new SpriteText();

    private Sprite2D wheatSprite = new Sprite2D();
    public static float wheat = 50;
    private SpriteText wheatSpriteText = new SpriteText();

    //@Override
    //コンストラクタ
    public HyperMotion2D(Context context) {
        _context = context;
    }

    //@Override
    //描画できるように毎フレーム呼び出される関数
    public void onDrawFrame(GL10 gl) {
        // 描画用バッファをクリア
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT
                | GL10.GL_DEPTH_BUFFER_BIT);

        drawCastle(gl);
        drawMoney(gl);
        drawWheat(gl);
        cpuTurn();
        updateUI();
    }

    //⑪画面の比率
    private float getRatio() {
        return (float) _width / 600.0f;
    }

    //@Override
    //サーフェイスのサイズ変更時に呼ばれる
    public void onSurfaceChanged(
            GL10 gl, int width, int height) {
    }

    //@Override
    //サーフェイスが生成される際・または再生成される際に呼ばれる
    public void onSurfaceCreated(
            GL10 gl, EGLConfig config) {
        //背景色
        gl.glClearColor(0.6f, 0.8f, 1.0f, 1.0f);
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


        for (int i = 0; i < CITY_ROW_COUNT; i++) {
            for (int j = 0; j < CITY_COLUMN_COUNT; j++) {
                Sprite2D sprite = new Sprite2D();
                int index = CITY_COLUMN_COUNT * i + j;
                if (index == 1 || index == CASTLE_NUMBER - 2)
                    sprite.setTexture(gl, _context.getResources(), R.drawable.mines);
                else if (index == 2 || index == CASTLE_NUMBER - 3)
                    sprite.setTexture(gl, _context.getResources(), R.drawable.farms);
                else
                    sprite.setTexture(gl, _context.getResources(), R.drawable.castles);
                int x = 100 + (int) (Math.random() * 200) + 400 * j;
                int y = 100 + (int) (Math.random() * 200) + 400 * i;
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
                spriteText.setTexture(gl, _context.getResources(), R.drawable.number);
                spriteText._width = Constant.SPRITE_TEXT_WIDTH;
                spriteText._texWidth = Constant.SPRITE_TEXT_WIDTH;
                spriteText._pos._x = soldierSprite._pos._x - 20;
                spriteText._pos._y = sprite._pos._y;

                if (index == 1 || index == CASTLE_NUMBER - 2)
                    cities[index] = new Mine(sprite, soldierSprite, spriteText, x, y);
                else if (index == 2 || index == CASTLE_NUMBER - 3)
                    cities[index] = new Farm(sprite, soldierSprite, spriteText, x, y);
                else
                    cities[index] = new Castle(sprite, soldierSprite, spriteText, x, y);
                cities[index].soldier.setCount(50);
            }
        }
        cities[0].setState(Castle.FRIEND_CASTLE);
        cities[CASTLE_NUMBER - 1].setState(Castle.ENEMY_CASTLE);
        cities[0].soldier.setCount(100);
        cities[CASTLE_NUMBER - 1].soldier.setCount(100);

        cpus[0] = new CPU(cities[CASTLE_NUMBER-1], 2);

        moneySprite.setTexture(gl, _context.getResources(), R.drawable.coin);
        moneySprite._pos._x = 10;
        moneySprite._pos._y = _height - 100;
        moneySprite._width = Constant.UI_SPRITE_WIDTH;
        moneySprite._height = Constant.UI_SPRITE_WIDTH;

        moneySpriteText.setTexture(gl, _context.getResources(), R.drawable.number);
        moneySpriteText._pos._x = moneySprite._pos._x + moneySprite._width;
        moneySpriteText._pos._y = moneySprite._pos._y;
        moneySpriteText._width = Constant.SPRITE_TEXT_WIDTH;
        moneySpriteText._texWidth = Constant.SPRITE_TEXT_WIDTH;

        wheatSprite.setTexture(gl, _context.getResources(), R.drawable.wheat);
        wheatSprite._pos._x = moneySpriteText._pos._x + 100;
        wheatSprite._pos._y = _height - 100;
        wheatSprite._width = Constant.UI_SPRITE_WIDTH;
        wheatSprite._height = Constant.UI_SPRITE_WIDTH;

        wheatSpriteText.setTexture(gl, _context.getResources(), R.drawable.number);
        wheatSpriteText._pos._x = wheatSprite._pos._x + wheatSprite._width;
        wheatSpriteText._pos._y = wheatSprite._pos._y;
        wheatSpriteText._width = Constant.SPRITE_TEXT_WIDTH;
        wheatSpriteText._texWidth = Constant.SPRITE_TEXT_WIDTH;
    }

    private void drawCastle(GL10 gl) {
        for (City city : cities) {
            city.draw(gl, _context);
        }
    }

    private void drawMoney(GL10 gl) {
        moneySprite.draw(gl);
        moneySpriteText.draw(gl, (int) money, 0.9f);
        for (City city : cities) {
            if (city.getState() == City.FRIEND_CASTLE)
                money += city.addMoney;
            for(CPU cpu : cpus){
                if(city.getState() == cpu.id) cpu.money += city.addMoney;
            }
        }
    }

    private void drawWheat(GL10 gl) {
        wheatSprite.draw(gl);
        wheatSpriteText.draw(gl, (int) wheat, 0.9f);
        for (City city : cities) {
            if (city.getState() == City.FRIEND_CASTLE)
                wheat += city.addWheat;
            for(CPU cpu : cpus){
                if(city.getState() == cpu.id) cpu.wheat += city.addWheat;
            }
        }
    }

    private void cpuTurn(){
        for(CPU cpu : cpus){
            cpu.turn(cities);
        }
    }

    private void updateUI() {
        wheatSprite._pos._x = moneySpriteText._pos._x +
                moneySpriteText._width * getNumberOfDigit(money) + 100;
        wheatSpriteText._pos._x = wheatSprite._pos._x + wheatSprite._width;
    }

    private int getNumberOfDigit(float n) {
        int i = 1;
        while (n >= 10) {
            n /= 10;
            i++;
        }
        return i;
    }

    public void actionDown(float x, float y) {
    }

    private float preX = -1, preY = -1;

    public void actionMove(float x, float y) {
        if (preX != -1) {
            shift(x - preX, -(y - preY));
        }
        preX = x;
        preY = y;
    }

    private void shift(float diffX, float diffY) {
        for (City city : cities) {
            city.shift(diffX, diffY);
        }
    }

    public void actionUp(float x, float y) {
        for (City city : cities) {
            if (city.sprite.hit(x, y)) {
                if (selectedCity == null && city.getState() == Castle.FRIEND_CASTLE) {
                    city.select();
                    selectedCity = city;
                } else if (selectedCity != null && selectedCity != city) {
                    selectedCity.attack(city);
                    selectedCity.select();
                    selectedCity = null;
                } else if (selectedCity != null && selectedCity == city) {
                    selectedCity.select();
                    selectedCity = null;
                } else {
                    selectedCity = null;
                }
            }
        }
        preX = -1;
        preY = -1;
    }
}
