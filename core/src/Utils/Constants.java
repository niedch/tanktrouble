package Utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Constants {
  public static class General{
      public final static SpriteBatch batch = new SpriteBatch();
      public final static int PIXEL_TO_METERS = 4;
      public final static int DELAY_TIME = 4;
      public final static String MAP_PATH = "maps/";
      public final static String MUSIC_PATH = "MusicAndSounds/";
      public final static String BUTTON_PACK = "button.atlas";
      public final static String MENU_BACK_MUSIC =MUSIC_PATH+"menuBg.mp3";
  }

    public static class Tank{
        public final static int TANK_LINEAR_FORCE = 9000000;
        public final static int TANK_AGULAR_FORCE = 90000;
        public final static String FRENDLY_TANK = "tanks/tankGreen.PNG";
        public final static String ENEMY_TANK = "tanks/tankRed.PNG";
        public final static String DESTROY_TANK_PARTICLES = "tanks/destroy.p";
        public final static String TANK_SHAPE = "tanks/tank.txt";
        public final static int MAX_Bullets = 3;
        public final static String SHOTSOUND = General.MUSIC_PATH+"tankShot.mp3";
        public final static String EXPLOSION = General.MUSIC_PATH+"tankexplosion.mp3";
        public final static float SHOOT_FREQ = 0.3f;
    }

    public static class Bullet{
        public final static int SHOOTING_STREN=900000000;
        public final static  String BULLET_PATH = "tanks/bullet.png";
        public final static float BULLET_LIFETIME = 10f;
        public final static String BULLET_DISAPEAR = "tanks/disabBull.p";
        public final static String BULLET_DISAPEAR_SOUND = General.MUSIC_PATH+"disapearBullet.mp3";
        public final static String BULLET_RICOCHET = General.MUSIC_PATH+"ricochet.mp3";
    }
}
