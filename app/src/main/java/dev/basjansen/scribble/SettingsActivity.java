package dev.basjansen.scribble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHOW_RECENTLY_USED_COLORS_KEY = "show_recently_used_colors";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.settings);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.settings:
                    return true;
                case R.id.gallery:
                    startActivity(new Intent(getApplicationContext(), GalleryActivity.class));
                    overridePendingTransition( 0, 0);
                    return true;
                case R.id.mydrawings:
                    startActivity(new Intent(getApplicationContext(), ViewDrawingActivity.class));
                    overridePendingTransition( 0, 0);
                    return true;
            }
            return false;
        });

        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content, new SettingsFragment()).commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
        }

    }
}