package MultiplayerServer.DevConsole;

import java.util.ArrayList;
import java.util.List;

public class DevConsoleModel {
    private String textFieldtext = "test";
    private List<String> prints = new ArrayList<String>();

    public String getTextFieldtext() {
        return textFieldtext;
    }

    public void setTextFieldtext(String textFieldtext) {
        this.textFieldtext = textFieldtext;
    }

    public void addItem(String text) {
        prints.add(text);
    }

    public List<String> getPrints() {
        return prints;
    }

    public void setPrints(List<String> prints) {
        this.prints = prints;
    }
}
