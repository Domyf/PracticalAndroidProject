package com.group18.sustainucd.Database;
/**
    This class represents the table 'bin' inside the database.
    A bin is represented by an id, a picture file name, a description,
    latitude and longitude.
    These values correspond to the columns of the table bin. The id is the
    primary key.
 */

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Bin {
    //@ColumnInfo(name = "picture_file_name")
    @PrimaryKey
    @NonNull public String pictureFileName;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "latitude")
    public double latitude;
    @ColumnInfo(name = "longitude")
    public double longitude;

    @Ignore
    public double distance;
    @Ignore
    public Bitmap bitmap = null;

}
