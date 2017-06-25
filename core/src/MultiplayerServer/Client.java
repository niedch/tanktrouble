package MultiplayerServer;

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
    private DevConsole devConsole;

    public Client(Socket socket, DevConsole devConsole){
        this.socket = socket;
        try {
            this.devConsole = devConsole;
            writer = new PrintWriter(socket.getOutputStream(),true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.setPlayerName(new JSONObject(reader.readLine()).getString("name"));
            TAG = this.getPlayerName();
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            for(;;){
                JSONObject result = new JSONObject(reader.readLine());
                if(result.getString("type").equals("disconnect")){
                    Gdx.app.log(TAG, this.getPlayerName() + ": disconnected!");
                }else if(result.getString("type").equals("updatePos")){
                    MainServer.sendBroadcast(result.toString(),this);
                }else if(result.getString("type").equals("shot")){
                    MainServer.sendBroadcast(result.toString(),this);
                }else if(result.getString("type").equals("died")){
                    devConsole.println(playerName+": died!");
                    this.isDead = true;
                }else if(result.getString("type").equals("updateBullet")){
                    MainServer.sendBroadcast(result.toString(),this);
                }
            }
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
}
