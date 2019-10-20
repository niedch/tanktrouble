package MultiplayerServer.DataModel.Messages.SubTypes;

import com.badlogic.gdx.math.Rectangle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StartPosition {
    @JsonProperty
    private float posX;
    @JsonProperty
    private float posY;

    @JsonProperty
    private String playerName;

    @JsonProperty
    private float width;
    @JsonProperty
    private float height;

    public StartPosition() {
    }

    public void setRectangle(Rectangle rectangle) {
        this.setPosX(rectangle.getX());
        this.setPosY(rectangle.getY());

        this.setWidth(rectangle.getWidth());
        this.setHeight(rectangle.getHeight());
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
