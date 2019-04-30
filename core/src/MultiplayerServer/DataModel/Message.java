package MultiplayerServer.DataModel;

import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Messages.*;
import MultiplayerServer.DataModel.Messages.Disconnect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Died.class, name = "died"),
        @JsonSubTypes.Type(value = Disconnect.class, name = "disconnect"),
        @JsonSubTypes.Type(value = EndRound.class, name = "endRound"),
        @JsonSubTypes.Type(value = GameStart.class, name = "gameStart"),
        @JsonSubTypes.Type(value= SetPlayerName.class, name = "setPlayerName"),
        @JsonSubTypes.Type(value = Shot.class, name = "shot"),
        @JsonSubTypes.Type(value = StartRound.class, name = "startRound"),
        @JsonSubTypes.Type(value = UpdateBullet.class, name = "updateBullet"),
        @JsonSubTypes.Type(value = UpdateLobby.class, name = "updateLobby"),
        @JsonSubTypes.Type(value = UpdatePosition.class, name = "updatePosition"),
})
public abstract class Message {
    public void workServer(Client serverChild) {
        // Empty implementation
    };

    public Message() {
        // Dummy constructor
    }

    @Override
    public String toString() {
        return MessageUtils.serialize(this);
    }
}
