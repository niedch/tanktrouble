package MultiplayerServer.DevConsole.Commands.ErrorHandling;

import MultiplayerServer.DevConsole.Commands.Command;

public class NotANumberException extends ConsoleException {
    public NotANumberException(Command thrownBy, String commandString) {
        super(thrownBy, "Parameter needs to be a number", commandString);
    }
}
