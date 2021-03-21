package dev.basjansen.scribble.services;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import dev.basjansen.scribble.models.Drawing;

public class DrawingService {

    private final static String COLLECTION_PATH = "drawings";

    private final DrawingSaveStrategy drawingSaveStrategy;
    private final FirebaseFirestore db;
    private final ObjectMapper objectMapper;

    public DrawingService(DrawingSaveStrategy drawingSaveStrategy) {
        this.drawingSaveStrategy = drawingSaveStrategy;
        this.db = FirebaseFirestore.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    public DrawingService() {
        this(new FirebaseDrawingSaveStrategy());
    }

    public void save(Bitmap bitmap, String name) {
        this.drawingSaveStrategy.save(bitmap, name);
    }

    public void fetch(OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFailureListener onFailureListener) {
        db.collection(COLLECTION_PATH).get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
            Drawing[] drawings = objectMapper.convertValue(queryDocumentSnapshots.getDocuments(), Drawing[].class);
            onFetchSuccessListener.onSuccess(drawings);
        }).addOnFailureListener(onFailureListener);
    }

    public void fetch(String documentPath, OnFetchSuccessListener<Drawing> onFetchSuccessListener, OnFailureListener onFailureListener) {
        db.collection(COLLECTION_PATH).document(documentPath).addSnapshotListener((DocumentSnapshot value, FirebaseFirestoreException error) -> {
            if (error != null)
                onFailureListener.onFailure(error);

            Map<String, Object> map = value.getData();
            Drawing drawing = objectMapper.convertValue(map, Drawing.class);
            onFetchSuccessListener.onSuccess(drawing);
        });
    }

}
