package dev.basjansen.scribble.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import dev.basjansen.scribble.models.Drawing;

public class LocalDrawingService implements DrawingSaver {

    private Context context;

    public LocalDrawingService(Context context) {
        this.context = context;
    }

    @Override
    public void save(Bitmap bitmap, Drawing drawing) {

    }

    public void fetch(String documentPath, OnFetchSuccessListener<Drawing> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {

    }
}
