import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bin {
    @PrimaryKey
    public int binId;
    @ColumnInfo(name = "picture_file_name")
    public String pictureFileName;
    @ColumnInfo(name = "description")
    public String description;
}
