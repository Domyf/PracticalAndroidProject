package com.group18.sustainucd.Database;
/*
    This class is the main access point for the app database that contains all the bins.
 */

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Bin.class}, version = 1)
public abstract class BinsDatabase extends RoomDatabase {
    private static BinsDatabase binsDatabase;   //Single instance for the database for all activities
    private static final String DB_NAME = "bins_database";

    public static synchronized BinsDatabase getInstance(Context context) {
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
