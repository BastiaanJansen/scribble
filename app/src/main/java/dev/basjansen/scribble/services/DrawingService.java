package dev.basjansen.scribble.services;

import android.graphics.Bitmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.FirebaseFirestore;

import dev.basjansen.scribble.models.Drawing;

public class DrawingService {

    public final static String COLLECTION_PATH = "drawings";

    private final DrawingServiceStrategy drawingServiceStrategy;
    private final FirebaseFirestore db;
    private final ObjectMapper objectMapper;

    public DrawingService(DrawingServiceStrategy drawingServiceStrategy) {
        this.drawingServiceStrategy = drawingServiceStrategy;
        this.db = FirebaseFirestore.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    public void save(Bitmap bitmap, String name) {
        this.drawingServiceStrategy.save(bitmap, name);
    }

    public void fetch(OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {
        this.drawingServiceStrategy.fetch(onFetchSuccessListener, onFetchFailureListener);
    }

    public void fetch(String documentPath, OnFetchSuccessListener<Drawing> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {
        this.drawingServiceStrategy.fetch(documentPath, onFetchSuccessListener, onFetchFailureListener);
    }

}
