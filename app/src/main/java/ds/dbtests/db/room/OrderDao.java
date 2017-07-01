package ds.dbtests.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    void insert(List<OrderRoom> orders);

    @Insert
    void insert(OrderRoom order);

    @Query("delete from OrderRoom")
    void deleteAll();

    @Query("select * from OrderRoom")
    List<OrderRoom> fetchAll();

    @Query("select * from OrderRoom where userId=:userId")
    List<OrderRoom> fetchOrders(long userId);

}
