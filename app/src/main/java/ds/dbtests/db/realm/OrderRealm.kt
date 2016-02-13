package ds.dbtests.db.realm

import ds.dbtests.db.Order
import ds.dbtests.db.User
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class OrderRealm : RealmObject() {
	@PrimaryKey open var id: Long = -1
	open var title: String? = null
	open var description: String? = null
	open var expiration: Date? = null
	open var created: Date? = null
	open var count: Int = 0
	open var price: Double = 0.0

}
