package MultiplayerServer.DataModel.Messages;

import MultiplayerClient.MultiplayerScreen;
import MultiplayerServer.Client;
import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.SubTypes.TankBullets;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingClient;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingServer;
import MultiplayerServer.MainServer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateBullet extends Message implements IWorkingServer, IWorkingClient {
    @JsonProperty
    TankBullets tankBullets;

    public UpdateBullet(TankBullets tankBullets) {
        this.tankBullets = tankBullets;
    }

    @Override
    public void workServer(Client serverChild) {
        MainServer.sendBroadcast(this.toString(), serverChild);
    }

    public UpdateBullet() {
        // Dummy constructor
    }

    @Override
    public void workClient(MultiplayerScreen screen) {
        screen.setBulletData(this.tankBullets);
    }
}
