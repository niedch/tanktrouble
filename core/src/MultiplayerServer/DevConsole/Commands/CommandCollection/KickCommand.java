package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;

public class KickCommand extends Command {
    public KickCommand(DevConsolePresenter presenter) {
        super(presenter, ECommand.KICK);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.KICK.startWith(command);
    }

    @Override
    public void help() {
        this.printHelp("Kicks players out of the game", "player");
    }

    @Override
    protected void execute(String command) {

    }
}
