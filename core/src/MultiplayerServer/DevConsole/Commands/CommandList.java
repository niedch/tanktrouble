package MultiplayerServer.DevConsole.Commands;

import MultiplayerServer.DevConsole.Commands.CommandCollection.*;
import MultiplayerServer.DevConsole.DevConsolePresenter;

import java.util.ArrayList;

public class CommandList extends ArrayList<Command> {
    public CommandList(DevConsolePresenter presenter) {
        this.add(new HelpCommand(presenter));
        this.add(new EndLobbyCommand(presenter));
        this.add(new ListPlayersCommand(presenter));
        this.add(new MaxPlayersCommand(presenter));
        this.add(new UpdateLobbyCommand(presenter));

        // Keep last entriy
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

    public void help() {
        for (Command command: this) {
            if (!command.isDefault()) {
                command.help();
            }
        }
    }
}
