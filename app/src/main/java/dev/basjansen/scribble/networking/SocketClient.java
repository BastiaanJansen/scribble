package dev.basjansen.scribble.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient implements Runnable {

    private final InetSocketAddress address;
    private final SocketListener socketListener;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketClient(InetSocketAddress address, SocketListener socketListener) throws IOException {
        this.address = address;
        this.socketListener = socketListener;
    }

    @Override
    public synchronized void run() {
        try {
            System.out.println("[Client]: Connecting to server");
            socket = new Socket(address.getHostName(), address.getPort());

            System.out.println("[Client]: Connected to: " + socket.getInetAddress().getHostAddress());

            ClientHandler clientHandler = new ClientHandler(socket, socketListener);
            new Thread(clientHandler).start();

            clientHandler.send("received");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String data) throws IOException {
        out.println(data);
    }

    public void stop(String data) throws IOException {
        socket.close();
    }
}
