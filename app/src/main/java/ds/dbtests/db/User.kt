package ds.dbtests.db

interface User {
	var id: Long?
	var name: String?
	var login: String?
	var password: String?
	var description: String?
	var phone: String?
	var sex: String?
	var age: Int
	var height: Double
	var orders: Collection<Order>

	//override fun toString(): String = "id=$id name=$name age=$age orders=${orders.size()}"
}