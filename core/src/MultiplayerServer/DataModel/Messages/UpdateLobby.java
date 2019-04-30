package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.DataModel.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateLobby extends Message {
    @JsonProperty
    private List<String> players;

    public UpdateLobby() {
        // Dummy
    }

    public UpdateLobby(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
