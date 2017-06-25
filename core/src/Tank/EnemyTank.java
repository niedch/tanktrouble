package Tank;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.World;

import Utils.Constants;
import Utils.MusicHandler;

/**
 * Created by Christoph on 18.03.2016.
 */
public class EnemyTank extends Tank{
    public EnemyTank(World world, String tankName, MusicHandler musicHandler) {
        super(world, Constants.Tank.ENEMY_TANK,tankName, musicHandler);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP){
            this.forward = true;
        }else if(keycode == Input.Keys.LEFT){
            this.left = true;
        }else if(keycode == Input.Keys.DOWN){
            this.backwards = true;
        }else if(keycode == Input.Keys.RIGHT){
            this.right = true;
        }else if(keycode== Input.Keys.L){
            this.shoot();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.UP){
            this.forward = false;
        }else if(keycode == Input.Keys.LEFT){
            this.left = false;
        }else if(keycode == Input.Keys.DOWN){
            this.backwards = false;
        }else if(keycode == Input.Keys.RIGHT){
            this.right  = false;
        }
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
