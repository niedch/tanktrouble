package MultiplayerServer.DataModel.Messages.SubTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewShot {
    @JsonProperty
    String playerName;

    @JsonProperty
    float posX;
    @JsonProperty
    float posY;
    @JsonProperty
    float angle;

    public NewShot() {
        // Dummy Constructor
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
