package ds.dbtests.db.objectbox;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Relation;
import io.objectbox.annotation.Generated;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.exception.DbDetachedException;
import io.objectbox.exception.DbException;

@Entity
public class UserBox /*implements ds.dbtests.db.User*/ {

    @Id
    private long id;
    private int age;
    private double height;
    private String name;
    private String login;
    private String password;
    private String sex;
    private String description;
    private String phone;

    @Relation(idProperty = "userId")
    private List<OrderBox> orders;
    /** Used to resolve relations */
    @Internal
    @Generated(hash = 1307364262)
    transient BoxStore __boxStore;

    @Generated(hash = 556203250)
    public UserBox(long id, int age, double height, String name, String login,
            String password, String sex, String description, String phone) {
        this.id = id;
        this.age = age;
        this.height = height;
        this.name = name;
        this.login = login;
        this.password = password;
        this.sex = sex;
        this.description = description;
        this.phone = phone;
    }

    @Generated(hash = 1578460280)
    public UserBox() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 969548343)
    public List<OrderBox> getOrders() {
        if (orders == null) {
            final BoxStore boxStore = this.__boxStore;
            if (boxStore == null) {
                throw new DbDetachedException();
            }
            Box<OrderBox> box = boxStore.boxFor(OrderBox.class);
            int targetTypeId = boxStore.getEntityTypeIdOrThrow(OrderBox.class);
            List<OrderBox> ordersNew = box.getBacklinkEntities(targetTypeId,
                    OrderBox_.userId, id);
            synchronized (this) {
                if (orders == null) {
                    orders = ordersNew;
                }
            }
        }
        return orders;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1446109810)
    public synchronized void resetOrders() {
        orders = null;
    }

    /**
     * Removes entity from its object box. Entity must attached to an entity context.
     */
    @Generated(hash = 1407782313)
    public void remove() {
        if (__boxStore == null) {
            throw new DbDetachedException();
        }
        __boxStore.boxFor(UserBox.class).remove(this);
    }

    /**
     * Puts the entity in its object box.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1264746468)
    public void put() {
        if (__boxStore == null) {
            throw new DbDetachedException();
        }
        __boxStore.boxFor(UserBox.class).put(this);
    }

}
