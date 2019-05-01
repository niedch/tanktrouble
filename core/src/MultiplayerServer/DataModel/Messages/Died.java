package MultiplayerServer.DataModel.Messages;

import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingServer;

public class Died extends Message implements IWorkingServer {
    public Died() {
        // Dummy constructor
    }

    @Override
    public void workServer(Client serverChild) {
        serverChild.getDevConsole().println(serverChild.getPlayerName() + " died!");
        serverChild.setIsDead(true);
    }
}
