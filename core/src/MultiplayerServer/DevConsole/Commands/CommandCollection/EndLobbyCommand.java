package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;

public class EndLobbyCommand extends Command {
    public EndLobbyCommand(DevConsolePresenter presenter) {
        super(presenter);
    }

    @Override
    protected boolean isMatching(String command) {
        return command.startsWith(ECommand.END_LOBBY.toString());
    }

    @Override
    protected void help() {
        this.presenter.println("Helps by showing help for all commands");
    }

    @Override
    protected void execute(String command) {
        this.presenter.println("ENDLOBBY");
    }
}
