package Tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import Utils.Box2DFactory;
import Utils.Constants;
import Utils.MusicHandler;


public class Bullet extends Sprite {
    private Body bulletBody;
    public static List<Bullet> bullets;
    private float currentLifeTime = 0f;
    private Tank tank;
    private boolean tagged = false;

    static {
        bullets = new ArrayList<Bullet>();
    }

    public Bullet(Tank tank, World world){
        super(new Texture(Gdx.files.internal(Constants.Bullet.BULLET_PATH)));
        setScale(1.5f);
        Vector2 bulletPostion = getStartPos(tank);

        System.out.println(bulletPostion.toString());
        this.tank = tank;
        bulletBody = Box2DFactory.createBody(world, BodyDef.BodyType.DynamicBody, bulletPostion, Box2DFactory.createFixture(
                Box2DFactory.createCircleShape(6f), 0, 0, 1, false
        ));

        bulletBody.setLinearVelocity(MathUtils.cos(tank.getTankBody().getAngle()) * Constants.Bullet.SHOOTING_STREN, MathUtils.sin(tank.getTankBody().getAngle()) * Constants.Bullet.SHOOTING_STREN);
        bulletBody.setUserData(this);
        add();
    }

    public static Vector2 getStartPos(Tank tank){
        Vector2 bulletPostion = new Vector2(tank.getWidth()/Constants.General.PIXEL_TO_METERS + 23f, tank.getHeight()/Constants.General.PIXEL_TO_METERS/2);
        bulletPostion.rotateRad(tank.getTankBody().getAngle()).add(tank.getTankBody().getPosition());
        return bulletPostion;
    }

    public Bullet (Vector2 startPos, float angle, World world, Tank tank){
        super(new Texture(Gdx.files.internal(Constants.Bullet.BULLET_PATH)));
        setScale(1.5f);

        this.tank = tank;
        bulletBody = Box2DFactory.createBody(world, BodyDef.BodyType.DynamicBody, startPos, Box2DFactory.createFixture(
                Box2DFactory.createCircleShape(6f), 0, 0, 1, false
        ));

        bulletBody.setLinearVelocity(MathUtils.cos(angle) * Constants.Bullet.SHOOTING_STREN, MathUtils.sin(angle) * Constants.Bullet.SHOOTING_STREN);
        bulletBody.setUserData(this);
        add();
    }

    public void update(float dt){
            currentLifeTime += dt;
            if (currentLifeTime >= Constants.Bullet.BULLET_LIFETIME) {
                setTagged(true);
                currentLifeTime = 0f;
            }

            setPosition(bulletBody.getPosition().x - getWidth() / 2, bulletBody.getPosition().y - getHeight() / 2);
    }

    public void add(){
        bullets.add(this);
    }

    public void remove(){
        bullets.remove(this);
        tank.getMyBullets().remove(this);
        tank.setCurrentBullets(tank.getCurrentBullets()-1);
    }

    public Body getBody(){
        return bulletBody;
    }

    public static void updateAndRender(float dt, SpriteBatch batch){
        for(Bullet bullet : bullets){
            bullet.update(dt);
            bullet.draw(batch);
        }
    }

    public boolean isTagged() {
        return tagged;
    }

    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    public Tank getTank() {
        return tank;
    }
}
