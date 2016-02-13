package ds.dbtests.db.orma;


import com.github.gfx.android.orma.SingleAssociation;
import com.github.gfx.android.orma.annotation.*;

import java.util.Date;

@Table
public class OrderOrma {

    @PrimaryKey/*(autoincrement = true)*/ public long id;
    @Column public String title;
    @Column public String description;
    @Column public Date expiration;
    @Column public Date created;
    @Column public int count;
    @Column public double price;

    //@Column public long userId;

    @Column(indexed = true) public SingleAssociation<UserOrma> user;

  /*  public SingleAssociation<UserOrma> getUser() {
        if (user == null)
            return SingleAssociation.id(0);

        return user;
    }*/

   /* public void setUser(SingleAssociation<UserOrma> user) {
        this.user = user;
    }*/

    //@ForeignKey(saveForeignKeyModel = false)
    //public ForeignKeyContainer<UserDBFlow> user;

   /* @Setter
    public void setUser(UserOrma u) {
        user = u;
        user_id = u.id;
    }*/

   /* public UserOrma getUser() {
        return user.value();
    }

    public void setUser(UserOrma u) {
        user = SingleAssociation.just(u);
    }*/

}

