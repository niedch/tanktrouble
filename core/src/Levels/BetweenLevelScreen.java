package Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Map;

import javax.swing.text.View;

import Skins.BasicSkin;
import Utils.MusicHandler;
import scenes.ScoreBoard;

/**
 * Created by Christoph on 19.03.2016.
 */
public class BetweenLevelScreen implements Screen {
    Stage stage;
    Skin skin;
    Viewport viewport;
    public BetweenLevelScreen(final ScoreBoard scoreBoard, final MusicHandler musicHandler){
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        skin = new BasicSkin();
        Table table = new Table();
        table.setFillParent(true);

            for(Map.Entry<String, Integer> entry : scoreBoard.getScoreMap().entrySet()){
                Label label;
                if(entry.getKey().equals(" Player 1 ")){
                    label = new Label(entry.getKey()+" : "+entry.getValue().intValue(),skin,"playerGreen");
                }else{
                    label = new Label(entry.getKey()+" : "+entry.getValue().intValue(),skin,"playerRed");
                }
                table.add(label).row();
            }
        stage.addActor(table);

        stage.addAction(new SequenceAction(Actions.delay(5f),Actions.run(new Runnable() {
            @Override
            public void run() {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LocalMultiplayer(scoreBoard, musicHandler));
            }
        })));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
