package com.windh.only;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * @author hsc
 * @date 2024/5/11 14:30
 */
public class MenuScreen implements Screen {

    final Drop game;

    OrthographicCamera camera;

    private Integer windowW;

    private Integer windowH;

    public MenuScreen(Drop game) {
        this.game = game;
        camera = new OrthographicCamera();
        windowW = Gdx.graphics.getWidth();
        windowH = Gdx.graphics.getHeight();
        camera.setToOrtho(false, windowW, windowH);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
