package dev.basjansen.scribble;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.DrawingService;


public class GalleryActivity extends AppCompatActivity {

    private DrawingService drawingService;
    private DrawingsAdapter drawingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTitle("Gallery");

        drawingService = new DrawingService();
        drawingsAdapter = new DrawingsAdapter(this);

        setupFAB();
        setupDrawingsList();
    }

    public void setupDrawingsList() {
        RecyclerView drawingsRecycleView = findViewById(R.id.drawings_reclycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        drawingsRecycleView.setLayoutManager(linearLayoutManager);
        drawingsRecycleView.setAdapter(drawingsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        drawingService.fetch((Drawing[] drawings) -> {
            drawingsAdapter.setDrawings(drawings);
            drawingsAdapter.notifyDataSetChanged();
        }, Exception::printStackTrace);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, DrawingActivity.class);
            startActivity(intent);
        });
    }
}