package dev.basjansen.scribble.services;

import android.graphics.Bitmap;

public interface DrawingSaveStrategy {
    void save(Bitmap bitmap, String name);
}
