package Levels;

import MultiplayerServer.DataModel.Messages.SubTypes.ScoreBoard;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import Tank.*;
import Utils.Map;
import Utils.MusicHandler;

public class LocalMultiplayer extends GameState {
    public LocalMultiplayer(){
        super(null);
        this.scoreBoard = new ScoreBoard();
    }

    public LocalMultiplayer(ScoreBoard scoreBoard, MusicHandler musicHandler){
        super(musicHandler);
        this.scoreBoard = scoreBoard;
    }

    ScoreBoard scoreBoard;

    @Override
    public void show() {
        Tank tank =  new FriendlyTank(world, " Player 1 ", musicHandler);
        Tank tank1 =  new EnemyTank(world, " Player 2 " , musicHandler);
        if(!scoreBoard.contains(tank1.getTankName())){
            scoreBoard.addRow(tank.getTankName(),0);
            scoreBoard.addRow(tank1.getTankName(),0);
        }

        map = new Map(world,camera);
        super.show();
    }

    @Override
    public void run() {
        for(Tank tank : Tank.tanks){
            scoreBoard.addWin(tank.getTankName());
        }
        Tank.tanks.clear();
        Bullet.bullets.clear();
        ((Game) Gdx.app.getApplicationListener()).setScreen(new BetweenLevelScreen(scoreBoard, musicHandler));
    }
}
