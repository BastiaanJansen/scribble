package dev.basjansen.scribble.models;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public class Drawing implements Serializable  {
    private String name;
    private String path;
    private User user;
    private Date createdAt;

    public Drawing() { }

    public Drawing(String name, String path, User user, Date createdAt) {
        this.name = name;
        this.path = path;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Drawing(String name, String path, User user) {
        this(name, path, user, new Date());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
