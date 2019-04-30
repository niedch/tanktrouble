package MultiplayerServer.DevConsole.Commands;

public enum ECommand {
    HELP("help"),
    END_LOBBY("endLobby"),
    LIST_PLAYERS("listPlayers"),
    MAX_PLAYERS("maxplayers"),
    UPDATE_LOBBY("updateLobby"),
    KICK("kick");

    private final String commandString;

    ECommand(final String commandString) {
        this.commandString = commandString;
    }

    @Override
    public String toString() {
        return commandString;
    }

    public boolean startWith(String command) {
        return command.startsWith(commandString);
    }
}
