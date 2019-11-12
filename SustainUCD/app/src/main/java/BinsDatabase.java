import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Bin.class}, version = 1)
public abstract class BinsDatabase extends RoomDatabase {
    public abstract BinDao binDao();
}
