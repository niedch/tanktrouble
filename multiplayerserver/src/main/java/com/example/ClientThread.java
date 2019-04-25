package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import sun.applet.Main;

public class ClientThread extends Thread{

    private PrintWriter writer;
    private BufferedReader reader;
    private boolean sendFile = false;

    private String playerName;
    private float posX = 0f;
    private float posY = 0f;
    private float angle = 0f;

    public ClientThread(Socket socket){
        try {
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mess = reader.readLine();
            if(playerName == null && mess.startsWith("name:")){
                playerName = mess.substring(mess.indexOf(':')+1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                String mess = reader.readLine();
                if(mess.startsWith("map:"))handleMap(mess.substring(mess.indexOf(':')+1));
            }

        } catch (NullPointerException e) {
            System.err.println(playerName +": Connection closed!");
            MainClass.playersThread.remove(playerName);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void send(String mess){
        writer.println(mess);
    }

    //Map Protocol map: sendFile:
    public void handleMap(String mess){
        if(Boolean.parseBoolean(mess.substring(0,mess.indexOf(':')))){

        }
    }

    //Update Protocoll update:playerName:PosX:PosY:Angle
    public void update(String mess){
        int prePos = 0, postPos = mess.indexOf(':');
        playerName = mess.substring(prePos,postPos);

        prePos = postPos+1;
        postPos = mess.indexOf(':',prePos);
        posX = Float.parseFloat(mess.substring(prePos, postPos));

        prePos = postPos+1;
        postPos = mess.indexOf(':',prePos);
        posY = Float.parseFloat(mess.substring(prePos, postPos));

        prePos = postPos+1;
        postPos = mess.indexOf(':',prePos);
        angle = Float.parseFloat(mess.substring(prePos, postPos));

        System.out.println(playerName+":"+posX+":"+posY+":"+angle);
    }
}
