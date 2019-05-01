package MultiplayerServer.DataModel.Messages.SubTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TankBullets {
    @JsonProperty
    public String playerName;

    @JsonProperty
    public List<BulletData> bulletDataList;

    public TankBullets(String playerName) {
        this.playerName = playerName;
        this.bulletDataList = new ArrayList<>();
    }

    public TankBullets() {
        // Dummy constructor
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<BulletData> getBulletDataList() {
        return bulletDataList;
    }

    public void setBulletDataList(List<BulletData> bulletDataList) {
        this.bulletDataList = bulletDataList;
    }
}
