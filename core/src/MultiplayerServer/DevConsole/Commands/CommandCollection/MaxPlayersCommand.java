package MultiplayerServer.DevConsole.Commands.CommandCollection;

import MultiplayerServer.DevConsole.Commands.Command;
import MultiplayerServer.DevConsole.Commands.ECommand;
import MultiplayerServer.DevConsole.Commands.ErrorHandling.ConsoleException;
import MultiplayerServer.DevConsole.Commands.ErrorHandling.MaxMapPlayerException;
import MultiplayerServer.DevConsole.Commands.ErrorHandling.NotANumberException;
import MultiplayerServer.DevConsole.DevConsolePresenter;
import MultiplayerServer.MainServer;

public class MaxPlayersCommand extends Command {
    public MaxPlayersCommand(DevConsolePresenter presenter) {
        super(presenter, ECommand.MAX_PLAYERS);
    }

    @Override
    protected boolean isMatching(String command) {
        return ECommand.MAX_PLAYERS.startWith(command);
    }

    @Override
    public void help() {
        this.printHelp("Sets maximum amount of players is restricted to map size", "amount");
    }

    @Override
    protected void execute(String command) throws ConsoleException {
        try {
            // Parses first parameter to integer
            int maxPlayers = Integer.parseInt(this.tokenize(command).get(1));

            if (MainServer.MAP_MAXPLAYER <= maxPlayers) {
                throw new MaxMapPlayerException(this, command);
            }

            MainServer.MAX_PLAYERS = maxPlayers;
        } catch (NumberFormatException e){
            throw new NotANumberException(this, command);
        }
    }
}
