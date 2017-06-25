package Start;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import Utils.Constants;

public class SplashScreen implements Screen {

	private Stage stage;
	private Image simuImage;
	
	@Override
	public void show() {
		stage = new Stage();
		
		simuImage = new Image(new Texture(Gdx.files.internal("splash.jpg")));
		
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		simuImage.setSize(width, height);
		final Music bg = Gdx.audio.newMusic(Gdx.files.internal(Constants.General.MENU_BACK_MUSIC));
		bg.setVolume(.6f);
		bg.setLooping(true);
		bg.play();
		simuImage.addAction(Actions.sequence(Actions.fadeIn(1f),Actions.delay(1f),Actions.fadeOut(0.5f),Actions.run(new Runnable() {
			@Override
			public void run() {
				((Game)Gdx.app.getApplicationListener()).setScreen(new Menu(bg));
			}
		})));
		
		simuImage.setPosition((width - simuImage.getWidth())*0.5f, (height - simuImage.getHeight())*0.5f);
		stage.addActor(simuImage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);
			
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		
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
		dispose();
	}

	


}
