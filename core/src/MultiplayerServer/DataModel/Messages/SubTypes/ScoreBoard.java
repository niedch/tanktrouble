package MultiplayerServer.DataModel.Messages.SubTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScoreBoard {
    @JsonProperty
    List<ScoreEntry> scoreEntries;

    public void initPlayers(Collection<String> players) {
        this.scoreEntries = new ArrayList<>();

        for(String player: players) {
            scoreEntries.add(new ScoreEntry(player, 0));
        }
    }

    public void addWin(String player) {
        for(ScoreEntry scoreEntry: scoreEntries) {
            if (scoreEntry.getPlayerName().equals(player)) {
                scoreEntry.setScoreResult(scoreEntry.getScoreResult() + 1);
            }
        }
    }

    public void addRow(String playerName, int initalScore) {
        scoreEntries.add(new ScoreEntry(playerName, initalScore));
    }

    public boolean contains(String player) {
        for(ScoreEntry scoreEntry: scoreEntries) {
            if (scoreEntry.getPlayerName().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public ScoreBoard() {
        // Dummy constructor
    }

    public List<ScoreEntry> getScoreEntries() {
        return scoreEntries;
    }

    public void setScoreEntries(List<ScoreEntry> scoreEntries) {
        this.scoreEntries = scoreEntries;
    }
}
