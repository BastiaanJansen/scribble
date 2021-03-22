package dev.basjansen.scribble;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import dev.basjansen.scribble.services.DrawingService;
import dev.basjansen.scribble.services.FirebaseDrawingServiceStrategy;

public class DrawingActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        db = FirebaseFirestore.getInstance();

        drawingView = findViewById(R.id.drawing_view);

        setupDefaultDrawingSettings();
        setupDrawButtons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home)
            return super.onOptionsItemSelected(item);

        saveDrawing();
        return super.onOptionsItemSelected(item);
    }

    public void saveDrawing() {
        DrawingService drawingService = new DrawingService(new FirebaseDrawingServiceStrategy());
        drawingService.save(drawingView.getCanvasBitmap(), "My drawing");
    }

    public void setupDefaultDrawingSettings() {
        drawingView.setColor(Color.BLACK);
        drawingView.setStrokeWidth(15);
    }

    public void setupDrawButtons() {
        Button redColorButton = findViewById(R.id.red_color_button);
        Button blueColorButton = findViewById(R.id.blue_color_button);
        Button blackColorButton = findViewById(R.id.black_color_button);
        Button greenColorButton = findViewById(R.id.green_color_button);
        Button yellowColorButton = findViewById(R.id.yellow_color_button);

        redColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.RED));
        blueColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.BLUE));
        blackColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.BLACK));
        greenColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.GREEN));
        yellowColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.YELLOW));
    }
}
