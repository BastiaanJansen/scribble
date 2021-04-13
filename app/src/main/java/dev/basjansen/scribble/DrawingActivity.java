package dev.basjansen.scribble;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import dev.basjansen.scribble.views.DrawingBottomSheet;
import dev.basjansen.scribble.views.DrawingView;
import dev.basjansen.scribble.views.SaveDrawingBottomSheetFragment;

public class DrawingActivity extends AppCompatActivity {
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingView = findViewById(R.id.drawing_view);

        setupDefaultDrawingSettings();

        DrawingBottomSheet drawingBottomSheet = new DrawingBottomSheet(this, drawingView);
        getSupportFragmentManager().beginTransaction().add(R.id.drawing_layout, drawingBottomSheet, DrawingActivity.class.getName()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_drawing_menu, menu);

        MenuItem saveDrawingButton = menu.findItem(R.id.save_drawing_button);
        saveDrawingButton.setOnMenuItemClickListener(this::onDoneMenuItemClicked);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home)
            return super.onOptionsItemSelected(item);

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private boolean isEmptyBitmap(Bitmap bitmap) {
        Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        return emptyBitmap.sameAs(bitmap);
    }

    private boolean onDoneMenuItemClicked(MenuItem item) {
        if (isEmptyBitmap(drawingView.getCanvasBitmap())) {
            Toast.makeText(this, "An empty drawing cannot be saved", Toast.LENGTH_SHORT).show();
            return false;
        }

        SaveDrawingBottomSheetFragment drawingBottomSheet = new SaveDrawingBottomSheetFragment(drawingView.getCanvasBitmap(), this::finish);
        drawingBottomSheet.show(getSupportFragmentManager(), DrawingActivity.class.getName());

        return true;
    }

    private void setupDefaultDrawingSettings() {
        drawingView.setColor(Color.BLACK);
        drawingView.setStrokeWidth(15);
    }
}
