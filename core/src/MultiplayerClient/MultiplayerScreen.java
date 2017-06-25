package MultiplayerClient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import Levels.GameState;
import Tank.FriendlyTank;
import Tank.MultiplayerTank;
import Tank.Bullet;
import Tank.Tank;
import Utils.Constants;
import Utils.Map;
import Utils.MusicHandler;
import scenes.ScoreBoard;

public class MultiplayerScreen extends GameState {
    static final String TAG = "MultiplayerScreen";
    private GameHandler gameHandler;
    private JSONArray players;
    private String mapFile;
    private FriendlyTank localTank;
    private BufferedReader reader;
    private PrintWriter writer;
    private JSONObject data = null;
    private Socket socket;
    private JSONObject whoShot = null;
    private boolean stopThread = false;
    private JSONArray scoreArr = null;
    private JSONObject bulletData = null;

    public MultiplayerScreen(String map, JSONArray players, Socket socket, GameHandler gameHandler, MusicHandler musicHandler) {
        super(musicHandler);
        this.gameHandler = gameHandler;
        this.players = players;
        this.mapFile = map;
        this.writer = new PrintWriter(socket.getOutputStream(),true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.socket = socket;
    }

    @Override
    public void show() {
        for(int i = 0; i< players.length(); i++){
            String player = players.getJSONObject(i).getString("name");
            if(!player.equals(gameHandler.getMyName())){
                new MultiplayerTank(world, Constants.Tank.ENEMY_TANK,player, musicHandler);
            }else{
                localTank = new FriendlyTank(world,player, musicHandler);
                System.out.println(player);
            }
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.currentTimeMillis();
                    for (;;) {
                        if (localTank.hasMoved() && !world.isLocked()) {
                            JSONObject object = new JSONObject();
                            object.put("name", localTank.getTankName());
                            object.put("posX", localTank.getTankBody().getPosition().x);
                            object.put("posY", localTank.getTankBody().getPosition().y);
                            object.put("angle", localTank.getTankBody().getAngle());
                            writer.println(createJSONObj("updatePos", object));
                        }

                        if (localTank.hasShot) {
                            JSONObject object = new JSONObject();
                            object.put("name", localTank.getTankName());
                            object.put("startPosX", Bullet.getStartPos(localTank).x);
                            object.put("startPosY", Bullet.getStartPos(localTank).y);
                            object.put("angle", localTank.getTankBody().getAngle());
                            writer.println(createJSONObj("shot", object));
                            System.out.println(createJSONObj("shot", object));
                            localTank.hasShot = false;
                        }

                        if (localTank.isDead()) {
                            writer.println(createJSONObj("died", null));
                            throw new InterruptedException();
                        }

                        if(stopThread){
                            throw new InterruptedException();
                        }

                        if((localTank.getMyBullets().size() != 0)){
                            JSONObject data = getBulletUpdateData();
                            writer.println(createJSONObj("updateBullet",data));
                            startTime = System.currentTimeMillis();
                        }

                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                } catch (InterruptedException e){

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true){
                        JSONObject result = new JSONObject(reader.readLine());
                        if(result.getString("type").equals("updatePos")){
                            if(!world.isLocked()) {
                                JSONObject data = result.getJSONObject("data");
                                MultiplayerScreen.this.data = data;
                            }
                        }else if(result.getString("type").equals("shot")){

                            whoShot = result.getJSONObject("data");
                        } else if(result.getString("type").equals("endRound")){
                            throw new InterruptedException(result.getJSONArray("data").toString());
                        } else if(result.getString("type").equals("updateBullet")){
                            bulletData = result.getJSONObject("data");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    stopThread = true;
                    scoreArr = new JSONArray(e.getMessage());
                }
            }
        }).start();

        map = new Map(world,camera,mapFile,players);
        super.show();
    }

    private JSONObject getBulletUpdateData() {
        JSONObject data = new JSONObject();
        data.put("name", localTank.getTankName());
        JSONArray arr = new JSONArray();
        for(int i = 0; i < localTank.getMyBullets().size(); i++){
            JSONObject obj = new JSONObject();
            obj.put("count", i);
            obj.put("posX", localTank.getMyBullets().get(i).getBody().getPosition().x);
            obj.put("posY", localTank.getMyBullets().get(i).getBody().getPosition().y);
            obj.put("vecX", localTank.getMyBullets().get(i).getBody().getLinearVelocity().x);
            obj.put("vecY", localTank.getMyBullets().get(i).getBody().getLinearVelocity().y);
            arr.put(obj);
        }
        data.put("arr", arr);
        return data;
    }

    @Override
    public void render(float delta) {
        if(data != null) {
            for (Tank tank : Tank.tanks) {
                if (tank.getTankName().equals(data.getString("name"))) {
                    tank.getTankBody().setTransform((float)data.getDouble("posX"),(float)data.getDouble("posY"),(float)data.getDouble("angle"));
                }
            }
            data = null;
        }

        if(whoShot != null) {
            for (Tank tank : Tank.tanks) {
                if (tank.getTankName().equals(whoShot.getString("name"))) {
                    tank.shoot(new Vector2((float)whoShot.getDouble("startPosX"),(float)whoShot.getDouble("startPosY")),(float)whoShot.getDouble("angle"));
                }
            }
            whoShot = null;
        }

        if(stopThread){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MultiplayerScoreScreen(new ScoreBoard(scoreArr), socket, gameHandler, musicHandler));
        }

        if(bulletData != null){
            try {
                for (Tank tank : Tank.tanks) {
                    if (tank.getTankName().equals(bulletData.getString("name"))) {
                        for (int i = 0; i < bulletData.getJSONArray("arr").length(); i++) {
                            JSONObject obj = bulletData.getJSONArray("arr").getJSONObject(i);
                            tank.getMyBullets().get(obj.getInt("count")).getBody().setTransform((float) obj.getDouble("posX"), (float) obj.getDouble("posY"), 0);
                            tank.getMyBullets().get(obj.getInt("count")).getBody().setLinearVelocity((float) obj.getDouble("vecX"), (float) obj.getDouble("vecY"));
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e){}

           bulletData = null;
        }

        super.render(delta);
    }

    @Override
    public void run() {

    }

    public static String createJSONObj(String type, Object data){
        JSONObject obj = new JSONObject();
        obj.put("type", type);
        if(data != null){
            obj.put("data", data);
        }
        return obj.toString();
    }
}
