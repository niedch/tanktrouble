package MultiplayerServer.DevConsole;

import MultiplayerServer.Client;
import MultiplayerServer.IView;
import MultiplayerServer.MainServer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class DevConsoleView extends JFrame implements IView {
    private JTextArea textArea;
    private JTextField textField;
    private JButton btn;
    private DevConsolePresenter presenter;

    public DevConsoleView(final DevConsolePresenter presenter){
        super("Server Console");
        this.presenter = presenter;

        initJFrame();

        textArea = new JTextArea();
        textArea.setEditable(false);
        this.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.submitCommand(textField.getText());
            }
        });

        panel.add(textField, BorderLayout.CENTER);
        btn = new JButton("Submit");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.submitCommand(textField.getText());
            }
        });
        panel.add(btn,BorderLayout.EAST);

        this.add(panel,BorderLayout.SOUTH);
        this.setVisible(true);
        printIP();
    }


    private void initJFrame() {
        this.setLocationRelativeTo(null);
        this.setSize(300, 600);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void handleCommand(String command){
            if(command.startsWith("help")){
                printHelp();
            }else if(command.startsWith("endLobby")){
                try {
                    MainServer.serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(command.startsWith("listPlayers")){
                printConnectedPlayers();
            }else if(command.startsWith("kick")){
                kickPlayer(command.substring(command.indexOf(' ')+1));
            }else if(command.startsWith("maxplayers")) {
                maxplayers(command.substring(command.indexOf(' ') + 1));
            }else if(command.startsWith("updateLobby")){
                MainServer.updateLobby();
            }else{
                println("Unknown Command");
            }
    }

    private void kickPlayer(String player){
        for(Map.Entry<String, Client> entry : MainServer.clients.entrySet()){
            if(entry.getKey().equals(player)){
                entry.getValue().println(MainServer.createJSONObj("kick",null));
                return;
            }
        }
        println("\""+player+"\" did not exist on this Server");
    }

    private void printIP(){

        try {
            println("IP: " + Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void maxplayers(String maxPlayers){
        try {
            if (MainServer.MAP_MAXPLAYER <= Integer.parseInt(maxPlayers)) {
                this.println("New Value cannot be greater than the original on");
            }else{
                MainServer.MAX_PLAYERS = Integer.parseInt(maxPlayers);
            }
        } catch (NumberFormatException e){
            this.println("Parameter is not a Number!");
        }
    }

    public void println(String s){
        textArea.append(s+"\n");
    }

    private void printHelp(){
        println("help - Tells you where you can find all Commands");
        println("endLobby - Clients are no longer able to connect!");
        println("updateLobby - Update the Lobby on all Clients !");
        println("listPlayers - Shows all active Players");
        println("kick <player> - Kicks player from the Server");
        println("maxplayers <Amount> - Sets the max amount of Players");
    }

    private void printConnectedPlayers(){
        for(HashMap.Entry<String, Client> entry : MainServer.clients.entrySet()){
            println("\t" + entry.getKey());
        }
    }

    @Override
    public void updateView() {
        textArea.setText(presenter.convertToText());
        textField.setText(presenter.getTextFieldValue());
    }
}
