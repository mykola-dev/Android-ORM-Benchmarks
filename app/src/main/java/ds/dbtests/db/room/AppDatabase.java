package ds.dbtests.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {UserRoom.class, OrderRoom.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract OrderDao orderDao();
}
