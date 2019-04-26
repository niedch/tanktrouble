package MultiplayerServer.DevConsole;

import MultiplayerServer.DevConsole.Commands.CommandList;
import MultiplayerServer.DevConsole.Commands.ErrorHandling.ConsoleException;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class DevConsolePresenter {
    private DevConsoleModel devConsoleModel;
    private DevConsoleView devConsoleView;
    private CommandList commandsList;
    private StringBuilder stringBuilder = new StringBuilder();

    public DevConsolePresenter() {
        devConsoleView = new DevConsoleView(this);
        devConsoleModel = new DevConsoleModel();
        commandsList = new CommandList(this);

        printIP();
    }

    void submitCommand(String command) {
        try {
            println(command);
            commandsList.start(command);
            clearTextField();
        } catch (ConsoleException e) {
            e.printCommandHelp();
            setTextFieldText(e.commandString);
            devConsoleView.showError(e.getErrorMessage());
        }
    }

    public void printHelp() {
        commandsList.help();
    }

    private void clearTextField() {
        setTextFieldText("");
    }

    private void setTextFieldText(String text) {
        devConsoleModel.setTextFieldtext(text);
        devConsoleView.updateView();
    }

    public void println(String text) {
        devConsoleModel.addItem(text);
        devConsoleView.updateView();
    }

    String convertToText() {
        clearStringBuilder();

        for (String s : devConsoleModel.getPrints()) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private void printIP(){
        try {
            println("IP: " + Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    String getTextFieldValue() {
        return devConsoleModel.getTextFieldtext();
    }

    private void clearStringBuilder() {
        stringBuilder.setLength(0);
    }
}
