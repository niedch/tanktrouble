package MultiplayerServer;

import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.MessageUtils;
import MultiplayerServer.DataModel.Messages.SetPlayerName;
import MultiplayerServer.DataModel.Messages.WorkingInterfaces.IWorkingServer;
import MultiplayerServer.DevConsole.DevConsolePresenter;
import com.badlogic.gdx.Gdx;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread{
    private String playerName;

    private static String TAG;
    private boolean isDead = false;

    private BufferedReader reader;
    private PrintWriter writer;

    private Socket socket;
    private DevConsolePresenter devConsole;

    public Client(Socket socket, DevConsolePresenter devConsole){
        this.socket = socket;
        try {
            this.devConsole = devConsole;
            writer = new PrintWriter(socket.getOutputStream(),true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            SetPlayerName setPlayerName = (SetPlayerName) MessageUtils .deserialize(reader.readLine());

            this.setPlayerName(setPlayerName.getPlayerName());
            TAG = this.getPlayerName();
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                IWorkingServer message = (IWorkingServer) MessageUtils.deserialize(reader.readLine());
                message.workServer(this);
            }
        } catch (NullPointerException e) {
            System.err.println("Invalid message sent to child process");
        } catch (IOException e) {
            MainServer.clients.remove(playerName);
        } finally {
            MainServer.updateLobby();
            MainServer.devConsole.println(this.getPlayerName() + ": disconnected!");
        }
    }

    public void println(String str){
        writer.println(str);
        writer.flush();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public DevConsolePresenter getDevConsole() {
        return devConsole;
    }
}
