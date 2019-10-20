package MultiplayerClient;

import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.MessageUtils;
import MultiplayerServer.DataModel.Messages.GameStart;
import MultiplayerServer.DataModel.Messages.UpdateLobby;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Skins.BasicSkin;
import Start.Menu;

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
                        Message message = MessageUtils.deserialize(reader.readLine());
                        if (message instanceof UpdateLobby) {
                            updateLobby(((UpdateLobby)message).getPlayers());
                        } else if (message instanceof GameStart) {
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

    public void updateLobby(List<String> players){
        table.clear();
        this.players = players;

        Label lobbyLabel = new Label("Lobby",skin);
        lobbyLabel.setFontScale(2);
        table.add(lobbyLabel).row();

        for(String player: players){
            Label label = new Label(player, skin);

            if(player.equals(myName)){
                label = new Label(player, skin,"myPlayer");
            }

            table.add(label).row();
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
