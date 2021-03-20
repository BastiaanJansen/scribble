package dev.basjansen.scribble;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        try {
//            DrawCanvasSyncServer server = new DrawCanvasSyncServer(9002);
//            new Thread(server).start();
//
//            DrawCanvasSyncClient client = new DrawCanvasSyncClient(new InetSocketAddress(server.getInetAddress(), server.getPort()));
//            new Thread(client).start();
//
//            DrawCanvasSyncClient client2 = new DrawCanvasSyncClient(new InetSocketAddress(server.getInetAddress(), server.getPort()));
//            new Thread(client2).start();
//
////            server.getClients().get(0).send("Hi");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        DrawingView drawingView = new DrawingView(this, new DrawingViewOnDrawListener());

        drawingView.setColor(Color.BLUE);

        setContentView(drawingView);
    }

}