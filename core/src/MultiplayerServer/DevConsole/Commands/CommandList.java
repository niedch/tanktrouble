package MultiplayerServer.DevConsole.Commands;

import MultiplayerServer.DevConsole.Commands.CommandCollection.DefaultCommand;
import MultiplayerServer.DevConsole.Commands.CommandCollection.EndLobbyCommand;
import MultiplayerServer.DevConsole.Commands.CommandCollection.HelpCommand;
import MultiplayerServer.DevConsole.DevConsolePresenter;

import java.util.ArrayList;

public class CommandList extends ArrayList<Command> {
    public CommandList(DevConsolePresenter presenter) {
        this.add(new HelpCommand(presenter));
        this.add(new EndLobbyCommand(presenter));
        this.add(new DefaultCommand(presenter));
    }

    public void start(String commandString){
        for (Command command : this) {
            if (!command.isMatching(commandString)) {
                continue;
            }

            command.execute(commandString);
        }
    }
}
