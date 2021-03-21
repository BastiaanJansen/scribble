package dev.basjansen.scribble.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class SocketServer implements Runnable {

    private final int port;
    private final ServerSocket serverSocket;
    private final SocketListener socketListener;

    private final List<ClientHandler> clients;
    private final Queue<Runnable> queue;

    private boolean isStopped = false;

    public SocketServer(int port, SocketListener socketListener) throws IOException {
        this.port = port;
        this.socketListener = socketListener;
        this.serverSocket = new ServerSocket(port);
        this.queue = new LinkedBlockingDeque<>();
        this.clients = new LinkedList<>();
        System.out.println("Socket server running on port " + port);
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, socketListener);
                new Thread(clientHandler).start();
                clients.add(clientHandler);

                clientHandler.send("sd");

                if (queue.size() > 0)
                    queue.poll().run();
            }

//            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void emit(Object data) {
        queue.add(new Runnable() {
            @Override
            public void run() {
                try {
                    for (ClientHandler clientHandler: clients)
                        clientHandler.send(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop() {
        isStopped = true;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() {
        return serverSocket.getInetAddress();
    }
}
