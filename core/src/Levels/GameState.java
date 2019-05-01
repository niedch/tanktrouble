package Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

import Tank.*;
import Utils.Constants;
import Utils.Map;
import Utils.MusicHandler;
import Utils.ParticleSystem;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public abstract class GameState implements Screen,ContactListener, Runnable{
    protected Map map;
    protected OrthographicCamera camera;
    protected World world;
    private Box2DDebugRenderer b2dr;
    private ParticleSystem particleSystem;
    private Stage stage;
    private Viewport viewport;
    protected MusicHandler musicHandler;
    private RayHandler rayHandler;

    public GameState(MusicHandler musicHandler){
        Bullet.bullets.clear();
        Tank.tanks.clear();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        viewport = new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        world = new World(new Vector2(0,0),true);
        b2dr = new Box2DDebugRenderer();
        rayHandler = new RayHandler(world);
        particleSystem = new ParticleSystem();
        if(musicHandler != null) {
            this.musicHandler = musicHandler;
        }
        else {
            this.musicHandler = new MusicHandler();
            this.musicHandler.startMusic(.2f);
        }
        stage = new Stage();
    }

    @Override
    public void show() {
        InputMultiplexer im = new InputMultiplexer();
        for(Tank tank : Tank.tanks){
            if(tank instanceof InputProcessor) im.addProcessor(tank);
        }
        Gdx.input.setInputProcessor(im);

        Tank.initPointLight(rayHandler);

        world.setContactListener(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1 / 60f, 8, 3);

        map.draw();

        Constants.General.batch.setProjectionMatrix(camera.combined);
        Constants.General.batch.begin();
            Tank.updateAndRenderTanks(Constants.General.batch);
            Bullet.updateAndRender(delta, Constants.General.batch);
            particleSystem.drawParticles(Constants.General.batch, delta);
        Constants.General.batch.end();

        Tank.updatePointLight();

        stage.act(delta);
        stage.draw();
        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.updateAndRender();

        //b2dr.render(world, camera.combined);

        deleteTaggedBodies();
        endSession();
    }

    List<Tank> tmpTank = new ArrayList<Tank>();
    List<Bullet> tmpBullet = new ArrayList<Bullet>();

    public void deleteTaggedBodies(){
        tmpTank.addAll(Tank.tanks);
        for(Tank tank : tmpTank){
            if(tank.isDead()){
                world.destroyBody(tank.getTankBody());
                Tank.tanks.remove(tank);
                musicHandler.explosionSound();
            }
        }
        tmpTank.clear();

        tmpBullet.addAll(Bullet.bullets);
        for(Bullet bullet : tmpBullet){
            if(bullet.isTagged()){
                particleSystem.disapearBullet(bullet.getBody().getPosition(),bullet.getBody().getLinearVelocity().angle());
                world.destroyBody(bullet.getBody());
                bullet.remove();
                musicHandler.disapearSound();
            }
        }
        tmpBullet.clear();


        rayHandler.removeAll();
        Tank.initPointLight(rayHandler);
    }

    public void endSession() {
        if(Tank.tanks.size() == 1) {
            stage.addAction(new SequenceAction(Actions.delay(Constants.General.DELAY_TIME), Actions.run(this)));
        }
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
        Constants.General.batch.dispose();
        for(Tank tank : Tank.tanks){
            tank.getTexture().dispose();
        }
        for(Bullet bullet : Bullet.bullets){
            bullet.getTexture().dispose();
        }

        musicHandler.dispose();
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getBody().getUserData() instanceof Bullet && contact.getFixtureB().getBody().getUserData() instanceof Tank){
            ((Bullet)contact.getFixtureA().getBody().getUserData()).setTagged(true);
            ((Tank) contact.getFixtureB().getBody().getUserData()).kill();
            particleSystem.destroyTank(contact.getFixtureB().getBody().getPosition());

        }else if(contact.getFixtureB().getBody().getUserData() instanceof Bullet && contact.getFixtureA().getBody().getUserData() instanceof Tank){
            ((Bullet)contact.getFixtureB().getBody().getUserData()).setTagged(true);
            ((Tank) contact.getFixtureA().getBody().getUserData()).kill();
            particleSystem.destroyTank(contact.getFixtureA().getBody().getPosition());
        }

        if(contact.getFixtureA().getBody().getUserData() instanceof Bullet && !(contact.getFixtureB().getBody().getUserData() instanceof Tank) || contact.getFixtureB().getBody().getUserData() instanceof  Bullet && !(contact.getFixtureA().getBody().getUserData() instanceof Tank)){
            musicHandler.ricochetSound();
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
