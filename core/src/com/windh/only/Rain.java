package com.windh.only;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author hsc
 * @date 2024/5/11 11:34
 */
public class Rain {

    /**
     * x, y代表左下角的坐标
     * width, height代表矩形的宽度和高度
     */
    private Rectangle rectangle;

    private Texture image;

    private SpriteBatch batch;

    private OrthographicCamera camera;

    public static final float WIDTH = 64.0f;

    public static final float HEIGHT = 64.0f;

    public Rain(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        image = new Texture("droplet.png");
    }

    public void create() {
        rectangle = new Rectangle();
        rectangle.x = MathUtils.random(0, 800 - 64);
        rectangle.y = 480;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;
    }

    public void draw() {
        batch.draw(image, rectangle.x, rectangle.y);
    }

    public void dispose() {
        image.dispose();
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}
