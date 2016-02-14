package ds.dbtests.db.greendao;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ds.dbtests.MainActivity;

public class FlatTests {

    public static void greenDaoWriteTest2(DaoSession s) {
        for (int i = 0; i < MainActivity.ITERATIONS; i++) {
            UserGreenDao u = new UserGreenDao();
            u.setName(MainActivity.names[i % MainActivity.names.length]);
            u.setAge(MainActivity.ages[i % MainActivity.ages.length]);
            u.setHeight(1.85);
            u.setDescription(MainActivity.DESCRIPTION);
            u.setLogin("login");
            u.setPassword("password123");
            u.setPhone("555-123-4567");
            u.setSex("male");
            ArrayList<OrderGreenDao> orders = new ArrayList<>();
            for (int k = 0; k < MainActivity.ORDERS; k++) {
                OrderGreenDao o = new OrderGreenDao();
                o.setTitle("just a title ");
                o.setPrice(99.95);
                o.setCount(k % 2 + 1);
                o.setCreated(new Date());
                o.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60));
                o.setDescription(MainActivity.DESCRIPTION);
                orders.add(o);
            }
            u.setOrders(orders);
            s.insert(u);
        }

    }


    public static void greenDaoReadTest2(DaoSession s) throws IOException, ClassNotFoundException {
        s.clear();
        List<UserGreenDao> users = s.getUserGreenDaoDao().loadAll();
        for (int i = 0, c = users.size(); i < c; i++) {
            UserGreenDao u = users.get(i);
            for (OrderGreenDao o : u.getRawOrders()) {
                if (i == 0)
                    Log.v("#", "greendao fingerprint=" + o.toString());
                o.getTitle();
            }
        }
    }
}
