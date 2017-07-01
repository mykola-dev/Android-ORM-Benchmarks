package ds.dbtests.db.room;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(
        entity = UserRoom.class,
        childColumns = "userId",
        parentColumns = "id"

))
public class OrderRoom {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String title;
    public String description;
    public Date expiration;
    public Date created;
    public int count;
    public double price;

    public long userId;

}

