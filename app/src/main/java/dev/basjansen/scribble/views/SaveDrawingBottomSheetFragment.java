package dev.basjansen.scribble.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import dev.basjansen.scribble.R;
import dev.basjansen.scribble.models.Drawing;
import dev.basjansen.scribble.models.User;
import dev.basjansen.scribble.services.DrawingService;

public class SaveDrawingBottomSheetFragment extends BottomSheetDialogFragment {

    private final Bitmap drawingBitmap;
    private final DrawingService drawingService;
    private final FirebaseAuth firebaseAuth;
    private final OnDrawingSavedListener onDrawingSavedListener;

    private EditText nameEditText;

    public SaveDrawingBottomSheetFragment(Bitmap bitmap, OnDrawingSavedListener onDrawingSavedListener) {
        this.drawingBitmap = bitmap;
        this.onDrawingSavedListener = onDrawingSavedListener;
        this.drawingService = new DrawingService();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_drawing_layout, container, false);

        nameEditText = view.findViewById(R.id.drawing_name_edit_text);

        Button saveButton = view.findViewById(R.id.save_drawing_popup_button);
        saveButton.setOnClickListener(this::onSaveButtonClick);

        return view;
    }

    public void onSaveButtonClick(View v) {
        String name = nameEditText.getText().toString();

        if (name.isEmpty()) {
            nameEditText.setError("Name cannot be empty");
            return;
        }

        String path = "images/" + new Date().getTime() + ".png";
        Drawing drawing = new Drawing(name, path, User.fromFirebaseUser(firebaseAuth.getCurrentUser()));
        drawingService.save(drawingBitmap, drawing);
        dismiss();

        onDrawingSavedListener.onSaved();
    }
}
