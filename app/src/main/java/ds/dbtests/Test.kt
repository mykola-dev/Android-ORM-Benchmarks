package ds.dbtests

interface Test {

	fun writeTest()
	fun readTest(lazy: Boolean)
	fun init()
	fun clean()
}

abstract class TestBase() : Test {
	init {
		init()
	}
}
