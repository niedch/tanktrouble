package MultiplayerServer.DevConsole.Commands.ErrorHandling;

import MultiplayerServer.DevConsole.Commands.Command;

public class MaxMapPlayerException extends ConsoleException {
    public MaxMapPlayerException(Command thrownBy, String commandString) {
        super(thrownBy, "Maximum amount of players are the minimum amount of players that can spawn on the smallest map", commandString);
    }
}
