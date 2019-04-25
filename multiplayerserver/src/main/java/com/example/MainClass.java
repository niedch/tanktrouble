package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainClass implements Runnable{
    ServerSocket serverSocket;
    Thread lobbyThread;

    private boolean isInLobby = true;

    public static HashMap<String, Thread> playersThread;

    public MainClass(){
        new Thread(this).start();

        PlayMap map = new PlayMap();
        playersThread = new HashMap<String,Thread>();

        lobbyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(9999);

                    Socket socket = null;
                    while(isInLobby){
                        socket = serverSocket.accept();

                        if(socket != null){
                            System.out.println("Socket connected");
                            ClientThread thread = new ClientThread(socket);
                            playersThread.put(thread.getPlayerName(), thread);
                        }
                    }
                } catch (IOException e) {
                }
            }
        });
        lobbyThread.start();

        try {
            lobbyThread.join();
        } catch (InterruptedException e) {
        }


        System.out.println("Game started!");

        for(Map.Entry<String,Thread> entry : playersThread.entrySet()){
            entry.getValue().start();
        }
    }

    public static void main(String[] args) {
        new MainClass();
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for(;;){
            try {
                String mess = br.readLine();
                if(mess.startsWith("help")){
                    System.out.println("Befehle: ");
                    System.out.println("\thelp : Zeig alle Befehle");
                    System.out.println("\tendLobby : Beendet warten auf Spieler!");
                    System.out.println("\tlistPlayers : Zeigt alle Spieler die zurzeit verbunden sind!");
                    System.out.println("\tkick <playerName> : Kicket den Spieler vom Server!");
                }else if(mess.startsWith("endLobby")) {
                    serverSocket.close();
                }else if(mess.startsWith("listPlayers")) {
                    if (playersThread.isEmpty()) System.out.println("\tKeine Spieler gefunden!");
                    else for (Map.Entry<String, Thread> entry : playersThread.entrySet()) {
                        System.out.println("\t" + entry.getKey());
                    }
                }else if(mess.startsWith("kick ")){
                    if(playersThread.containsKey(mess.substring(mess.indexOf(' ')+1))){
                        playersThread.remove(mess.substring(mess.indexOf(' ')+1));
                    }else{
                        System.out.println("Player: \""+mess.substring(mess.indexOf(' ')+1)+"\" ist nicht am Server!");
                    }
                }else{
                    System.out.println("Unbekannter Befehl type\"help\"");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
