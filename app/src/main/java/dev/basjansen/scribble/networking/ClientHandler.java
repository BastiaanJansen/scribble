package dev.basjansen.scribble.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final SocketListener socketListener;

    private final PrintWriter out;
    private final BufferedReader in;

    public ClientHandler(Socket socket, SocketListener socketListener) throws IOException {
        this.socket = socket;
        this.socketListener = socketListener;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("[ClientHandler]: New socket connected: " + socket.getInetAddress().getHostAddress());
    }


    @Override
    public synchronized void run() {
        try {
            while (true) {
                socketListener.onRecieve(socket, in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Object data) throws IOException {
        System.out.println("[Server]: Sending data to: " + socket.getInetAddress().getHostAddress());
        out.println(data);
    }
}
