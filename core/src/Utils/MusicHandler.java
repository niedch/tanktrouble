package Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicHandler {
    public static boolean isMute = false;
    private Music backgroundMusic;
    private Music backgroundMusic2;
    private Sound ricochet;
    private Sound explosion;
    private Sound tankShot;
    private Sound disapearBullet;

    public MusicHandler(){
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.General.MUSIC_PATH+"Adamant Rivers.mp3"));
        backgroundMusic.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                backgroundMusic2.play();
            }
        });
        backgroundMusic2 = Gdx.audio.newMusic(Gdx.files.internal(Constants.General.MUSIC_PATH+"Wistful Pardon.mp3"));
        backgroundMusic2.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                backgroundMusic.play();
            }
        });

        ricochet = Gdx.audio.newSound(Gdx.files.internal(Constants.Bullet.BULLET_RICOCHET));
        explosion = Gdx.audio.newSound(Gdx.files.internal(Constants.Tank.EXPLOSION));
        tankShot = Gdx.audio.newSound(Gdx.files.internal(Constants.Tank.SHOTSOUND));
        disapearBullet = Gdx.audio.newSound(Gdx.files.internal(Constants.Bullet.BULLET_DISAPEAR_SOUND));
    }

    public void startMusic(float volume){
        backgroundMusic.setVolume(volume);
        backgroundMusic2.setVolume(volume);

        if(!isMute)backgroundMusic2.play();
    }

    public void shotSound(){
        if(!isMute)tankShot.play();
    }

    public void explosionSound(){
        if(!isMute)explosion.play(.5f);
    }

    public void ricochetSound(){
        if(!isMute)ricochet.play(0.5f);
    }

    public void disapearSound(){
        if(!isMute) disapearBullet.play();
    }

    public void dispose(){
        backgroundMusic.dispose();
        backgroundMusic2.dispose();
    }

}
