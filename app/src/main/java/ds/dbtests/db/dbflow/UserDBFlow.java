package ds.dbtests.db.dbflow;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.*;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(database = DBFlowDatabase.class, cachingEnabled = true)
@ModelContainer
public class UserDBFlow extends BaseModel {

    @PrimaryKey(autoincrement = true) public long id;
    @Column public String name;
    @Column public String login;
    @Column public String password;
    @Column public String description;
    @Column public String phone;
    @Column public String sex;
    @Column public int age;
    @Column public double height;

    List<OrderDBFlow> orders;

    @OneToMany(methods = {})
    public List<OrderDBFlow> getOrders() {
        if (orders == null) {
            Where<OrderDBFlow> w = SQLite.select()
                                         .from(OrderDBFlow.class)
                                         .where(OrderDBFlow_Table.user_id.eq(id));
            Log.v("query=", w.getQuery());
            orders = w.queryList();

        }
        return orders;
    }

}
