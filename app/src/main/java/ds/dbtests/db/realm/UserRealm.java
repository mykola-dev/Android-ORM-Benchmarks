/*
package ds.dbtests.db.realm;

import ds.dbtests.db.User;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class UserRealm extends RealmObject implements User {

	@PrimaryKey private long realmId;
	private String name;
	private String login;
	private String password;
	private String description;
	private String phone;
	private String sex;
	private int age;
	private double height;
	private RealmList<OrderRealm> ordersRealm;

	@NotNull
	@Override
	public String getName() {
		return name;
	}


	@Override
	public void setName(@NotNull final String s) {
		name = s;
	}


	@NotNull
	@Override
	public String getLogin() {
		return login;
	}


	@Override
	public void setLogin(@NotNull final String s) {
		login = s;
	}


	@NotNull
	@Override
	public String getPassword() {
		return password;
	}


	@Override
	public void setPassword(@NotNull final String s) {
		password = s;
	}


	@NotNull
	@Override
	public String getDescription() {
		return description;
	}


	@Override
	public void setDescription(@NotNull final String s) {
		description = s;
	}


	@NotNull
	@Override
	public String getPhone() {
		return phone;
	}


	@Override
	public void setPhone(@NotNull final String s) {
		phone = s;
	}


	@NotNull
	@Override
	public String getSex() {
		return sex;
	}


	@Override
	public void setSex(@NotNull final String s) {
		sex = s;
	}


	@Override
	public int getAge() {
		return age;
	}


	@Override
	public void setAge(final int i) {
		age = i;
	}


	@Override
	public double getHeight() {
		return height;
	}


	@Override
	public void setHeight(final double v) {
		height = v;
	}


	public RealmList<OrderRealm> getOrdersRealm() {
		return ordersRealm;
	}


	public void setOrdersRealm(RealmList<OrderRealm> ordersRealm) {
		this.ordersRealm = ordersRealm;
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
