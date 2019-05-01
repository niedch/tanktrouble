package MultiplayerServer.DataModel.Messages;

import MultiplayerClient.MultiplayerScreen;
import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.SubTypes.ScoreBoard;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingClient;
import com.fasterxml.jackson.annotation.JsonProperty;


public class EndRound extends Message implements IWorkingClient {
    @JsonProperty
    ScoreBoard scoreBoard;

    public EndRound(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public EndRound() {
        // Dummy constructor
    }

    @Override
    public void workClient(MultiplayerScreen screen) throws InterruptedException {
        screen.setScoreBoard(scoreBoard);
        screen.setStopThread(true);
        throw new InterruptedException();
    }
}
