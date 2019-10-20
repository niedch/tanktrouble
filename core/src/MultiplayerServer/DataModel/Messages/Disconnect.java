package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingServer;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Disconnect extends Message implements IWorkingServer {
    @JsonProperty
    private String playerName;

    public Disconnect(String playerName, List<String> testField) {
        this.playerName = playerName;
    }

    @Override
    public void workServer(Client serverChild) {
        serverChild.getDevConsole().println(playerName + ": disconnected!");
    }

    public Disconnect() {
        // Dummy constructor
    }
}
