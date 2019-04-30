package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SetPlayerName extends Message {
    @JsonProperty
    private String playerName;

    public SetPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public SetPlayerName() {
        // Dummy constructor
    }
}
