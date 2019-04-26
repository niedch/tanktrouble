package MultiplayerServer.DevConsole;

import MultiplayerServer.DevConsole.Commands.CommandList;

public class DevConsolePresenter {
    private DevConsoleModel devConsoleModel;
    private DevConsoleView devConsoleView;
    private CommandList commandsList;
    private StringBuilder stringBuilder = new StringBuilder();

    public DevConsolePresenter() {
        devConsoleView = new DevConsoleView(this);
        devConsoleModel = new DevConsoleModel();
        commandsList = new CommandList(this);
    }

    public void submitCommand(String command) {
        println(command);
        commandsList.start(command);
        clearTextField();
    }

    public void printHelp() {
        commandsList.help();
    }

    public void clearTextField() {
        setTextFieldText("");
    }

    public void setTextFieldText(String text) {
        devConsoleModel.setTextFieldtext(text);
    }

    public void println(String text) {
        devConsoleModel.addItem(text);
        devConsoleView.updateView();
    }

    public String convertToText() {
        clearStringBuilder();

        for (String s : devConsoleModel.getPrints()) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    String getTextFieldValue() {
        return devConsoleModel.getTextFieldtext();
    }

    public DevConsoleModel getDevConsoleModel() {
        return devConsoleModel;
    }

    public DevConsoleView getDevConsoleView() {
        return devConsoleView;
    }

    private void clearStringBuilder() {
        stringBuilder.setLength(0);
    }
}
