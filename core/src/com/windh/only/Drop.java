package com.windh.only;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Drop extends Game {


    SpriteBatch batch;

    public BitmapFont font;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }


    /**
     * 释放方法：释放所有资源。
     */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
