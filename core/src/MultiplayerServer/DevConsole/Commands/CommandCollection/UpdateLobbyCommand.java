package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;

public class UpdateLobbyCommand extends Command {
    public UpdateLobbyCommand(DevConsolePresenter presenter) {
        super(presenter);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.UPDATE_LOBBY.startWith(command);
    }

    @Override
    protected void help() {

    }

    @Override
    protected void execute(String command) {

    }
}
