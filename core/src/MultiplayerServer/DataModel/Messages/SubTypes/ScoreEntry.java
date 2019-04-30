package MultiplayerServer.DataModel.Messages.SubTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScoreEntry {
    @JsonProperty
    private String playerName;

    @JsonProperty
    private int scoreResult;

    public ScoreEntry(String playerName, int scoreResult) {
        this.playerName = playerName;
        this.scoreResult = scoreResult;
    }
}
