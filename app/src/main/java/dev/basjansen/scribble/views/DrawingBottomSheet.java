package dev.basjansen.scribble.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.basjansen.scribble.R;
import dev.basjansen.scribble.SettingsFragment;

public class DrawingBottomSheet extends Fragment {

    public static final String RECENTLY_USED_COLORS_KEY = "recently_used_colors";

    private final SharedPreferences preferences;
    private final Gson gson;

    private final DrawingView drawingView;
    private final ColorPicker colorPicker;
    private final List<Integer> recentlyUsedColors;

    private final boolean showRecentlyUsedColors;

    private Button redColorButton;
    private Button blueColorButton;
    private Button blackColorButton;
    private Button greenColorButton;
    private Button yellowColorButton;
    private Button eraseButton;
    private Button setStrokeWidthButtonSmall;
    private Button setStrokeWidthButtonMedium;
    private Button setStrokeWidthButtonLarge;
    private Button resetButton;
    private Button colorPickerButton;

    private LinearLayout customizedButtonLayout;

    public DrawingBottomSheet(Activity activity, DrawingView drawingView) {
        this.drawingView = drawingView;
        this.colorPicker = new ColorPicker(activity);
        this.colorPicker.enableAutoClose();
        this.gson = new GsonBuilder().create();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.recentlyUsedColors = retrieveRecentlyUsedColors();
        this.showRecentlyUsedColors = preferences.getBoolean(SettingsFragment.SHOW_RECENTLY_USED_COLORS_KEY, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawing_bottom_sheet, container, false);

        redColorButton = view.findViewById(R.id.red_color_button);
        blueColorButton = view.findViewById(R.id.blue_color_button);
        blackColorButton = view.findViewById(R.id.black_color_button);
        greenColorButton = view.findViewById(R.id.green_color_button);
        yellowColorButton = view.findViewById(R.id.yellow_color_button);
        eraseButton = view.findViewById(R.id.erase_button);
        setStrokeWidthButtonSmall = view.findViewById(R.id.adjust_width_button_1);
        setStrokeWidthButtonMedium = view.findViewById(R.id.adjust_width_button_2);
        setStrokeWidthButtonLarge = view.findViewById(R.id.adjust_width_button_3);
        resetButton = view.findViewById(R.id.reset_button);
        colorPickerButton = view.findViewById(R.id.color_picker_button);
        customizedButtonLayout = (LinearLayout) view.findViewById(R.id.button_row_0);

        setupButtonClickListeners();

        if (showRecentlyUsedColors)
            recentlyUsedColors.forEach(this::addRecentlyUsedColorToUI);

        return view;
    }

    private void setupButtonClickListeners() {
        redColorButton.setOnClickListener(v -> selectColor(Color.RED));
        blueColorButton.setOnClickListener(v -> selectColor(Color.BLUE));
        blackColorButton.setOnClickListener(v -> selectColor(Color.BLACK));
        greenColorButton.setOnClickListener(v -> selectColor(Color.GREEN));
        yellowColorButton.setOnClickListener(v -> selectColor(Color.YELLOW));
        eraseButton.setOnClickListener(v -> drawingView.setErase(true));

        setStrokeWidthButtonSmall.setOnClickListener(v -> drawingView.setStrokeWidth(5));
        setStrokeWidthButtonMedium.setOnClickListener(v -> drawingView.setStrokeWidth(15));
        setStrokeWidthButtonLarge.setOnClickListener(v -> drawingView.setStrokeWidth(30));
        resetButton.setOnClickListener(v -> drawingView.clear());

        colorPicker.setCallback(this::onColorPickerSelected);
        colorPickerButton.setOnClickListener(v -> {
            colorPicker.setColor(drawingView.getColor());
            colorPicker.show();
        });
    }

    private void onColorPickerSelected(int color) {
        selectColor(color);

        if (recentlyUsedColors.contains(color))
            return;

        if (recentlyUsedColors.size() >= 6) {
            recentlyUsedColors.remove(0);
            if (customizedButtonLayout.getChildCount() >= 5)
                customizedButtonLayout.removeViewAt(customizedButtonLayout.getChildCount() - 1);
        }

        if (showRecentlyUsedColors)
            addRecentlyUsedColorToUI(color);

        recentlyUsedColors.add(color);

        saveRecentlyUsedColors();
    }

    private void addRecentlyUsedColorToUI(int color) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(28, 0, 28, 0);

        @SuppressLint("InflateParams") Button button = (Button) getLayoutInflater().inflate(R.layout.color_button, null);
        button.setLayoutParams(layoutParams);
        button.setBackgroundColor(color);
        button.setOnClickListener(v -> selectColor(color));

        customizedButtonLayout.addView(button, 0);
    }

    private void saveRecentlyUsedColors() { ;
        String json = gson.toJson(recentlyUsedColors);
        System.out.println(json);
        preferences.edit().putString(RECENTLY_USED_COLORS_KEY, json).apply();
    }

    @SuppressWarnings("unchecked")
    private List<Integer> retrieveRecentlyUsedColors() {
        String json = preferences.getString(RECENTLY_USED_COLORS_KEY, "");
        List<Double> colors =  gson.fromJson(json, ArrayList.class);
        return colors == null ? new ArrayList<>() : colors.stream().map(Double::intValue).collect(Collectors.toList());
    }

    private void selectColor(int color) {
        drawingView.setColor(color);
        drawingView.setErase(false);
        setStrokeWidthButtonSmall.setBackgroundColor(color);
        setStrokeWidthButtonMedium.setBackgroundColor(color);
        setStrokeWidthButtonLarge.setBackgroundColor(color);
    }
}
