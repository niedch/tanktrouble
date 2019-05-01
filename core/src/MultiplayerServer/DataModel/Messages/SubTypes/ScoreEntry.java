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

    public ScoreEntry() {
        // Dummy constructor
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScoreResult() {
        return scoreResult;
    }

    public void setScoreResult(int scoreResult) {
        this.scoreResult = scoreResult;
    }
}
