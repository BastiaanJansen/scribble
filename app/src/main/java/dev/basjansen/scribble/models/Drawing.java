package dev.basjansen.scribble.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Drawing implements Serializable  {
    private String name;
    private String path;

    public Drawing() { }

    public Drawing(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
