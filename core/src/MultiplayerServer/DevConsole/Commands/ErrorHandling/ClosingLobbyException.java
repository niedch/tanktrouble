package MultiplayerServer.DevConsole.Commands.ErrorHandling;

import MultiplayerServer.DevConsole.Commands.Command;

public class ClosingLobbyException extends ConsoleException {
    public ClosingLobbyException(Command thrownBy, String commandString) {
        super(thrownBy, "Something went wrong when closing lobby", commandString);
    }
}
