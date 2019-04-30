package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;
import MultiplayerServer.MainServer;

public class UpdateBullet extends Message {
    @Override
    public void workServer(Client serverChild) {
        MainServer.sendBroadcast(this.toString(), serverChild);
    }

    public UpdateBullet() {
        // Dummy constructor
    }
}
