package dev.basjansen.scribble;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class DrawCanvasSyncClient implements Runnable {

    private final InetSocketAddress address;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public DrawCanvasSyncClient(InetSocketAddress address) throws IOException {
        this.address = address;
    }

    @Override
    public synchronized void run() {
        try {
            socket = new Socket(address.getHostName(), address.getPort());

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String data) throws IOException {
        out.write(data);
        out.flush();
    }

    public void stop(String data) throws IOException {
        socket.close();
    }
}
