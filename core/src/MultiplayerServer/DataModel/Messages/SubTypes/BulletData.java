package MultiplayerServer.DataModel.Messages.SubTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulletData {
    @JsonProperty
    private int count;

    @JsonProperty
    private float posX;
    @JsonProperty
    private float posY;

    @JsonProperty
    private float vecX;
    @JsonProperty
    private float vecY;

    public BulletData() {
        // Dummy constructor
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public float getVecX() {
        return vecX;
    }

    public void setVecX(float vecX) {
        this.vecX = vecX;
    }

    public float getVecY() {
        return vecY;
    }

    public void setVecY(float vecY) {
        this.vecY = vecY;
    }
}
