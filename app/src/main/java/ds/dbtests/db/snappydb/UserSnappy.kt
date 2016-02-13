package ds.dbtests.db.dbflow

import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.Select
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer
import ds.dbtests.db.Order
import ds.dbtests.db.User
import java.util.*

class UserSnappy : User {

	override var id: Long? = null
	override var name: String? = null
	override var login: String? = null
	override var password: String? = null
	override var description: String? = null
	override var phone: String? = null
	override var sex: String? = null
	override var age: Int = 0
	override var height: Double = 0.0
	override var orders: Collection<Order>
		get() = ordersSnappy
		set(v) {
			ordersSnappy = v as MutableList<OrderSnappy>
		}


	private var ordersSnappy: MutableList<OrderSnappy> = arrayListOf()

	fun addOrder(o:OrderSnappy){
		ordersSnappy.add(o)
	}

}
