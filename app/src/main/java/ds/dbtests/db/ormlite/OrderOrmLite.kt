package ds.dbtests.db.ormlite

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.misc.BaseDaoEnabled
import com.j256.ormlite.table.DatabaseTable
import ds.dbtests.db.Order
import ds.dbtests.db.User
import java.util.*

@DatabaseTable
class OrderOrmLite : Order, BaseDaoEnabled<OrderOrmLite, Int>() {

	@DatabaseField(generatedId = true) override var id: Long? = null
	@DatabaseField override var price: Double = 0.0
	@DatabaseField override var title: String? = null
	@DatabaseField(foreign = true, foreignAutoRefresh = false,index = true) private lateinit var _user: UserOrmLite
	@DatabaseField override var description: String? = null
	@DatabaseField override var expiration: Date? = null
	@DatabaseField override var created: Date? = null
	@DatabaseField override var count: Int = 0
	override var user: User?
		get() = _user
		set(v) {
			_user = v as UserOrmLite
		}
}