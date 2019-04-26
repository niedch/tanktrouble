package MultiplayerServer.DevConsole.Commands.ErrorHandling;

import MultiplayerServer.DevConsole.Commands.Command;

public class ConsoleException extends Exception {
    public Command thrownBy;
    private String errorMessage;
    public String commandString;


    public ConsoleException(Command thrownBy, String errorMessage, String commandString) {
        this.thrownBy = thrownBy;
        this.errorMessage = errorMessage;
        this.commandString = commandString;
    }

    public void printCommandHelp() {
        this.thrownBy.help();
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
