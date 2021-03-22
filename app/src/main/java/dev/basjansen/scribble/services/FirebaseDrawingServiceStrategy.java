package dev.basjansen.scribble.services;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.basjansen.scribble.models.Drawing;

public class FirebaseDrawingServiceStrategy implements DrawingServiceStrategy {

    private final FirebaseFirestore db;
    private final FirebaseStorage firebaseStorage;
    private final StorageReference storageReference;
    private final ObjectMapper objectMapper;

    public FirebaseDrawingServiceStrategy() {
        this.db = FirebaseFirestore.getInstance();
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = firebaseStorage.getReference();
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public void save(Bitmap bitmap, String name) {
        uploadBitmap(bitmap, String.valueOf(new Date().getTime()), (UploadTask.TaskSnapshot taskSnapshot) -> {
            String path = taskSnapshot.getMetadata().getPath();
            Drawing drawing = new Drawing(name, path);
            db.collection(DrawingService.COLLECTION_PATH).add(objectMapper.convertValue(drawing, Map.class));
        }, Throwable::printStackTrace);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void fetch(OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {
        db.collection(DrawingService.COLLECTION_PATH).get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
            Drawing[] drawings = queryDocumentSnapshots
                    .getDocuments()
                    .stream()
                    .map((DocumentSnapshot document) -> objectMapper.convertValue(document.getData(), Drawing.class))
                    .toArray(Drawing[]::new);
            onFetchSuccessListener.onSuccess(drawings);
        }).addOnFailureListener(onFetchFailureListener::onFailure);
    }

    @Override
    public void fetch(String documentPath, OnFetchSuccessListener<Drawing> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {
        db.collection(DrawingService.COLLECTION_PATH).document(documentPath).addSnapshotListener((DocumentSnapshot value, FirebaseFirestoreException error) -> {
            if (error != null || value == null)
                onFetchFailureListener.onFailure(error);

            Map<String, Object> map = value.getData();
            Drawing drawing = objectMapper.convertValue(map, Drawing.class);
            onFetchSuccessListener.onSuccess(drawing);
        });
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
