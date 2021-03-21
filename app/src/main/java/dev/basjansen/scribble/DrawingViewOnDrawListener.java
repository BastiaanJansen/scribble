package dev.basjansen.scribble;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class DrawingViewOnDrawListener implements OnDrawListener, EventListener<DocumentSnapshot> {

    private final FirebaseFirestore db;
    private final ObjectMapper objectMapper;
    private OnDrawListener onDrawListener;

    public DrawingViewOnDrawListener(FirebaseFirestore db) {
        this.db = db;
        this.objectMapper = new ObjectMapper();

        db.collection("drawings").document("1").addSnapshotListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDraw(OnDrawEvent event) {
        Map<String, Object> map = objectMapper.convertValue(event, Map.class);
        System.out.println(map);
        db.collection("drawings").document("1")
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Success");
                    }
                });
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
        Map<String, Object> map = value.getData();
        OnDrawEvent onDrawEvent = objectMapper.convertValue(map, OnDrawEvent.class);
        System.out.println("UPDATE");
        if (onDrawListener != null)
            onDrawListener.onDraw(onDrawEvent);
    }

    public void setOnDrawListener(OnDrawListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }
}
