package ds.dbtests.db.objectbox;

import java.util.Date;

import io.objectbox.BoxStore;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Relation;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.exception.DbDetachedException;
import io.objectbox.relation.ToOne;


@Entity
public class OrderBox /*implements Order*/ {

    @Id
    private long id;
    private String title;
    private double price;
    private int count;
    private Date created;
    private Date expiration;
    private String description;
    @Relation
    private UserBox user;
    long userId;
    /** Used to resolve relations */
    @Internal
    @Generated(hash = 1307364262)
    transient BoxStore __boxStore;
    @Generated(hash = 861401677)
    public OrderBox(long id, String title, double price, int count, Date created,
            Date expiration, String description, long userId) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.count = count;
        this.created = created;
        this.expiration = expiration;
        this.description = description;
        this.userId = userId;
    }
    @Generated(hash = 851799299)
    public OrderBox() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public Date getExpiration() {
        return expiration;
    }
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    @Internal
    @Generated(hash = 1214004309)
    private transient ToOne<OrderBox, UserBox> user__toOne;
    /** See {@link io.objectbox.relation.ToOne} for details. */
    @Generated(hash = 1060655990)
    public synchronized ToOne<OrderBox, UserBox> getUser__toOne() {
        if (user__toOne == null) {
            user__toOne = new ToOne<>(this, OrderBox_.userId, UserBox.class);
        }
        return user__toOne;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 606673029)
    public UserBox getUser() {
        user = getUser__toOne().getTarget(this.userId);
        return user;
    }
    /** Set the to-one relation including its ID property. */
    @Generated(hash = 1227882430)
    public void setUser(UserBox user) {
        getUser__toOne().setTarget(user);
        this.user = user;
    }
    /**
     * Removes entity from its object box. Entity must attached to an entity context.
     */
    @Generated(hash = 1114511745)
    public void remove() {
        if (__boxStore == null) {
            throw new DbDetachedException();
        }
        __boxStore.boxFor(OrderBox.class).remove(this);
    }
    /**
     * Puts the entity in its object box.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1371990828)
    public void put() {
        if (__boxStore == null) {
            throw new DbDetachedException();
        }
        __boxStore.boxFor(OrderBox.class).put(this);
    }



}
