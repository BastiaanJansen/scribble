package dev.basjansen.scribble;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.services.FirebaseDrawingService;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Gallery");
        setupFAB();
        setupDrawingsList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setupDrawingsList() {
        FirebaseDrawingService drawingSaver = new FirebaseDrawingService();

        RecyclerView drawingsRecycleView = findViewById(R.id.drawings_reclycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        drawingsRecycleView.setLayoutManager(linearLayoutManager);
        DrawingsAdapter adapter = new DrawingsAdapter(this, new Drawing[]{});
        drawingsRecycleView.setAdapter(adapter);

        drawingSaver.fetch((Drawing[] drawings) -> {
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