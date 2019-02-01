package com.anotap.whatagreatmovie.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;
    private String username;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    private String url;
    private String error;

    public User(String username) {
        this.username = username;
    }

    public User() {
    }

    public User(String username, String error) {
        this.username = username;
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccessful() {
        return TextUtils.isEmpty(error);
    }
}
