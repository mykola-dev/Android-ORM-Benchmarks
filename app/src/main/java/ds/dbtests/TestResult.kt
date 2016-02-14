package ds.dbtests

//@Parcel
data class TestResult(
		val title: String?,
		val writeMessage: String?,
		val writeValue: Long = 0,
		val readMessage: String? = null,
		val readValue: Long = 0)
