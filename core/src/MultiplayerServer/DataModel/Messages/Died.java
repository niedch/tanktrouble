package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;

public class Died extends Message {
    @Override
    public void workServer(Client serverChild) {
        serverChild.getDevConsole().println(serverChild.getPlayerName() + "died!");
        serverChild.setIsDead(true);
    }

    public Died() {
        // Dummy constructor
    }
}
