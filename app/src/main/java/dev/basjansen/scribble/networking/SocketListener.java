package dev.basjansen.scribble.networking;

import java.net.Socket;

public interface SocketListener {
    void onRecieve(Socket socket, String line);
}
