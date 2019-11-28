package com.group18.sustainucd.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * This class is the main access point for the android room database that contains all the bins.
 */
@Database(entities = {Bin.class}, version = 1)
public abstract class BinsDatabase extends RoomDatabase {
    private static BinsDatabase binsDatabase;   //Single database instance for all activities
    private static final String DB_NAME = "bins_database";

    public static synchronized BinsDatabase getInstance(final Context context) {
        if (binsDatabase == null) { //If there's no instance of the database
            //Build the database and get an instance of it
            binsDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    BinsDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return binsDatabase;
    }

    public abstract BinDao binDao();

}
