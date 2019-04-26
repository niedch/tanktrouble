package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;

public class ListPlayersCommand extends Command {
    public ListPlayersCommand(DevConsolePresenter presenter) {
        super(presenter, ECommand.LIST_PLAYERS);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.LIST_PLAYERS.startWith(command);
    }

    @Override
    protected void help() {
        this.printHelp("List all players that are connected to the Server");
    }

    @Override
    protected void execute(String command) {

    }
}
