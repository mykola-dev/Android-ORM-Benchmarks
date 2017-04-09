package ds.dbtests.db.dbflow;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.Index;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

@Table(database = DBFlowDatabase.class, cachingEnabled = true)
//@ModelContainer
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

    @ForeignKey(stubbedRelationship = true, saveForeignKeyModel = false)
    @Index
    public UserDBFlow user;

    public void setUser(UserDBFlow u) {
        user = u;
    }


}

