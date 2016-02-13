package ds.dbtests.db.dbflow;


import com.raizlabs.android.dbflow.annotation.*;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.util.Date;

@Table(database = DBFlowDatabase.class, cachingEnabled = true)
@ModelContainer
public class OrderDBFlow extends BaseModel {

    @PrimaryKey(autoincrement = true) public long id;
    @Column public String title;
    @Column public String description;
    @Column public Date expiration;
    @Column public Date created;
    @Column public int count;
    @Column public double price;

    //@Column long user_id;
    //private UserDBFlow user;

    @ForeignKey(saveForeignKeyModel = false)
    public ForeignKeyContainer<UserDBFlow> user;

    public void setUser(UserDBFlow u) {
        user = FlowManager.getContainerAdapter(UserDBFlow.class).toForeignKeyContainer(u);
        //user = u;
        //user_id = u.id;
    }

    /*public UserDBFlow getUser() {
        if (user == null) {
            if (user_id != 0)
                user = new Select().from(UserDBFlow.class)
                                   .where(UserDBFlow_Table.id.eq(user_id))
                                   .querySingle();
        }

        return user;
    }*/

}

