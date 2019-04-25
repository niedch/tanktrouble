package MultiplayerServer;

import MultiplayerServer.DevConsole.DevConsolePresenter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import MultiplayerServer.DevConsole.DevConsoleView;

import Utils.Constants;
import scenes.ScoreBoard;

public class MainServer {
    public static DevConsolePresenter devConsole;
    public static HashMap<String, Client> clients;
    public static ServerSocket serverSocket;
    public static final String TAG = "MainServer";
    public static int MAX_PLAYERS;
    public static int MAP_MAXPLAYER;


    public MainServer(){
        devConsole = new DevConsolePresenter();
        clients = new HashMap<String, Client>();

        MAP_MAXPLAYER = getMAXPlayers();
        MAX_PLAYERS = MAP_MAXPLAYER;
        devConsole.println("MAX_PLAYERS: " + MAX_PLAYERS);

        try {
            serverSocket = new ServerSocket(9999);

            while(true){
                if(clients.size() >= MAX_PLAYERS){
                    devConsole.println("MAX Players reached!");
                    serverSocket.close();
                }

                Socket socket = serverSocket.accept();
                Client client = new Client(socket, devConsole);
                if(!clients.containsKey(client.getPlayerName())){
                    clients.put(client.getPlayerName(),client);
                    devConsole.println(client.getPlayerName() + ": connected!");
                    client.println(createJSONObj("ok",null));
                    updateLobby();
                }else
                    client.println(createJSONObj("nok",null));
            }
        } catch (BindException e) {
            devConsole.println("Server already started!");
        } catch (IOException e) {

        }

        devConsole.println("Game started!");
        Gdx.app.log(TAG, "Game started!");

        sendBroadcast(createJSONObj("GameStart", null));
        ScoreBoard scoreBoard = new ScoreBoard();

        for(Map.Entry<String, Client> entry : clients.entrySet()){
            scoreBoard.addRow(entry.getKey(),0);
        }

        boolean isEndLobby = false;
        int i = 0;

        while(true) {
            String s = createJSONObj("startRound", getStartPos(getRandomMap()));
            sendBroadcast(s);

            while(!isEndLobby) {
                for (Map.Entry<String, Client> entry : clients.entrySet()) {
                    if (!entry.getValue().isDead()) i++;
                }

                if (i == 1) {
                    isEndLobby = true;
                    try {
                        Thread.sleep(Constants.General.DELAY_TIME*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (Map.Entry<String, Client> entry : clients.entrySet()) {
                        if (!entry.getValue().isDead()) scoreBoard.addWin(entry.getKey());
                    }

                    devConsole.println("Round ended!");
                    sendBroadcast(createJSONObj("endRound", scoreBoard.toJSONArray()));
                }
                i = 0;
            }

            try {
                Thread.sleep(3000);
                sendBroadcast(createJSONObj("endShowStats", null));
                devConsole.println("End Score Screen send!");
                for (Map.Entry<String, Client> entry : clients.entrySet()) {
                    entry.getValue().setIsDead(false);
                }
                isEndLobby = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject getStartPos(File file){

        List<RectangleMapObject> spawnPoints = new ArrayList<RectangleMapObject>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s;

            while ((s = reader.readLine()) != null) {
                if (s.startsWith(" <objectgroup name=\"TankSpawnPoints\">")) {
                    while ((s = reader.readLine()).startsWith("  <object ")) {
                        spawnPoints.add(parseToRectangle(s));
                    }
                }
            }
        } catch (Exception e){
            return null;
        }

        JSONArray arr = new JSONArray();

        for(Map.Entry<String, Client> client : clients.entrySet()){
            int i = ThreadLocalRandom.current().nextInt(0,spawnPoints.size());
            RectangleMapObject rectangleMapObject = spawnPoints.get(i);
            JSONObject obj = new JSONObject();
                obj.put("name",client.getKey());
                obj.put("posX", rectangleMapObject.getRectangle().getX());
                obj.put("posY", rectangleMapObject.getRectangle().getY());
                obj.put("width", rectangleMapObject.getRectangle().getWidth());
                obj.put("height", rectangleMapObject.getRectangle().getHeight());
            arr.put(obj);
        }
        JSONObject obj = new JSONObject();
            obj.put("map",Constants.General.MAP_PATH+file.getName());
            obj.put("startPos", arr);
        return obj;
    }

    private static RectangleMapObject parseToRectangle(String s){
        int x,y,width,height;

        x = Integer.parseInt(s.substring(s.indexOf("x=\"")+3,s.indexOf("\"",s.indexOf("x=\"")+3)));
        y = Integer.parseInt(s.substring(s.indexOf("y=\"")+3,s.indexOf("\"",s.indexOf("y=\"")+3)));
        width = Integer.parseInt(s.substring(s.indexOf("width=\"")+7,s.indexOf("\"",s.indexOf("width=\"")+7)));
        height = Integer.parseInt(s.substring(s.indexOf("height=\"")+8,s.indexOf("\"",s.indexOf("height=\"")+8)));

        return new RectangleMapObject(x,y,width,height);
    }

    public static void updateLobby(){
        JSONArray arr = new JSONArray();
        for(Map.Entry<String, Client> entry : clients.entrySet()){
            arr.put(entry.getKey());
        }

        for(Map.Entry<String, Client> entry : clients.entrySet()){
            entry.getValue().println(createJSONObj("updateLobby",arr));
        }
    }

    public static void sendBroadcast(String obj){
        for(Map.Entry<String, Client> entry : clients.entrySet()){
            entry.getValue().println(obj.toString());
        }
    }

    public static void sendBroadcast(String obj, Client without){
        for(Map.Entry<String, Client> entry : clients.entrySet()){
            if(!entry.getValue().equals(without)){
                entry.getValue().println(obj.toString());
            }
        }
    }

    public static String createJSONObj(String type, Object data){
        JSONObject obj = new JSONObject();
        obj.put("type", type);
        if(data != null){
            obj.put("data", data);
        }
        return obj.toString();
    }


    private int getMAXPlayers() {
        int i = 1;
        int tmpMax = 100;
        File currentFile = new File(Constants.General.MAP_PATH+"map"+i+".tmx");

        while(currentFile.exists()){
            if(tmpMax > getMaxSpawnPoints(currentFile)){
                tmpMax = getMaxSpawnPoints(currentFile);
            }

            i++;
            currentFile = new File(Constants.General.MAP_PATH+"map"+i+".tmx");;
        }

        return tmpMax;
    }

    private int getMaxSpawnPoints(File file){
        try {
            int i = 0;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s;
            while((s = reader.readLine())!= null){
                if(s.startsWith(" <objectgroup name=\"TankSpawnPoints\">")){
                    while((s = reader.readLine()).startsWith("  <object ")) i++;
                    return i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 100;
    }

    private File getRandomMap(){
        int i = 200;
        while(!Gdx.files.internal(Constants.General.MAP_PATH+"map"+i+".tmx").exists()){
            i = ThreadLocalRandom.current().nextInt(0, 200);
        }

        return new File(Constants.General.MAP_PATH+"map"+i+".tmx");
    }
}
