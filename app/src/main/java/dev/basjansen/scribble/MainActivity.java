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
import dev.basjansen.scribble.services.FirebaseDrawingService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Gallery");
        setupFAB();
        setupDrawingsList();
    }

    public void setupDrawingsList() {
        DrawingService drawingService = new FirebaseDrawingService();

        RecyclerView drawingsRecycleView = findViewById(R.id.drawings_reclycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        drawingsRecycleView.setLayoutManager(linearLayoutManager);
        DrawingsAdapter adapter = new DrawingsAdapter(this, new Drawing[]{});
        drawingsRecycleView.setAdapter(adapter);

        drawingService.fetch((Drawing[] drawings) -> {
            adapter.setDrawings(drawings);
            adapter.notifyDataSetChanged();
        }, Exception::printStackTrace);
    }

    public void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, DrawingActivity.class);
            startActivity(intent);
        });
    }
}