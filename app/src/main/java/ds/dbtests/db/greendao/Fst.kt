package ds.dbtests.db.greendao

import org.nustaq.serialization.FSTConfiguration
import org.nustaq.serialization.FSTObjectInput
import java.lang.reflect.Field
import java.util.*

object Fst {
	private val fst = FSTConfiguration.createDefaultConfiguration()

	init {
		fst.setForceSerializable(true)
		fst.registerClass(OrderGreenDao::class.java, Date::class.java, String::class.java, Long::class.java, ArrayList::class.java)
		fst.isShareReferences = false
		fst.isPreferSpeed = true
		fst.isShareReferences = false
	}

	internal var conditionalCallback: FSTObjectInput.ConditionalCallback = object : FSTObjectInput.ConditionalCallback {
		override fun shouldSkip(halfDecoded: Any, streamPosition: Int, field: Field): Boolean {
			return field.name == "user__resolvedKey"
				|| field.name == "user"
				|| field.name == "myDao"
				|| field.name == "daoSession"
		}
	}

	fun deserialize(bytes: ByteArray): Any {
		val input = fst.getObjectInput(bytes)
		input.conditionalCallback = conditionalCallback
		return input.readObject()
	}

	fun serialize(obj: Any): ByteArray = fst.asByteArray(obj)
}