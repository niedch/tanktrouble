package MultiplayerServer.DevConsole.Commands;


import MultiplayerServer.DevConsole.DevConsolePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class Command {
    public DevConsolePresenter presenter;

    public Command(DevConsolePresenter presenter) {
        this.presenter = presenter;
    }

    protected List<String> tokenize(String command){
        List<String> tokenized = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(command);

        while(tokenizer.hasMoreElements()) {
            tokenized.add(tokenizer.nextToken());
        }
        return tokenized;
    }

    protected abstract boolean isMatching(String command);

    protected abstract void help();

    protected abstract void execute(String command);

    protected boolean isDefault() {
        return false;
    }
}
