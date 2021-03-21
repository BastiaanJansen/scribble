package dev.basjansen.scribble;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        drawingView = findViewById(R.id.drawing_view);

        setupDrawButtons();

        drawingView.setColor(Color.BLUE);
        drawingView.setStrokeWidth(15);
    }

    public void setupDrawButtons() {
        Button redColorButton = findViewById(R.id.red_color_button);
        Button blueColorButton = findViewById(R.id.blue_color_button);
        Button blackColorButton = findViewById(R.id.black_color_button);

        redColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.RED));
        blueColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.BLUE));
        blackColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.BLACK));
    }

}