package dev.basjansen.scribble.services.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import dev.basjansen.scribble.models.User;

public class UserService {

    public static final String COLLECTION_PATH = "users";

    private final FirebaseFirestore db;
    private final ObjectMapper objectMapper;

    public UserService() {
        this.db = FirebaseFirestore.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    public void save(User user) {
        db.collection(COLLECTION_PATH).document(user.getUid()).set(objectMapper.convertValue(user, Map.class));
    }

}
