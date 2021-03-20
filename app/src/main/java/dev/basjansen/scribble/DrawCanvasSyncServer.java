package dev.basjansen.scribble;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DrawCanvasSyncServer implements Runnable {

    private final int port;
    private ServerSocket serverSocket;

    private ArrayList<ClientHandler> clients;

    public DrawCanvasSyncServer(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        Log.d(DrawCanvasSyncServer.class.getName(), "Socket server running on port " + port);
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
                clientHandler.start();

                Timer timer = new Timer();

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            clientHandler.send("Hello");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() {
        return serverSocket.getInetAddress();
    }
}
