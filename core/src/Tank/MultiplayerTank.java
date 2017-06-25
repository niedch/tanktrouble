package Tank;

import com.badlogic.gdx.physics.box2d.World;

import Utils.MusicHandler;

/**
 * Created by Christoph on 23.03.2016.
 */
public class MultiplayerTank extends Tank{
    public MultiplayerTank(World world, String tankSkin, String tankName , MusicHandler musicHandler) {
        super(world, tankSkin, tankName, musicHandler);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
