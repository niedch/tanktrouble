package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.Client;
import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.Commands.ErrorHandling.ConsoleException;
import MultiplayerServer.DevConsole.Commands.ErrorHandling.PlayerDoesNotExistException;
import MultiplayerServer.DevConsole.DevConsolePresenter;
import MultiplayerServer.MainServer;

import java.util.Map;

public class KickCommand extends Command {
    public KickCommand(DevConsolePresenter presenter) {
        super(presenter, ECommand.KICK);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.KICK.startWith(command);
    }

    @Override
    public void help() {
        this.printHelp("Kicks players out of the game", "player");
    }

    @Override
    protected void execute(String command) throws ConsoleException {
        String player = this.tokenize(command).get(1);

        for(Map.Entry<String, Client> entry : MainServer.clients.entrySet()){
            if(entry.getKey().equals(player)){
                entry.getValue().println(MainServer.createJSONObj("kick",null));
                return;
            }
        }

        throw new PlayerDoesNotExistException(this, command, player);
    }
}
