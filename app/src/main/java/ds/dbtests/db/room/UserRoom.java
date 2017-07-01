package ds.dbtests.db.room;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class UserRoom {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String login;
    public String password;
    public String description;
    public String phone;
    public String sex;
    public int age;
    public double height;

    //@Relation(parentColumn = "id", entityColumn = "userId")
    //public List<OrderRoom> orders;

}
