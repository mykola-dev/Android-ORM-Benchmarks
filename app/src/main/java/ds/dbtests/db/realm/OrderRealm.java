/*
package ds.dbtests.db.realm;

import ds.dbtests.db.Order;
import ds.dbtests.db.User;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public abstract class OrderRealm extends RealmObject implements Order {

	@PrimaryKey
	private long realmId;
	private String item;
	private String description;
	private Date expiration;
	private Date created;
	private int count;
	private double price;
	@Ignore
	private User user;



	@Nullable
	@Override
	public String getItem() {
		return item;
	}


	@Override
	public void setItem(@Nullable final String s) {
		item = s;
	}


	@Nullable
	@Override
	public String getDescription() {
		return description;
	}


	@Override
	public void setDescription(@Nullable final String s) {
		description = s;
	}


	@Nullable
	@Override
	public Date getExpiration() {
		return expiration;
	}


	@Override
	public void setExpiration(@Nullable final Date date) {
		expiration = date;
	}


	@Nullable
	@Override
	public Date getCreated() {
		return created;
	}


	@Override
	public void setCreated(@Nullable final Date date) {
		created = date;
	}


	@Override
	public int getCount() {
		return count;
	}


	@Override
	public void setCount(final int i) {
		count = i;
	}


	@Override
	public double getPrice() {
		return price;
	}


	@Override
	public void setPrice(final double v) {
		price = v;
	}


	@Nullable
	@Override
	public User getUser() {
		return user;
	}


	@Override
	public void setUser(@Nullable final User user) {
		this.user = user;
	}


	public long getRealmId() {
		return realmId;
	}


	public void setRealmId(long realmId) {
		this.realmId = realmId;
	}


	@Nullable
	@Override
	public abstract Long getId();


	@Override
	public abstract  void setId(@Nullable final Long aLong);
}
*/
