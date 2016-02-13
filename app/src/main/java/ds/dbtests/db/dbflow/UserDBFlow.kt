/*
package ds.dbtests.db.dbflow

import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder
import com.raizlabs.android.dbflow.sql.language.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import com.raizlabs.android.dbflow.structure.BaseModel
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer
import ds.dbtests.db.Order
import ds.dbtests.db.User
import java.util.*

@ModelContainer
@Table(database = DBFlowDatabase::class)
class UserDBFlow : BaseModel(), User {

	@PrimaryKey(autoincrement = true)
	@Column override var id: Long? = null
	@Column override var name: String? = null
	@Column override var login: String? = null
	@Column override var password: String? = null
	@Column override var description: String? = null
	@Column override var phone: String? = null
	@Column override var sex: String? = null
	@Column override var age: Int = 0
	@Column override var height: Double = 0.0
	override var orders: Collection<Order>
		get() = getOrdersDbFlow()
		set(v) {
			ordersDbflow = v as MutableList<OrderDBFlow>
		}


	var ordersDbflow: MutableList<OrderDBFlow>? = null


	@OneToMany(*/
/*methods = arrayOf(OneToMany.Method.LOAD, OneToMany.Method.SAVE),*//*
 variableName = "ordersDbflow")
	fun getOrdersDbFlow(): List<OrderDBFlow> {
		if (ordersDbflow == null)
			ordersDbflow = Select()
					.from(OrderDBFlow::class.java)
					.where(Condition.column(OrderDBFlow_Table.USERDBFLOW_USER_ID).eq(id))
					.queryList()

		return ordersDbflow!!
	}


}
*/
