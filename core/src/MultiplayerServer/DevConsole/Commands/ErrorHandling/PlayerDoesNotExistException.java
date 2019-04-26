package MultiplayerServer.DevConsole.Commands.ErrorHandling;

import MultiplayerServer.DevConsole.Commands.Command;

public class PlayerDoesNotExistException extends ConsoleException {
    public PlayerDoesNotExistException(Command thrownBy, String commandString, String player) {
        super(thrownBy, "Player: "+ player +" does not exist on the Server", commandString);
    }
}
