package com.example.bing.heartratemonitor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import database.data.Location;
import database.data.UserInfo;
import database.data.UserStatus;

/**
 * Created by bing on 12/1/14.
 */
public class TCPConnection {

    private Socket socket;
    private ObjectOutputStream output;

    private final String IP = "192.168.1.115";
    private final int port = 55667;

    public TCPConnection() throws IOException {

        socket = new Socket(IP,port);
        output = new ObjectOutputStream(socket.getOutputStream());


    }
    public void send(double latitude,double longitude,int heartRate) throws IOException {

        output.writeObject(new UserStatus(
                new UserInfo(
                90001,"Bing","Li"),
                new Location(longitude,latitude),
                heartRate)
        );
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
