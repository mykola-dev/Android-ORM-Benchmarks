package ds.dbtests.db

import java.util.*

interface Order {
	var id: Long?
	var title: String?
	var description: String?
	var expiration: Date?
	var created: Date?
	var count: Int
	var price: Double
	var user: User?

	//override fun toString(): String = "id=$id item=$title price=$price"
}