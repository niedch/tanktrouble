package MultiplayerServer.DataModel.Messages.WorkingInterfaces;

import MultiplayerClient.MultiplayerScreen;

public interface IWorkingClient {
    void workClient(MultiplayerScreen screen) throws InterruptedException;
}
