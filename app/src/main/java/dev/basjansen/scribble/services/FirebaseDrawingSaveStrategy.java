package dev.basjansen.scribble.services;

import android.graphics.Bitmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Map;

import dev.basjansen.scribble.models.Drawing;

public class FirebaseDrawingSaveStrategy implements DrawingSaveStrategy {

    private final FirebaseFirestore db;
    private final FirebaseStorage firebaseStorage;
    private final StorageReference storageReference;
    private final ObjectMapper objectMapper;

    public FirebaseDrawingSaveStrategy() {
        this.db = FirebaseFirestore.getInstance();
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = firebaseStorage.getReference();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(Bitmap bitmap, String name) {
        uploadBitmap(bitmap, String.valueOf(new Date().getTime()), (UploadTask.TaskSnapshot taskSnapshot) -> {
            String path = taskSnapshot.getMetadata().getPath();
            Drawing drawing = new Drawing(name, path);
            db.collection("drawings").add(objectMapper.convertValue(drawing, Map.class));
        }, Throwable::printStackTrace);
    }

    private void uploadBitmap(Bitmap bitmap, String name, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        StorageReference drawingRef = storageReference.child("images/" + name + ".png");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = drawingRef.putBytes(data);
        uploadTask.addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
}
