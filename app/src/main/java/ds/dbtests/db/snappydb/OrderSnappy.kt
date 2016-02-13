package ds.dbtests.db.dbflow

import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer
import ds.dbtests.db.Order
import ds.dbtests.db.User
import java.util.*

class OrderSnappy : Order {
	override var id: Long? = null
	override var title: String? = null
	override var description: String? = null
	override var expiration: Date? = null
	override var created: Date? = null
	override var count: Int = 0
	override var price: Double = 0.0
	override var user: User?
		get() = userSnappy
		set(v) {
			userSnappy = v as UserSnappy
		}

	private lateinit var userSnappy: UserSnappy

}

