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
        setupDrawSheet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_drawing_menu, menu);

        MenuItem saveDrawingButton = menu.findItem(R.id.save_drawing_button);
        saveDrawingButton.setOnMenuItemClickListener(this::onDoneMenuItemClicked);

        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home)
            return super.onOptionsItemSelected(item);

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public boolean isEmptyBitmap(Bitmap bitmap) {
        Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        return emptyBitmap.sameAs(bitmap);
    }

    public boolean onDoneMenuItemClicked(MenuItem item) {
        if (isEmptyBitmap(drawingView.getCanvasBitmap())) {
            Toast.makeText(this, "An empty drawing cannot be saved", Toast.LENGTH_SHORT).show();
            return false;
        }

        SaveDrawingBottomSheetFragment drawingBottomSheet = new SaveDrawingBottomSheetFragment(drawingView.getCanvasBitmap(), this::finish);
        drawingBottomSheet.show(getSupportFragmentManager(), DrawingActivity.class.getName());

        return true;
    }

    public void setupDefaultDrawingSettings() {
        drawingView.setColor(Color.BLACK);
        drawingView.setStrokeWidth(15);
    }

    public void setupDrawSheet() {
        Button redColorButton = findViewById(R.id.red_color_button);
        Button blueColorButton = findViewById(R.id.blue_color_button);
        Button blackColorButton = findViewById(R.id.black_color_button);
        Button greenColorButton = findViewById(R.id.green_color_button);
        Button yellowColorButton = findViewById(R.id.yellow_color_button);
        Button eraseButton = findViewById(R.id.erase_button);
        Button setStrokeWidthButtonSmall = findViewById(R.id.adjust_width_button_1);
        Button setStrokeWidthButtonMedium = findViewById(R.id.adjust_width_button_2);
        Button setStrokeWidthButtonLarge = findViewById(R.id.adjust_width_button_3);
        Button resetButton = findViewById(R.id.reset_button);
        Button colorPickerButton = findViewById(R.id.color_picker_button);

        redColorButton.setOnClickListener((View v) -> onColorButtonClick(Color.RED));
        blueColorButton.setOnClickListener((View v) -> onColorButtonClick(Color.BLUE));
        blackColorButton.setOnClickListener((View v) -> onColorButtonClick(Color.BLACK));
        greenColorButton.setOnClickListener((View v) -> onColorButtonClick(Color.GREEN));
        yellowColorButton.setOnClickListener((View v) -> onColorButtonClick(Color.YELLOW));
        eraseButton.setOnClickListener((View v) -> drawingView.setErase(true));

        setStrokeWidthButtonSmall.setOnClickListener((View v) -> drawingView.setStrokeWidth(5));
        setStrokeWidthButtonMedium.setOnClickListener((View v) -> drawingView.setStrokeWidth(15));
        setStrokeWidthButtonLarge.setOnClickListener((View v) -> drawingView.setStrokeWidth(30));
        resetButton.setOnClickListener((View v) -> drawingView.clear());

        colorPickerButton.setOnClickListener((View v) -> {
            ColorPicker colorPicker = new ColorPicker(this);
            colorPicker.setColor(drawingView.getColor());
            colorPicker.show();
            colorPicker.enableAutoClose();

            colorPicker.setCallback(color -> drawingView.setColor(color));
        });
    }

    public void onColorButtonClick(int color) {
        drawingView.setColor(color);
        drawingView.setErase(false);
    }
}
