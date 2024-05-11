package com.windh.only;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * @author hsc
 * @date 2024/5/11 11:20
 */
public class Bucket {

    private Rectangle rectangle;

    private Texture image;

    private SpriteBatch batch;

    private OrthographicCamera camera;

    public static final float WIDTH = 64.0f;

    public static final float HEIGHT = 64.0f;

    public Bucket(SpriteBatch batch, OrthographicCamera camera) {
        this.batch = batch;
        this.camera = camera;
        image = new Texture("bucket.png");
    }

    public void create() {
        // 初始化桶的位置
        rectangle = new com.badlogic.gdx.math.Rectangle();
        rectangle.x = (float) (800.0 / 2.0 - 64.0 / 2.0);
        rectangle.y = 20;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;
    }

    public void draw() {
        batch.draw(image, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void dispose() {
        image.dispose();
    }

    /**
     * 处理用户输入，移动桶。
     */
    public void handleUserInput() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            rectangle.x = touchPos.x - 64.0f / 2.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rectangle.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rectangle.x += 200 * Gdx.graphics.getDeltaTime();
        }

        // 确保桶不会超出屏幕边界
        constrainBucket();
    }

    /**
     * 确保桶的位置不会超出屏幕边界。
     */
    private void constrainBucket() {
        if (rectangle.x < 0) rectangle.x = 0;
        if (rectangle.x > 800 - 64) rectangle.x = 800 - 64;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }
}
