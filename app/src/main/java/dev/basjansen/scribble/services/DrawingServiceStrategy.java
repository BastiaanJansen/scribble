package dev.basjansen.scribble.services;

import android.graphics.Bitmap;

import dev.basjansen.scribble.models.Drawing;

public interface DrawingServiceStrategy {
    void save(Bitmap bitmap, String name);
    void fetch(OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener);
    void fetch(String documentPath, OnFetchSuccessListener<Drawing> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener);
}
