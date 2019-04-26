package MultiplayerServer.DevConsole.Commands;

import MultiplayerServer.DevConsole.Commands.CommandCollection.*;
import MultiplayerServer.DevConsole.DevConsolePresenter;

import java.util.ArrayList;

public class CommandList extends ArrayList<Command> {
    private Command defaultCommand;

    public CommandList(DevConsolePresenter presenter) {
        this.add(new HelpCommand(presenter));
        this.add(new EndLobbyCommand(presenter));
        this.add(new ListPlayersCommand(presenter));
        this.add(new MaxPlayersCommand(presenter));
        this.add(new UpdateLobbyCommand(presenter));
        this.add(new KickCommand(presenter));

        this.defaultCommand = new DefaultCommand(presenter);
    }

    public void start(String commandString){
        for (Command command : this) {
            if (!command.isMatching(commandString)) {
                continue;
            }

            command.execute(commandString);
            return;
        }

        this.defaultCommand.execute(commandString);
    }

    public void help() {
        for (Command command: this) {
            if (!command.isDefault()) {
                command.help();
            }
        }
    }
}
