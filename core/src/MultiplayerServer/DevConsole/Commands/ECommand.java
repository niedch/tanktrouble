package MultiplayerServer.DevConsole.Commands;

public enum ECommand {
    HELP("help"),
    END_LOBBY("endLobby");

    private final String commandString;
    ECommand(final String commandString) {
        this.commandString = commandString;
    }

    @Override
    public String toString() {
        return commandString;
    }
}
