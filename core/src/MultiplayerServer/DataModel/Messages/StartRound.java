package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.SubTypes.MapInformation;
import MultiplayerServer.DataModel.Messages.SubTypes.StartPosition;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StartRound extends Message {
    @JsonProperty
    private MapInformation mapInformation;
    @JsonProperty
    private List<StartPosition> positions;

    public StartRound(MapInformation mapInformation, List<StartPosition> positions) {
        this.mapInformation = mapInformation;
        this.positions = positions;
    }

    public MapInformation getMapInformation() {
        return mapInformation;
    }

    public void setMapInformation(MapInformation mapInformation) {
        this.mapInformation = mapInformation;
    }

    public List<StartPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<StartPosition> positions) {
        this.positions = positions;
    }

    public StartRound() {
    }
}
