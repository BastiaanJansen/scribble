package dev.basjansen.scribble;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.gallery);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.mydrawings:
                        startActivity(new Intent(getApplicationContext(), ViewDrawingActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.gallery:
                        return true;
                }
                return false;
            }
        });
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