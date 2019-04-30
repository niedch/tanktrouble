package MultiplayerClient;

import MultiplayerServer.DataModel.Messages.SetPlayerName;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import Skins.BasicSkin;
import Start.Menu;

public class EnterPlayerNameScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Viewport viewport;
    private Music bg;

    public EnterPlayerNameScreen(Music bg) {
        this.bg = bg;
    }

    @Override
    public void show() {
        viewport = new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        skin = new BasicSkin();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
            table.setFillParent(true);
            Label lable = new Label("IP-Address :",skin);
            table.add(lable).fill();

            final TextField ipAddress = new TextField("",skin);
            table.add(ipAddress).fill().row();

            lable = new Label("Your PlayerName :",skin);
            table.add(lable).fill();

            final TextField textField = new TextField("",skin);
            table.add(textField).fill().row();

            TextButton textButton = new TextButton("Connect",skin);
                textButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        SocketHints socketHints = new SocketHints();
                        Socket socket = null;
                        try {
                            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, ipAddress.getText(), 9999, socketHints);
                            SetPlayerName setPlayerName = new SetPlayerName(textField.getText());

                            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                            writer.println(setPlayerName.toString());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            try {
                                JSONObject result = new JSONObject(reader.readLine());
                                if (result.getString("type").equals("ok")) {
                                    MoveByAction mba = new MoveByAction();
                                    mba.setAmount(-500, 0);
                                    mba.setDuration(1f);
                                    final Socket finalSocket = socket;
                                    stage.addAction(new SequenceAction(mba, Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((Game) Gdx.app.getApplicationListener()).setScreen(new LobbyScreen(finalSocket, textField.getText(), bg));
                                        }
                                    })));
                                } else if (result.getString("type").equals("nok")) {
                                    MoveByAction mba = new MoveByAction();
                                    mba.setAmount(-20, 0);
                                    mba.setDuration(.1f);

                                    MoveByAction mba1 = new MoveByAction();
                                    mba1.setAmount(40, 0);
                                    mba1.setDuration(.1f);

                                    MoveToAction mta = new MoveToAction();
                                    mta.setPosition(0, 0);
                                    mta.setDuration(.1f);
                                    stage.addAction(new SequenceAction(mba, mba1, mta));

                                    return;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (GdxRuntimeException e) {
                            MoveByAction mba = new MoveByAction();
                            mba.setAmount(-20, 0);
                            mba.setDuration(.1f);

                            MoveByAction mba1 = new MoveByAction();
                            mba1.setAmount(40, 0);
                            mba1.setDuration(.1f);

                            MoveToAction mta = new MoveToAction();
                            mta.setPosition(0, 0);
                            mta.setDuration(.1f);
                            stage.addAction(new SequenceAction(mba, mba1, mta));

                            return;
                        }
                    }
                });
            table.add(textButton);

            TextButton backBtn = new TextButton("Back",skin);
            backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Menu(bg));
            }
            });
            table.add(backBtn).fill();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
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
        stage.dispose();
    }
}
