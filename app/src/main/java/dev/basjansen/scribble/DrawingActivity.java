package dev.basjansen.scribble;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.DrawingService;
import dev.basjansen.scribble.services.FirebaseDrawingService;

public class DrawingActivity extends AppCompatActivity {
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingView = findViewById(R.id.drawing_view);

        setupDefaultDrawingSettings();
        setupDrawButtons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home)
            return super.onOptionsItemSelected(item);

        saveDrawing(new FirebaseDrawingService(), "My drawing");
        return super.onOptionsItemSelected(item);
    }

    public void saveDrawing(DrawingService drawingService, String name) {
        drawingService.save(drawingView.getCanvasBitmap(), name);
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
        Button setStrokeWidthButtonSmall = findViewById(R.id.adjust_width_button_1);
        Button setStrokeWidthButtonMedium = findViewById(R.id.adjust_width_button_2);
        Button setStrokeWidthButtonLarge = findViewById(R.id.adjust_width_button_3);

        redColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.RED));
        blueColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.BLUE));
        blackColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.BLACK));
        greenColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.GREEN));
        yellowColorButton.setOnClickListener((View v) -> drawingView.setColor(Color.YELLOW));

        setStrokeWidthButtonSmall.setOnClickListener((View v) -> drawingView.setStrokeWidth(5));
        setStrokeWidthButtonMedium.setOnClickListener((View v) -> drawingView.setStrokeWidth(15));
        setStrokeWidthButtonLarge.setOnClickListener((View v) -> drawingView.setStrokeWidth(30));
    }
}
