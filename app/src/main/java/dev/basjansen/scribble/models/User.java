package dev.basjansen.scribble.models;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String displayName;

    public User() {}

    public User(String uid, String displayName) {
        this.uid = uid;
        this.displayName = displayName;
    }

    public static User fromFirebaseUser(FirebaseUser firebaseUser) {
        return new User(firebaseUser.getUid(), firebaseUser.getDisplayName());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
