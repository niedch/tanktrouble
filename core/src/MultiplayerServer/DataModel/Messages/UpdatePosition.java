package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;
import MultiplayerServer.MainServer;

public class UpdatePosition extends Message {
    @Override
    public void workServer(Client serverChild) {
        MainServer.sendBroadcast(this.toString(), serverChild);
    }

    public UpdatePosition() {
        // Dummy Constructor
    }
}
