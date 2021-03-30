package dev.basjansen.scribble;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<Integer, Fragment> pagesMap = new HashMap<>();
        pagesMap.put(R.id.gallery, new GalleryFragment());
        pagesMap.put(R.id.my_drawings_reclycler_view, new MyDrawingsFragment());
        pagesMap.put(R.id.settings, new SettingsFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment newFragment = pagesMap.get(item.getItemId());

            if (newFragment == null)
                return false;

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, newFragment).commit();
            return true;
        });

        setupFAB();
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, DrawingActivity.class);
            startActivity(intent);
        });
    }

}