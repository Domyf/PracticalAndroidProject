import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BinDao {
    @Query("SELECT * FROM bin")
    List<Bin> getAll();
}
