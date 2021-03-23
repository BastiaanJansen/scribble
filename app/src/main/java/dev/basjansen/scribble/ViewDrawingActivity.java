package dev.basjansen.scribble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.FirebaseDrawingService;

public class ViewDrawingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drawing);

        FirebaseDrawingService drawingService = new FirebaseDrawingService();

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