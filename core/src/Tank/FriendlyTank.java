package Tank;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import Utils.Constants;
import Utils.MusicHandler;

/**
 * Created by Christoph on 18.03.2016.
 */
public class FriendlyTank extends Tank{
    private Vector2 prePos;
    private float preAngle;
    public boolean hasShot = false;

    public FriendlyTank(World world, String tankName, MusicHandler musicHandler) {
        super(world, Constants.Tank.FRENDLY_TANK, tankName, musicHandler);
    }

    @Override
    public void create(Vector2 position) {
        super.create(position);
        prePos = position;
        preAngle = this.getTankBody().getAngle();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.W){
            this.forward = true;
        }else if(keycode == Input.Keys.A){
            this.left = true;
        }else if(keycode == Input.Keys.S){
            this.backwards = true;
        }else if(keycode == Input.Keys.D){
            this.right = true;
        }else if(keycode== Input.Keys.SPACE){
            this.shoot();
            hasShot = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.W){
            this.forward = false;
        }else if(keycode == Input.Keys.A){
            this.left = false;
        }else if(keycode == Input.Keys.S){
            this.backwards = false;
        }else if(keycode == Input.Keys.D){
            this.right  = false;
        }
        return false;
    }

    public boolean hasMoved(){
        if(prePos != null) {
            if (!prePos.equals(this.getTankBody().getPosition()) || preAngle != this.getTankBody().getAngle()) {
                prePos.set(this.getTankBody().getPosition());
                preAngle = this.getTankBody().getAngle();
                return true;
            }
            return false;
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
