package dev.basjansen.scribble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.DrawingService;

public class ViewDrawingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drawing);

        DrawingService drawingService = new DrawingService();

        TextView userNameTextView = findViewById(R.id.user_name);
        ImageView drawingImageView = findViewById(R.id.image_view);

        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null) {
            if (intent.getExtras().containsKey("drawing")) {
                Drawing drawing = (Drawing) intent.getExtras().get("drawing");
                setTitle(drawing.getName());
                userNameTextView.setText(drawing.getUser().getDisplayName());
                drawingService.downloadBitmap(drawing.getPath(), drawingImageView::setImageBitmap, Exception::printStackTrace);
            }
        }
    }
}