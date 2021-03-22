package dev.basjansen.scribble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.DrawingService;
import dev.basjansen.scribble.services.FirebaseDrawingService;
import dev.basjansen.scribble.services.OnFetchSuccessListener;

public class ViewDrawingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_drawing);

        FirebaseDrawingService drawingService = new FirebaseDrawingService();

        ImageView imageView = new ImageView(this);

        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null) {
            if (intent.getExtras().containsKey("drawing")) {
                Drawing drawing = (Drawing) intent.getExtras().get("drawing");
                setTitle(drawing.getName());

                drawingService.downloadBitmap(drawing.getPath(), imageView::setImageBitmap, Exception::printStackTrace);
            }
        }

        setContentView(imageView);
    }
}