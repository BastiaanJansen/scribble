package dev.basjansen.scribble;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;
    private DataOutputStream out;
    private BufferedReader in;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("New socket connected: " + socket.getInetAddress().getHostName());
    }


    @Override
    public synchronized void start() {
        super.start();
    }

    public void send(String data) throws IOException {
        out.write(data.getBytes());
        out.flush();
    }
}
