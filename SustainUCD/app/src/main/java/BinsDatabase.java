/*
    This class is the main access point for the app database that contains all the bins.
 */

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Bin.class}, version = 1)
public abstract class BinsDatabase extends RoomDatabase {
    public abstract BinDao binDao();
}
