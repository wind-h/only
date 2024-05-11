package com.windh.only;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class Game extends ApplicationAdapter {

    // 雨滴声音
    private Sound dropSound;
    // 下雨背景音乐
    private Music rainMusic;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    // 雨滴的矩形区域
    private Array<Rectangle> drops;
    // 上一个雨滴生成的时间戳
    private long lastDropTime;

    private Bucket bucket;

    private Array<Rain> rainList = new Array<>();


    @Override
    public void create() {
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        bucket = new Bucket(batch, camera);
        bucket.create();
        Rain rain = new Rain(camera, batch);
        rain.create();
        lastDropTime = TimeUtils.nanoTime();
        rainList.add(rain);
    }

    @Override
    public void render() {
        // 清理屏幕
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // 更新相机
        camera.update();

        // 设置批次使用相机投影矩阵
        batch.setProjectionMatrix(camera.combined);

        // 开始绘制批次，绘制桶和所有雨滴
        batch.begin();
        bucket.draw();
        for (Rain rain : rainList) {
            rain.draw();
        }
        batch.end();

        // 处理用户输入，移动桶
        bucket.handleUserInput();

        // 检查是否需要生成新的雨滴，移动雨滴，并移除超出屏幕或与桶相交的雨滴
        updateRaindrops();
    }

    /**
     * 更新雨滴状态：移动雨滴，移除超出屏幕的雨滴，
     * 如果雨滴与桶相交，则播放水滴声并移除该雨滴。
     */
    private void updateRaindrops() {
        for (Iterator<Rain> iter = rainList.iterator(); iter.hasNext(); ) {
            Rain rain = iter.next();
            Rectangle raindrop = rain.getRectangle();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
            }
            if (raindrop.overlaps(bucket.getRectangle())) {
                dropSound.play();
                iter.remove();
            }
        }
        // 根据设定的时间间隔检查是否需要生成新的雨滴
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            Rain rain = new Rain(camera, batch);
            rain.create();
            lastDropTime = TimeUtils.nanoTime();
            rainList.add(rain);
        }
    }

    /**
     * 释放方法：释放所有资源。
     */
    @Override
    public void dispose() {
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
        bucket.dispose();
        for (Rain rain : rainList) {
            rain.dispose();
        }
    }
}
