package dev.basjansen.scribble.services;

import android.graphics.Bitmap;

import dev.basjansen.scribble.models.Drawing;

public class LocalDrawingServiceStrategy implements DrawingServiceStrategy {
    @Override
    public void save(Bitmap bitmap, String name) {

    }

    @Override
    public void fetch(OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {

    }

    @Override
    public void fetch(String documentPath, OnFetchSuccessListener<Drawing> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {

    }
}
