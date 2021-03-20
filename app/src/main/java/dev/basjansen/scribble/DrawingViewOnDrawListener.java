package dev.basjansen.scribble;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class DrawingViewOnDrawListener implements OnDrawListener {

    @Override
    public void onDraw(Bitmap bitmap) {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                String encoded = encode(bitmap);
            }
        });

        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String encode(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bitmapByteArray = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bitmapByteArray);
    }
}
