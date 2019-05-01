package MultiplayerClient;

import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.MessageUtils;
import MultiplayerServer.DataModel.Messages.Died;
import MultiplayerServer.DataModel.Messages.Shot;
import MultiplayerServer.DataModel.Messages.SubTypes.*;
import MultiplayerServer.DataModel.Messages.UpdateBullet;
import MultiplayerServer.DataModel.Messages.UpdatePosition;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingClient;
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
import java.util.List;

import Levels.GameState;
import Tank.FriendlyTank;
import Tank.MultiplayerTank;
import Tank.Bullet;
import Tank.Tank;
import Utils.Constants;
import Utils.Map;
import Utils.MusicHandler;

public class MultiplayerScreen extends GameState {
    static final String TAG = "MultiplayerScreen";
    private GameHandler gameHandler;
    private List<StartPosition> startPositions;
    private String mapFile;
    private FriendlyTank localTank;
    private BufferedReader reader;
    private PrintWriter writer;
    private TankPosition updateTank = null;
    private Socket socket;
    private NewShot whoShot = null;
    private boolean stopThread = false;
    private ScoreBoard scoreBoard = null;
    private TankBullets bulletData = null;

    public MultiplayerScreen(String map, List<StartPosition> startPositions, Socket socket, GameHandler gameHandler, MusicHandler musicHandler) {
        super(musicHandler);
        this.gameHandler = gameHandler;
        this.startPositions = startPositions;
        this.mapFile = map;
        this.writer = new PrintWriter(socket.getOutputStream(),true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.socket = socket;
    }

    @Override
    public void show() {
        for(StartPosition startPosition: startPositions){
            String player = startPosition.getPlayerName();
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
                    for (;;) {
                        if (localTank.hasMoved() && !world.isLocked()) {
                            TankPosition tankPosition = new TankPosition();
                            tankPosition.setPlayerName(localTank.getTankName());

                            tankPosition.setPosX(localTank.getTankBody().getPosition().x);
                            tankPosition.setPosY(localTank.getTankBody().getPosition().y);
                            tankPosition.setAngle(localTank.getTankBody().getAngle());

                            writer.println(new UpdatePosition(tankPosition));
                        }

                        if (localTank.hasShot) {
                            NewShot shot = new NewShot();
                            shot.setPlayerName(localTank.getTankName());
                            shot.setPosX(Bullet.getStartPos(localTank).x);
                            shot.setPosY(Bullet.getStartPos(localTank).y);
                            shot.setAngle(localTank.getTankBody().getAngle());

                            writer.println(new Shot(shot));
                            localTank.hasShot = false;
                        }

                        if (localTank.isDead()) {
                            Message died = new Died();
                            writer.println(died.toString());
                            throw new InterruptedException();
                        }

                        if(stopThread){
                            throw new InterruptedException();
                        }

                        if((localTank.getMyBullets().size() != 0)){
                            UpdateBullet updateBullet = getBulletUpdateData();
                            writer.println(updateBullet);
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
                        IWorkingClient message = (IWorkingClient) MessageUtils.deserialize(reader.readLine());
                        message.workClient(MultiplayerScreen.this);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                }
            }
        }).start();

        map = new Map(world,camera,mapFile,startPositions);
        super.show();
    }

    private UpdateBullet getBulletUpdateData() {
        TankBullets tankBullets = new TankBullets(localTank.getTankName());

        for(int i = 0; i < localTank.getMyBullets().size(); i++){
            BulletData bulletData = new BulletData();
            bulletData.setCount(i);

            bulletData.setPosX(localTank.getMyBullets().get(i).getBody().getPosition().x);
            bulletData.setPosY(localTank.getMyBullets().get(i).getBody().getPosition().y);

            bulletData.setVecX(localTank.getMyBullets().get(i).getBody().getLinearVelocity().x);
            bulletData.setVecY(localTank.getMyBullets().get(i).getBody().getLinearVelocity().y);

            tankBullets.bulletDataList.add(bulletData);
        }
        return new UpdateBullet(tankBullets);
    }

    @Override
    public void render(float delta) {
        if(updateTank != null) {
            for (Tank tank : Tank.tanks) {
                if (tank.getTankName().equals(updateTank.getPlayerName())) {
                    tank.getTankBody().setTransform(updateTank.getPosX(),updateTank.getPosY(),updateTank.getAngle());
                }
            }
            updateTank = null;
        }

        if(whoShot != null) {
            for (Tank tank : Tank.tanks) {
                if (tank.getTankName().equals(whoShot.getPlayerName())) {
                    tank.shoot(new Vector2(whoShot.getPosX(),whoShot.getPosY()),whoShot.getAngle());
                }
            }
            whoShot = null;
        }

        if(stopThread){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MultiplayerScoreScreen(scoreBoard, socket, gameHandler, musicHandler));
        }

        if(bulletData != null){
            try {
                for (Tank tank : Tank.tanks) {
                    if (tank.getTankName().equals(bulletData.getPlayerName())) {
                        for (BulletData bulletDataEntry: bulletData.getBulletDataList()) {
                            tank.getMyBullets().get(bulletDataEntry.getCount()).getBody().setTransform(bulletDataEntry.getPosX(), bulletDataEntry.getPosY(), 0);
                            tank.getMyBullets().get(bulletDataEntry.getCount()).getBody().setLinearVelocity(bulletDataEntry.getVecX(), bulletDataEntry.getVecY());
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

    public void setUpdateTank(TankPosition updateTank) {
        this.updateTank = updateTank;
    }

    public void setWhoShot(NewShot whoShot) {
        this.whoShot = whoShot;
    }

    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void setBulletData(TankBullets bulletData) {
        this.bulletData = bulletData;
    }

    public void setStopThread(boolean stopThread) {
        this.stopThread = stopThread;
    }
}
