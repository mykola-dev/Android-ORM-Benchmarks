/*
package ds.dbtests.db.dbflow

import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer
import ds.dbtests.db.Order
import ds.dbtests.db.User
import java.util.*

@Table(database = DBFlowDatabase::class)
class OrderDBFlow : BaseModel(), Order {

	@PrimaryKey(autoincrement = true)
	@Column override var id: Long? = null
	@Column override var title: String? = null
	@Column override var description: String? = null
	@Column override var expiration: Date? = null
	@Column override var created: Date? = null
	@Column override var count: Int = 0
	@Column override var price: Double = 0.0
	override var user: User?
		get() = userDbflow.toModel()
		set(v) {
			userDbflow = ForeignKeyContainer(UserDBFlow::class.java)
			val model = v as UserDBFlow
			userDbflow.model = model
			userDbflow.put(UserDBFlow_Table.ID, v.id)
		}

	@ForeignKey(
			references = arrayOf(ForeignKeyReference(columnName = "user_id", columnType = Long::class, foreignKeyColumnName = "id", referencedFieldIsPrivate = false)),
			saveForeignKeyModel = false)
	@Column
	lateinit var userDbflow: ForeignKeyContainer<UserDBFlow>

}

*/
