package Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Christoph on 18.03.2016.
 */
public class ParticleSystem {
    private final ParticleEffect destroyTankEffect;
    private final ParticleEffect disapearBullet;

    public ParticleSystem(){
        destroyTankEffect =  new ParticleEffect();
        destroyTankEffect.load(Gdx.files.internal(Constants.Tank.DESTROY_TANK_PARTICLES), Gdx.files.internal(""));
        destroyTankEffect.scaleEffect(0.3f);
        destroyTankEffect.setDuration(100);

        disapearBullet = new ParticleEffect();
        disapearBullet.load(Gdx.files.internal(Constants.Bullet.BULLET_DISAPEAR),Gdx.files.internal(""));
        disapearBullet.scaleEffect(0.2f);
    }

    public void drawParticles(SpriteBatch batch, float dt){
        destroyTankEffect.draw(batch, dt);
        disapearBullet.draw(batch, dt);
    }

    public void destroyTank(Vector2 postion){
        for(ParticleEmitter emitter : destroyTankEffect.getEmitters()){
            emitter.setPosition(postion.x,postion.y);
        }
        destroyTankEffect.start();
    }

    public void disapearBullet(Vector2 position, float angle){
        disapearBullet.getEmitters().first().setPosition(position.x,position.y);
        ScaledNumericValue val = disapearBullet.getEmitters().first().getAngle();
        float h1 = angle + 90f;
        float h2 = angle - 90f;
        val.setHigh(h1, h2);
        val.setLow(angle);
        disapearBullet.start();
    }
}
