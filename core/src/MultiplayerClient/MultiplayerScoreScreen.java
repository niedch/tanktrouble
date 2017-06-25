package MultiplayerClient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import Levels.LocalMultiplayer;
import Skins.BasicSkin;
import Utils.MusicHandler;
import scenes.ScoreBoard;

/**
 * Created by Christoph on 24.03.2016.
 */
public class MultiplayerScoreScreen implements Screen {
    Stage stage;
    Skin skin;
    Viewport viewport;
    GameHandler handler;
    boolean switchScreen = false;

    public MultiplayerScoreScreen(ScoreBoard scoreBoard, Socket socket, final GameHandler handler, MusicHandler musicHandler){
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        skin = new BasicSkin();
        this.handler = handler;
        Table table = new Table();
        table.setFillParent(true);

        for(Map.Entry<String, Integer> entry : scoreBoard.getScoreMap().entrySet()){
            Label label;
            if(entry.getKey().equals(handler.getMyName())){
                label = new Label(entry.getKey()+" : "+entry.getValue().intValue(),skin,"playerGreen");
            }else{
                label = new Label(entry.getKey()+" : "+entry.getValue().intValue(),skin,"playerRed");
            }
            table.add(label).row();
        }
        stage.addActor(table);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        JSONObject result = new JSONObject(reader.readLine());
                        if(result.getString("type").equals("endShowStats")){
                            throw new InterruptedException();
                        }
                    }
                }catch (InterruptedException e){
                    switchScreen = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if(switchScreen) ((Game) Gdx.app.getApplicationListener()).setScreen(handler);
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
