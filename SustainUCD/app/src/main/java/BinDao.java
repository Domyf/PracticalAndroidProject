/*
    This class contains the methods used for accessing the database.
    This is the Data Access Object (DAO) associated with the bin database.
 */

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BinDao {
    @Query("SELECT * FROM bin")
    List<Bin> getAll();
}
