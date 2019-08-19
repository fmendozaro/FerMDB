package com.fer_mendoza.fermdb.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorite {
    @PrimaryKey
    private long movieId;

    public Favorite(long movieId) {
        this.movieId = movieId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }
}
