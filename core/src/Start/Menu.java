package Start;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Levels.LocalMultiplayer;
import MultiplayerServer.MainServer;
import Skins.BasicSkin;
import MultiplayerClient.EnterPlayerNameScreen;
import Utils.MusicHandler;

public class Menu implements Screen {
	private Stage stage;
	private Table table;
	private Skin skin;
	private Viewport viewport;
	private Music bg;

	public Menu(Music bg) {
		this.bg = bg;
		bg.setVolume(0.2f);
	}

	@Override
	public void show() {
		viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		stage = new Stage(viewport);
		
        Gdx.input.setInputProcessor(stage);// Make the stage consume events
        skin = new BasicSkin();
        table = new Table(skin);
		table.setFillParent(true);

		final TextButton muteButton = new TextButton("",skin,"volume");
		muteButton.setSize(50,50);
		muteButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(MusicHandler.isMute){
					MusicHandler.isMute = false;
					bg.play();

				}
				else {
					MusicHandler.isMute = true;
					bg.pause();
				}
			}
		});


		table.add(muteButton).top().right().expandY().row();

        
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
			TextButton localMultiplayer = new TextButton("Local Multiplayer", skin); // Use the initialized skin
			localMultiplayer.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					bg.stop();
					((Game) Gdx.app.getApplicationListener()).setScreen(new LocalMultiplayer());
				}
			});
			table.add(localMultiplayer).row();
		}

        TextButton twoPlayerBtn = new TextButton("Connect to Server", skin); // Use the initialized skin
        twoPlayerBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new EnterPlayerNameScreen(bg));
			}
		});
        table.add(twoPlayerBtn).row();

        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
			TextButton threePlayerBtn = new TextButton("Start Server", skin); // Use the initialized skin
			threePlayerBtn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							new MainServer();
						}
					}).start();
				}
			});
			table.add(threePlayerBtn).row();
			table.add().expand();
		}
        stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		 Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
