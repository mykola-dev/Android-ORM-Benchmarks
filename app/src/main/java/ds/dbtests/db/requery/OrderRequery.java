package ds.dbtests.db.requery;


import java.util.Date;

import io.requery.*;

@Entity
public class OrderRequery {

    @Key
    @Generated
    public long id;
    public String title;
    public String description;
    public Date expiration;
    public Date created;
    public int count;
    public double price;

    @ManyToOne(/*cascade = {}*/)
    @Index
    public UserRequery user;

}

