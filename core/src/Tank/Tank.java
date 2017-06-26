package Tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utils.Constants;
import Utils.FizzXJSonLoader;
import Utils.MusicHandler;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public abstract class Tank extends Sprite implements InputProcessor{
    private World world;
    private Body tankBody;
    public static List<Tank> tanks;
    private int currentBullets = 0;
    private String tankName;
    private MusicHandler musicHandler;
    private List<Bullet> myBullets;
    private long lastShotTime = 0;
    private PointLight pointLight;

    protected boolean forward = false, backwards = false, left = false, right= false, isDead = false;

    static{
        tanks = new ArrayList<Tank>();
    }

    public static void initPointLight(RayHandler rayHandler){
        Vector2 vec;
        for(Tank tank: tanks){
            vec = Bullet.getStartPos(tank);
            tank.setPointLight(new PointLight(rayHandler, 1000, Color.WHITE, 1000, vec.x, vec.y));
        }
    }

    public static void updatePointLight(){
        Vector2 vec;
        for(Tank tank: tanks) {
            vec = Bullet.getStartPos(tank);
            tank.getPointLight().setPosition(vec.x,vec.y);
        }
    }



    public Tank(World world, String tankSkin, String tankName, MusicHandler musicHandler){
        super(new Texture(Gdx.files.internal(tankSkin)));
        this.myBullets = new ArrayList<Bullet>();
        this.setScale(1.3f);
        this.world = world;
        this.tankName = tankName;
        this.musicHandler = musicHandler;
        add(this);
    }

    public void create(Vector2 position){
        FizzXJSonLoader loader = new FizzXJSonLoader(Gdx.files.internal(Constants.Tank.TANK_SHAPE), Constants.General.PIXEL_TO_METERS);
        try {
            loader.loadContent();
            tankBody = loader.getBody(world,position,loader.getFixtureList(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        tankBody.setLinearDamping(6);
        tankBody.setAngularDamping(50);
        tankBody.setUserData(this);
    }

    public void applyLinearStrength(Body body, float angle, int strength){
        body.applyForceToCenter(MathUtils.cos(angle) * strength, MathUtils.sin(angle) * strength, true);
    }

    public void update() {
        setPosition(tankBody.getPosition().x - getWidth() / 2, tankBody.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(tankBody.getAngle())-270f);

        if(tankBody == null) return;
        if(forward) applyLinearStrength(tankBody,tankBody.getAngle(), Constants.Tank.TANK_LINEAR_FORCE);
        if(backwards) applyLinearStrength(tankBody, tankBody.getAngle(), -Constants.Tank.TANK_LINEAR_FORCE);
        if(left) tankBody.applyAngularImpulse(Constants.Tank.TANK_AGULAR_FORCE,true);
        if(right) tankBody.applyAngularImpulse(-Constants.Tank.TANK_AGULAR_FORCE, true);
    }

    public void shoot(){
        System.out.println(System.currentTimeMillis()+ " - " + lastShotTime +" = "+ (System.currentTimeMillis() - lastShotTime));
        if(!isDead && tankBody != null && currentBullets < Constants.Tank.MAX_Bullets && (System.currentTimeMillis() - lastShotTime) > (Constants.Tank.SHOOT_FREQ * 1000)){
            myBullets.add(new Bullet(this,world));
            currentBullets++;
            musicHandler.shotSound();
            lastShotTime = System.currentTimeMillis();
        }
    }

    public void shoot(Vector2 pos, float angle){
        System.out.println(System.currentTimeMillis()+ " - " + lastShotTime +" = "+ (System.currentTimeMillis() - lastShotTime));
        if(!isDead && tankBody != null && currentBullets < Constants.Tank.MAX_Bullets && (System.currentTimeMillis() - lastShotTime) > (Constants.Tank.SHOOT_FREQ * 1000)){
            myBullets.add(new Bullet(pos,angle,world,this));
            currentBullets++;
            musicHandler.shotSound();
            lastShotTime = System.currentTimeMillis();
        }
    }

    public static void updateAndRenderTanks(SpriteBatch batch){
        for(Tank tank : tanks){
            if(!tank.isDead) {
                tank.update();
                tank.draw(batch);
            }
        }
    }

    public PointLight getPointLight() {
        return pointLight;
    }

    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    public void add(Tank tank){
        tanks.add(tank);
    }

    public Body getTankBody() {
        return tankBody;
    }

    public boolean isDead() {
        return isDead;
    }

    public void kill(){
        isDead = true;
    }

    public int getCurrentBullets() {
        return currentBullets;
    }

    public void setCurrentBullets(int currentBullets) {
        this.currentBullets = currentBullets;
    }

    public String getTankName() {
        return tankName;
    }

    public List<Bullet> getMyBullets() {
        return myBullets;
    }
}
