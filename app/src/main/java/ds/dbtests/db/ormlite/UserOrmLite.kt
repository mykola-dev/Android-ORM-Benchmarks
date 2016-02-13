package ds.dbtests.db.ormlite

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.misc.BaseDaoEnabled
import com.j256.ormlite.table.DatabaseTable
import ds.dbtests.db.Order
import ds.dbtests.db.User

@DatabaseTable
class UserOrmLite : User, BaseDaoEnabled<UserOrmLite, Int>() {

	@DatabaseField(generatedId = true) override var id: Long? = null
	@DatabaseField override var age: Int = 0
	@DatabaseField override var name: String? = null
	@ForeignCollectionField(eager = false) private lateinit var _orders: java.util.Collection<OrderOrmLite>
	@DatabaseField override var login: String? = null
	@DatabaseField override var password: String? = null
	@DatabaseField override var description: String? = null
	@DatabaseField override var phone: String? = null
	@DatabaseField override var sex: String? = null
	@DatabaseField override var height: Double = 0.0
	override var orders: Collection<Order>
		get() = _orders as Collection<Order>
		set(v) {
			_orders = v as java.util.Collection<OrderOrmLite>
		}

}