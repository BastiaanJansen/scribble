package dev.basjansen.scribble.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import dev.basjansen.scribble.models.Drawing;

public class LocalDrawingService implements DrawingService {

    private Context context;

    public LocalDrawingService(Context context) {
        this.context = context;
    }

    @Override
    public void save(Bitmap bitmap, String name) {
        File dir = new File(context.getFilesDir(), "drawings");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream out = null;
            Integer counter = 0;
            File file = new File(path, name + ".png");
            out = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush(); // Not really required
            out.close(); // do not forget to close the stream

        } catch (IOException e) {
            e.printStackTrace();
        }

//        MediaStore.Images.Media.insertImage(context.getContentResolver(), )
    }

    @Override
    public void fetch(OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {

    }

    @Override
    public void fetch(String documentPath, OnFetchSuccessListener<Drawing> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {

    }
}
