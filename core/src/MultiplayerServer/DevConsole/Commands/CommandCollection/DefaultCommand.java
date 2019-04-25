package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.DevConsolePresenter;

public class DefaultCommand extends Command {
    public DefaultCommand(DevConsolePresenter presenter) {
        super(presenter);
    }

    @Override
    protected boolean isMatching(String command) {
        return true;
    }

    @Override
    protected void help() {

    }

    @Override
    protected void execute(String command) {
        this.presenter.println("Does not match any command \""+command+"\"");
    }

    @Override
    protected boolean isDefault() {
        return true;
    }
}
