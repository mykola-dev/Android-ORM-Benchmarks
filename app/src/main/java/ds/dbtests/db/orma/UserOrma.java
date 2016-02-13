package ds.dbtests.db.orma;

import com.github.gfx.android.orma.annotation.*;

@Table
public class UserOrma {

    @PrimaryKey/*(autoincrement = true) */ public long id;
    @Column public String name;
    @Column public String login;
    @Column public String password;
    @Column public String description;
    @Column public String phone;
    @Column public String sex;
    @Column public int age;
    @Column public double height;

    public OrderOrma_Relation getOrders(OrmaDatabase orma) {
        return orma.relationOfOrderOrma().userEq(this);
    }

}
