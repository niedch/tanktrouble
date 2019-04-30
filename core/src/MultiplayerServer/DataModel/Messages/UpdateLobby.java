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
}
