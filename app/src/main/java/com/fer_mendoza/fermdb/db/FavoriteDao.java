package com.fer_mendoza.fermdb.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT movieId FROM favorites")
    LiveData<List<Long>> findAllIds();

    @Query("SELECT * FROM favorites WHERE movieId = :id")
    Favorite findOne(long id);

    @Insert
    void insert(Favorite fav);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Favorite fav);

    @Delete
    void delete(Favorite fav);

}
