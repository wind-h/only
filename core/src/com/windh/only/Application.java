package com.windh.only;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class Application extends ApplicationAdapter {

    // 雨滴图片
    private Texture dropImage;
    // 桶图片
    private Texture bucketImage;
    // 雨滴声音
    private Sound dropSound;
    // 下雨背景音乐
    private Music rainMusic;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    // 桶的矩形区域
    private Rectangle bucket;
    // 雨滴的矩形区域
    private Array<Rectangle> drops;
    // 上一个雨滴生成的时间戳
    private long lastDropTime;


    @Override
    public void create() {
        dropImage = new Texture("dropplet.png");
        bucketImage = new Texture("bucket.png");

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // 初始化桶的位置
        bucket = new Rectangle();
        bucket.x = (float) (800.0 / 2.0 - 64.0 / 2.0);
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        // 初始化雨滴数组并生成第一滴雨
        drops = new Array<>();
        spawnRaindrop();
    }

    /**
     * 生成一个新的雨滴，并更新最后生成雨滴的时间戳。
     */
    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        drops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
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
        batch.draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle raindrop : drops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.end();

        // 处理用户输入，移动桶
        handleUserInput();

        // 检查是否需要生成新的雨滴，移动雨滴，并移除超出屏幕或与桶相交的雨滴
        updateRaindrops();
    }

    /**
     * 处理用户输入，移动桶。
     */
    private void handleUserInput() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64.0f / 2.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // 确保桶不会超出屏幕边界
        constrainBucket();
    }

    /**
     * 确保桶的位置不会超出屏幕边界。
     */
    private void constrainBucket() {
        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > 800 - 64) bucket.x = 800 - 64;
    }

    /**
     * 更新雨滴状态：移动雨滴，移除超出屏幕的雨滴，
     * 如果雨滴与桶相交，则播放水滴声并移除该雨滴。
     */
    private void updateRaindrops() {
        for (Iterator<Rectangle> iter = drops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
            }
            if (raindrop.overlaps(bucket)) {
                dropSound.play();
                iter.remove();
            }
        }

        // 根据设定的时间间隔检查是否需要生成新的雨滴
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
    }

    /**
     * 释放方法：释放所有资源。
     */
    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }
}
