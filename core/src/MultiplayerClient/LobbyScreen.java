package MultiplayerClient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Skins.BasicSkin;
import Start.Menu;
import scenes.ScoreBoard;

public class LobbyScreen implements Screen {
    public static final String TAG = "Lobby";

    private Stage stage;
    private Skin skin;
    private Table table;
    private Viewport viewport;
    private List<String> players;

    private BufferedReader reader;
    private PrintWriter writer;

    private boolean switchHandler = false;
    private Socket socket;

    private String myName;
    private Music bg;

    public LobbyScreen(Socket socket, String myName,final Music bg){
        this.myName = myName;
        this.players = new ArrayList<String>();
        this.socket = socket;
        this.bg = bg;

        this.table = new Table();
        this.table.setFillParent(true);

        players = new ArrayList<String>();
        skin = new BasicSkin();

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(),true);

        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage(viewport);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        JSONObject obj = new JSONObject(reader.readLine());
                        if (obj.getString("type").equals("updateLobby")) {
                            updateLobby(obj.getJSONArray("data"));
                        }else if(obj.getString("type").equals("kick")){
                            writer.println(createJSONObj("disconnect",null));
                            writer.flush();
                            ((Game)Gdx.app.getApplicationListener()).setScreen(new Menu(bg));
                        }else if(obj.getString("type").equals("GameStart")){

                            throw new InterruptedException();
                        }
                    }
                } catch (IOException e) {
                    Gdx.app.exit();
                } catch (InterruptedException e){
                    switchHandler = true;
                }
            }
        }).start();

        stage.addActor(table);
    }

    public void updateLobby(JSONArray arr){
        table.clear();
        players.clear();
        for(int i = 0; i < arr.length(); i++){
            if(i == 0){
                Label label = new Label("Lobby",skin);
                label.setFontScale(2);
                table.add(label).row();
            }
            if(arr.getString(i).equals(myName)){
                Label label = new Label(arr.getString(i),skin,"myPlayer");
                table.add(label).row();
            }else{
                Label label = new Label(arr.getString(i),skin);
                table.add(label).row();
            }

            players.add(arr.getString(i));
        }
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if(switchHandler){
            bg.stop();
            ((Game)Gdx.app.getApplicationListener()).setScreen(new GameHandler(players,socket, this.getMyName()));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        writer.println(createJSONObj("disconnect",null));
        writer.flush();
    }

    public static String createJSONObj(String type, Object data){
        JSONObject obj = new JSONObject();
        obj.put("type", type);
        if(data != null){
            obj.put("data", data);
        }
        return obj.toString();
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }
}
