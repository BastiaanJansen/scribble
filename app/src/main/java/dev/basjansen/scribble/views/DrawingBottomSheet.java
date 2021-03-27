package dev.basjansen.scribble.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.ArrayList;
import java.util.List;

import dev.basjansen.scribble.R;

public class DrawingBottomSheet extends Fragment {

    private final Context context;
    private final DrawingView drawingView;
    private final ColorPicker colorPicker;
    private final List<Integer> recentlyUsedColors;

    private View view;

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
        this.context = activity;
        this.drawingView = drawingView;
        this.recentlyUsedColors = new ArrayList<>();
        this.colorPicker = new ColorPicker(activity);
        this.colorPicker.enableAutoClose();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.drawing_bottom_sheet, container, false);

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

        setupButtonClickListeners();

        customizedButtonLayout = (LinearLayout) view.findViewById(R.id.button_row_0);

        return view;
    }

    public void setupButtonClickListeners() {
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

    public void onColorPickerSelected(int color) {
        selectColor(color);

        if (recentlyUsedColors.contains(color))
            return;
        addRecentlyUsedColor(color);
    }

    public void addRecentlyUsedColor(int color) {
        if (recentlyUsedColors.size() >= 6) {
            recentlyUsedColors.remove(0);
            customizedButtonLayout.removeViewAt(5);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        Button button = (Button) getLayoutInflater().inflate(R.layout.color_button, null);
        button.setLayoutParams(layoutParams);
        button.setBackgroundColor(color);
        button.setOnClickListener(v -> selectColor(color));

        Space space = new Space(view.getContext());
        customizedButtonLayout.addView(space);

        customizedButtonLayout.addView(button, 0);
        recentlyUsedColors.add(color);
    }

    public void selectColor(int color) {
        drawingView.setColor(color);
        drawingView.setErase(false);
        setStrokeWidthButtonSmall.setBackgroundColor(color);
        setStrokeWidthButtonMedium.setBackgroundColor(color);
        setStrokeWidthButtonLarge.setBackgroundColor(color);
    }
}
