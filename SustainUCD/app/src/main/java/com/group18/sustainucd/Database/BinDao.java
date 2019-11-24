package com.group18.sustainucd.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This class contains the methods used for accessing the database.
 * This is the Data Access Object (DAO) associated with the bin database.
 */
@Dao
public interface BinDao {
    @Query("SELECT * FROM bin")
    List<Bin> getAll();
    @Insert
    void insertBin(Bin bin);
    @Delete
    void delete(Bin bin);
}
