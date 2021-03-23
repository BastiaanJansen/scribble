package dev.basjansen.scribble.services;

import android.graphics.Bitmap;

import dev.basjansen.scribble.models.Drawing;

public interface DrawingSaver {
    void save(Bitmap bitmap, Drawing drawing);
}
