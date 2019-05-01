package MultiplayerServer.DataModel.Messages;

import MultiplayerClient.MultiplayerScreen;
import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.SubTypes.NewShot;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingClient;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingServer;
import MultiplayerServer.MainServer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Shot extends Message implements IWorkingServer, IWorkingClient {
    @JsonProperty
    private NewShot whoShot;

    public void workServer(Client serverChild) {
        MainServer.sendBroadcast(this.toString(), serverChild);
    }

    public Shot(NewShot whoShot) {
        this.whoShot = whoShot;
    }

    public Shot() {
        // Dummy constructor
    }

    @Override
    public void workClient(MultiplayerScreen screen) {
        screen.setWhoShot(this.whoShot);
    }
}
