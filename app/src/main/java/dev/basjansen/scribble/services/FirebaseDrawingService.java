package dev.basjansen.scribble.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dev.basjansen.scribble.models.Drawing;

public class FirebaseDrawingService implements DrawingService {

    private final static String COLLECTION_PATH = "drawings";

    private final FirebaseFirestore db;
    private final FirebaseStorage firebaseStorage;
    private final StorageReference storageReference;
    private final ObjectMapper objectMapper;

    public FirebaseDrawingService() {
        this.db = FirebaseFirestore.getInstance();
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = firebaseStorage.getReference();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(Bitmap bitmap, String name) {
        String location = "images/" + new Date().getTime() + ".png";
        uploadBitmap(bitmap, location, (UploadTask.TaskSnapshot taskSnapshot) -> {
            String path = taskSnapshot.getMetadata().getPath();
            Drawing drawing = new Drawing(name, path);
            db.collection(COLLECTION_PATH).add(objectMapper.convertValue(drawing, Map.class));
        }, Throwable::printStackTrace);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void fetch(OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {
        db.collection(COLLECTION_PATH).get().addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
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
        db.collection(COLLECTION_PATH).document(documentPath).addSnapshotListener((DocumentSnapshot value, FirebaseFirestoreException error) -> {
            if (error != null || value == null) {
                onFetchFailureListener.onFailure(error);
                return;
            }

            Map<String, Object> map = value.getData();
            Drawing drawing = objectMapper.convertValue(map, Drawing.class);
            onFetchSuccessListener.onSuccess(drawing);
        });
    }

    public void downloadBitmap(String documentPath, OnFetchSuccessListener<Bitmap> onFetchSuccessListener, OnFailureListener onFailureListener) {
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference imageRef = storageReference.child(documentPath);

        imageRef.getBytes(ONE_MEGABYTE * 5).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            onFetchSuccessListener.onSuccess(bitmap);
        }).addOnFailureListener(onFailureListener);
    }

    private void uploadBitmap(Bitmap bitmap, String path, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        StorageReference drawingRef = storageReference.child(path);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = drawingRef.putBytes(data);
        uploadTask.addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
}
