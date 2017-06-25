package Start;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MainClass extends Game {
	@Override
	public void create () {
		((Game)Gdx.app.getApplicationListener()).setScreen(new SplashScreen());
	}

}
