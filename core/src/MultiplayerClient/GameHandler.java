package MultiplayerClient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import Utils.MusicHandler;
import scenes.ScoreBoard;

public class GameHandler implements Screen {
    private ScoreBoard scoreBoard;
    private Socket socket;

    private MusicHandler musicHandler;

    private BufferedReader reader;
    private PrintWriter writer;

    private Stage stage;
    private Viewport viewport;

    private String map;
    private boolean startGameRound = false;
    private JSONArray players;
    private String myName;

    public GameHandler(List<String> players, Socket socket, String myName){
        this.myName = myName;
        this.scoreBoard = new ScoreBoard(players);
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        this.musicHandler = new MusicHandler();
    }


    @Override
    public void show() {
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage(viewport);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        JSONObject obj = new JSONObject(reader.readLine());
                        System.out.println(obj.toString());
                        if(obj.getString("type").equals("startRound")) {
                            JSONObject data = obj.getJSONObject("data");
                            players = data.getJSONArray("startPos");
                            map = data.getString("map");
                            startGameRound = true;
                            throw new InterruptedException();
                        }
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }catch (InterruptedException e){

                }
            }
        }).start();

        Image image = new Image(new Texture(Gdx.files.internal("loading.png")));
        image.setPosition(stage.getWidth()/2-image.getWidth()/2,stage.getHeight()/2-image.getHeight()/2);
        image.setOrigin(image.getWidth()/2,image.getHeight()/2);
        RotateByAction rba = new RotateByAction();
        rba.setAmount(360f);
        rba.setDuration(5f);
        RotateByAction rba2 = new RotateByAction();
        rba2.setAmount(360f);
        rba2.setDuration(1f);

        Action action = new SequenceAction(rba,rba2);
        image.addAction(Actions.forever(action));
        stage.addActor(image);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if(startGameRound){
            System.out.println("NEW Round started!");
            startGameRound = false;
            musicHandler.startMusic(.3f);
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MultiplayerScreen(map, players, socket, this, musicHandler));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }
}
