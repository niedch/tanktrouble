package MultiplayerServer;

import MultiplayerServer.DataModel.Message;
import MultiplayerServer.DataModel.Messages.*;
import MultiplayerServer.DataModel.Messages.SubTypes.MapInformation;
import MultiplayerServer.DataModel.Messages.SubTypes.ScoreBoard;
import MultiplayerServer.DataModel.Messages.SubTypes.StartPosition;
import MultiplayerServer.DevConsole.DevConsolePresenter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

import com.badlogic.gdx.math.Rectangle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import Utils.Constants;

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

                    Message message = new ConnectedOk();
                    devConsole.println(client.getPlayerName() + ": connected!");
                    client.println(message.toString());

                    updateLobby();
                }else {
                    Message message = new ConnectedNotOk();
                    client.println(message.toString());
                }
            }
        } catch (BindException e) {
            devConsole.println("Server already started!");
        } catch (IOException e) {

        }

        devConsole.println("Game started!");

        Message message = new GameStart();
        sendBroadcast(message.toString());
        ScoreBoard scoreBoard = new ScoreBoard();

        scoreBoard.initPlayers(clients.keySet());

        boolean isEndLobby = false;
        int i = 0;

        while(true) {
            sendBroadcast(String.valueOf(getStartPos(getRandomMap())));

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
                    Message endMessage = new EndRound(scoreBoard);
                    sendBroadcast(endMessage.toString());
                }
                i = 0;
            }

            try {
                Thread.sleep(3000);
                Message endGame = new EndGame();
                sendBroadcast(endGame.toString());
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

    public static StartRound getStartPos(File file){

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

        List<StartPosition> startPositions = new ArrayList<>();
        MapInformation map = new MapInformation();

        JSONArray arr = new JSONArray();

        for(Map.Entry<String, Client> client : clients.entrySet()){
            System.out.println("create spawnpoints: "+spawnPoints.size());
            int i = ThreadLocalRandom.current().nextInt(0,spawnPoints.size());
            System.out.println(i);

            StartPosition startPosition = new StartPosition();
                Rectangle rectangle = spawnPoints.get(i).getRectangle();
                startPosition.setPlayerName(client.getKey());
                startPosition.setRectangle(rectangle);
            startPositions.add(startPosition);
        }

        map.setMap(Constants.General.MAP_PATH+file.getName());

        return new StartRound(map, startPositions);
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
        UpdateLobby updateLobby = new UpdateLobby(new ArrayList<>());
        for(Map.Entry<String, Client> entry : clients.entrySet()){
            updateLobby.getPlayers().add(entry.getKey());
        }

        for(Map.Entry<String, Client> entry : clients.entrySet()){
            entry.getValue().println(updateLobby.toString());
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
