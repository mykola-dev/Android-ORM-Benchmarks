package ds.dbtests.db.requery;


import java.util.List;

import io.requery.*;

@Entity
public class UserRequery {

    @Key
    @Generated
    public long id;
    public String name;
    public String login;
    public String password;
    public String description;
    public String phone;
    public String sex;
    public int age;
    public double height;

    @OneToMany(cascade = {})
    public List<OrderRequery> orders;

}
