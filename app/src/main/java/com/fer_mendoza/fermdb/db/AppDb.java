package com.fer_mendoza.fermdb.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {
    private static final String LOG_TAG = AppDb.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "fermdb";
    private static AppDb sIntance;

    public static AppDb getInstance(Context context){
        if(sIntance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new db instance");
                sIntance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDb.class, AppDb.DB_NAME).build();
            }
        }
        Log.d(LOG_TAG, "Getting db instance");
        return sIntance;
    }

    public abstract UserDao userDao();
}
