package ds.dbtests.db.firebase;

import java.util.ArrayList;
import java.util.List;

public class UserFB {

    public long id;
    public int age;
    public double height;
    public String name;
    public String login;
    public String password;
    public String sex;
    public String description;
    public String phone;

    public List<OrderFB> orders;

    public UserFB() {
        this.orders = new ArrayList<>();
    }

}
