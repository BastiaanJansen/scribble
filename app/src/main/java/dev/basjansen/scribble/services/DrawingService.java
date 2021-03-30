package dev.basjansen.scribble.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import dev.basjansen.scribble.models.Drawing;

public class DrawingService {

    private final static String COLLECTION_PATH = "drawings";

    private final FirebaseFirestore db;
    private final StorageReference storageReference;
    private final ObjectMapper objectMapper;

    public DrawingService() {
        this.db = FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = firebaseStorage.getReference();
        this.objectMapper = new ObjectMapper();
    }

    @SuppressWarnings("unchecked")
    public void save(Bitmap bitmap, Drawing drawing) {
        uploadBitmap(bitmap, drawing.getPath(), (UploadTask.TaskSnapshot taskSnapshot) -> {
            Map<String, Object> map = objectMapper.convertValue(drawing, Map.class);
            db.collection(COLLECTION_PATH).add(map);
        }, Throwable::printStackTrace);
    }

    public void fetch(OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {
            db.collection(COLLECTION_PATH).orderBy("createdAt", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> onFetchSuccessListener.onSuccess(documentsToDrawings(queryDocumentSnapshots)))
                    .addOnFailureListener(onFetchFailureListener::onFailure);
    }

    public void fetch(String documentPath, OnFetchSuccessListener<Drawing> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {
        Query query = db.collection(COLLECTION_PATH);
        query.getFirestore().document(documentPath).addSnapshotListener((DocumentSnapshot value, FirebaseFirestoreException error) -> {
            if (error != null || value == null) {
                onFetchFailureListener.onFailure(error);
                return;
            }

            Map<String, Object> map = value.getData();
            Drawing drawing = objectMapper.convertValue(map, Drawing.class);
            onFetchSuccessListener.onSuccess(drawing);
        });
    }

    public void fetchDrawingsOfUser(String uid, OnFetchSuccessListener<Drawing[]> onFetchSuccessListener, OnFetchFailureListener onFetchFailureListener) {
        db.collection(COLLECTION_PATH).whereEqualTo("user.uid", uid).get()
                .addOnSuccessListener(queryDocumentSnapshots -> onFetchSuccessListener.onSuccess(documentsToDrawings(queryDocumentSnapshots)))
                .addOnFailureListener(onFetchFailureListener::onFailure);
    }

    public void downloadBitmap(String documentPath, OnFetchSuccessListener<Bitmap> onFetchSuccessListener, OnFailureListener onFailureListener) {
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference imageRef = storageReference.child(documentPath);

        imageRef.getBytes(ONE_MEGABYTE * 5).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            onFetchSuccessListener.onSuccess(bitmap);
        }).addOnFailureListener(onFailureListener);
    }

    private Drawing[] documentsToDrawings(QuerySnapshot queryDocumentSnapshots) {
        return queryDocumentSnapshots
                .getDocuments()
                .stream()
                .map((DocumentSnapshot document) -> objectMapper.convertValue(document.getData(), Drawing.class))
                .toArray(Drawing[]::new);
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
