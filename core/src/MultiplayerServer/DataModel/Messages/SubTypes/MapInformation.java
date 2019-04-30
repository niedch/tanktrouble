package MultiplayerServer.DataModel.Messages.SubTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MapInformation {
    @JsonProperty
    private String map;

    public MapInformation(String map) {
        this.map = map;
    }

    public MapInformation() {
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
