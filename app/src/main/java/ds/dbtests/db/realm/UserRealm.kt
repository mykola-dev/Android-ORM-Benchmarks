package ds.dbtests.db.realm

import ds.dbtests.db.Order
import ds.dbtests.db.User
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class UserRealm : RealmObject() {
	@PrimaryKey open var id: Long = -1
	open var name: String? = null
	open var login: String? = null
	open var password: String? = null
	open var description: String? = null
	open var phone: String? = null
	open var sex: String? = null
	open var age: Int = 0
	open var height: Double = 0.0
	open var orders: RealmList<OrderRealm> = RealmList()

}
