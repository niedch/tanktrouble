package MultiplayerServer.DevConsole.Commands;


import MultiplayerServer.DevConsole.DevConsolePresenter;

public abstract class Command {
    public DevConsolePresenter presenter;

    public Command(DevConsolePresenter presenter) {
        this.presenter = presenter;
    }

    protected abstract boolean isMatching(String command);

    protected abstract void help();

    protected abstract void execute(String command);

    protected boolean isDefault() {
        return false;
    }
}
