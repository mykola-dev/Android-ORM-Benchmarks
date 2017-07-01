package ds.dbtests.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    List<Long> insert(List<UserRoom> users);

    @Insert
    long insert(UserRoom user);

    @Query("delete from UserRoom")
    void deleteAll();

    @Query("select * from UserRoom")
    List<UserRoom> fetchAll();

}
