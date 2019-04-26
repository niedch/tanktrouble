package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;

public class HelpCommand extends Command {
    public HelpCommand(DevConsolePresenter presenter) {
        super(presenter, ECommand.HELP);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.HELP.startWith(command);
    }

    @Override
    public void help() {
        this.presenter.println("Helps by showing help for all commands");
    }

    @Override
    protected void execute(String command) {
        this.presenter.printHelp();
    }
}
