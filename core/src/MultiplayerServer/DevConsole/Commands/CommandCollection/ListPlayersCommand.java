package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.Client;
import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;
import MultiplayerServer.MainServer;

import java.util.HashMap;

public class ListPlayersCommand extends Command {
    public ListPlayersCommand(DevConsolePresenter presenter) {
        super(presenter, ECommand.LIST_PLAYERS);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.LIST_PLAYERS.startWith(command);
    }

    @Override
    public void help() {
        this.printHelp("List all players that are connected to the Server");
    }

    @Override
    protected void execute(String command) {
        for(HashMap.Entry<String, Client> entry : MainServer.clients.entrySet()){
            presenter.println("\t" + entry.getKey());
        }
    }
}
