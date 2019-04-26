package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;

public class EndLobbyCommand extends Command {
    public EndLobbyCommand(DevConsolePresenter presenter) {
        super(presenter, ECommand.END_LOBBY);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.END_LOBBY.startWith(command);
    }

    @Override
    public void help() {
        this.printHelp("Ends the lobby and starts the game");
    }

    @Override
    protected void execute(String command) {
        this.presenter.println("ENDLOBBY");
    }
}
