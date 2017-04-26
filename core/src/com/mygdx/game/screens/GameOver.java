package com.mygdx.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Shuni on 3/19/17.
 * Tutorial from https://www.youtube.com/watch?v=CNjkPPveqG8
 * https://bitbucket.org/dermetfan/blackpoint2/downloads/
 */
public class GameOver implements Screen{

    private Stage stage;
    private Skin skin;
    private Table table;

    private int initialWidth;
    private int initialHeight;
    private boolean gameStatus;

    public GameOver() {
        initialWidth = 0;
        initialHeight = 0;
    }

    public GameOver(int w, int h) {
        initialWidth = w;
        initialHeight = h;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        table.invalidateHierarchy();
    }

    @Override
    public void show() {

        float w = (this.initialWidth == 0) ? Gdx.graphics.getWidth() : this.initialWidth;
        float h = (this.initialHeight== 0) ? Gdx.graphics.getHeight() : this.initialHeight;

        Viewport viewport = new FitViewport(w, h, new OrthographicCamera());
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));

        table = new Table(skin);
        table.setFillParent(true);

        // creating heading
        Label heading = new Label("Game Over", skin, "default");
        heading.setFontScale(2);

        // creating buttons
        TextButton buttonExit = new TextButton("EXIT", skin, "default");
        buttonExit.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonExit.pad(15);

        // creating play again buttons
//        TextButton buttonPlay = new TextButton("Play again", skin, "default");
//        buttonPlay.addListener(new ClickListener() {
//
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                stage.addAction(sequence(moveTo(0, -stage.getHeight(), .5f), run(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        ((Game) Gdx.app.getApplicationListener()).setScreen(new Play(initialWidth, initialHeight));
//                    }
//                })));
//            }
//        });
//        buttonPlay.pad(15);

        // putting stuff together
        table.add(heading).spaceBottom(100).row();
        table.add(buttonExit);
//        table.add(buttonExit).spaceBottom(20).row();
//        table.add(buttonPlay).spaceBottom(15).row();

        stage.addActor(table);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
