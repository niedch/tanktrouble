package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.SubTypes.ScoreEntry;
import com.fasterxml.jackson.annotation.JsonProperty;
import scenes.ScoreBoard;

import java.util.List;

public class EndRound extends Message {
    @JsonProperty
    List<ScoreEntry> score;

    public EndRound(List<ScoreEntry> score) {
        this.score = score;
    }
}
