package MultiplayerServer.DataModel.Messages;

import MultiplayerClient.MultiplayerScreen;
import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.SubTypes.TankPosition;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingClient;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingServer;
import MultiplayerServer.MainServer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdatePosition extends Message implements IWorkingServer, IWorkingClient {
    @JsonProperty
    TankPosition tankPosition;

    public UpdatePosition(TankPosition tankPosition) {
        this.tankPosition = tankPosition;
    }

    public UpdatePosition() {
        // Dummy constructor
    }

    @Override
    public void workServer(Client serverChild) {
        MainServer.sendBroadcast(this.toString(), serverChild);
    }

    @Override
    public void workClient(MultiplayerScreen screen) {
        if (!screen.getWorld().isLocked()) {
            screen.setUpdateTank(this.tankPosition);
        }
    }
}
