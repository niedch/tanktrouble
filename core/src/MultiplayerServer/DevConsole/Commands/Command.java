package MultiplayerServer.DevConsole.Commands;


import MultiplayerServer.DevConsole.Commands.ErrorHandling.ConsoleException;
import MultiplayerServer.DevConsole.DevConsolePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class Command {
    protected DevConsolePresenter presenter;
    protected ECommand command;

    public Command(DevConsolePresenter presenter, ECommand command) {
        this.presenter = presenter;
        this.command = command;
    }

    protected List<String> tokenize(String command){
        List<String> tokenized = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(command);

        while(tokenizer.hasMoreElements()) {
            tokenized.add(tokenizer.nextToken());
        }
        return tokenized;
    }

    protected void printHelp(String helpText, String ...parameters) {
        if(parameters.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String parameter: parameters) {
                stringBuilder.append("<"+parameter+">");
            }

            this.presenter.println(command.toString() + " " + stringBuilder.toString() + " : "+ helpText);
            return;
        }


        this.presenter.println(command.toString() + " : " + helpText);
    }

    protected abstract boolean isMatching(String command);

    public abstract void help();

    protected abstract void execute(String command) throws ConsoleException;

    protected boolean isDefault() {
        return false;
    }

    @Override
    public String toString() {
        return command.toString();
    }
}
