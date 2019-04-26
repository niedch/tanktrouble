package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;

public class MaxPlayersCommand extends Command {
    public MaxPlayersCommand(DevConsolePresenter presenter) {
        super(presenter, ECommand.MAX_PLAYERS);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.MAX_PLAYERS.startWith(command);
    }

    @Override
    public void help() {
        this.printHelp("Sets maximum amount of players is restricted to map size", "amount");
    }

    @Override
    protected void execute(String command) {
    }
}
