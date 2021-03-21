package dev.basjansen.scribble;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        DrawingViewOnDrawListener drawingViewOnDrawListener = new DrawingViewOnDrawListener(db);

        DrawingView drawingView = new DrawingView(this, drawingViewOnDrawListener);

        drawingViewOnDrawListener.setOnDrawListener(new OnDrawListener() {
            @Override
            public void onDraw(OnDrawEvent event) {
                drawingView.update(event);
            }
        });

        drawingView.setColor(Color.BLUE);

        setContentView(drawingView);
    }

}