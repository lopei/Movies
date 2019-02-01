package com.anotap.whatagreatmovie.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    @SerializedName("main_star")
    private String mainStar;
    private String thumbnail;
    private List<Genre> gentres;
    @SerializedName("updated_at")
    private String updatedAt;
    private int year;
    private String director;
    private String name;
    private String description;
    @SerializedName("created_at")
    private String createdAt;
    private int id;
    private String url;
    private boolean listedFavorite;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Movie) {
            if (((Movie) obj).id == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return String.valueOf(id).hashCode();
    }

    public String getMainStar() {
        return mainStar;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public List<Genre> getGentres() {
        return gentres;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public boolean isListedFavorite() {
        return listedFavorite;
    }

    public void setListedFavorite(boolean listedFavorite) {
        this.listedFavorite = listedFavorite;
    }

    public class Genre implements Serializable {
        private String name;

        public String getName() {
            return name;
        }
    }
}
