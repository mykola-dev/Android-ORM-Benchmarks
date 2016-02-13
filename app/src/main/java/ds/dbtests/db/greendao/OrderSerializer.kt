package ds.dbtests.db.greendao

import de.greenrobot.dao.converter.PropertyConverter
import java.util.*

class OrderSerializer : PropertyConverter<List<OrderGreenDao>, ByteArray> {
	override fun convertToEntityProperty(databaseValue: ByteArray): List<OrderGreenDao> {
		return Fst.deserialize(databaseValue) as List<OrderGreenDao>
	}

	override fun convertToDatabaseValue(entityProperty: List<OrderGreenDao>): ByteArray {
		return Fst.serialize(entityProperty)
	}

}