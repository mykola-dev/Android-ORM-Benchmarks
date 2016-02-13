package ds.dbtests

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import butterknife.bindView
import com.github.gfx.android.orma.AccessThreadConstraint
import com.github.gfx.android.orma.SingleAssociation
import com.github.gfx.android.orma.TransactionTask
import com.j256.ormlite.table.TableUtils
import com.raizlabs.android.dbflow.runtime.TransactionManager
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import com.snappydb.DB
import com.snappydb.DBFactory
import ds.dbtests.db.User
import ds.dbtests.db.dbflow.*
import ds.dbtests.db.greendao.*
import ds.dbtests.db.orma.OrderOrma
import ds.dbtests.db.orma.OrmaDatabase
import ds.dbtests.db.orma.UserOrma
import ds.dbtests.db.ormlite.OrderOrmLite
import ds.dbtests.db.ormlite.OrmLiteDB
import ds.dbtests.db.ormlite.UserOrmLite
import ds.dbtests.db.realm.OrderRealm
import ds.dbtests.db.realm.UserRealm
import io.realm.Realm
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.observable
import rx.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {
	companion object {
		const val LAZY = false
		const val ITERATIONS = 99
		const val ORDERS = 99
		const val DESCRIPTION = """Once upon a midnight dreary, while I pondered, weak and weary,
	Over many a quaint and curious volume of forgotten lore,
	While I nodded, nearly napping, suddenly there came a tapping,
	As of some one gently rapping, rapping at my chamber door.
	"'Tis some visitor," I muttered, "tapping at my chamber door-
	Only this, and nothing more."""
		@JvmField val names: Array<String> = arrayOf("Leonardo", "Donatello", "Raphael", "Michaelangelo")
		@JvmField val ages: Array<Int> = Array(100, { it })
	}


	private lateinit var greenDao: DaoSession
	private lateinit var ormLite: OrmLiteDB
	private lateinit var realm: Realm
	private lateinit var snappy: DB
	private lateinit var orma: OrmaDatabase
	private var memory: MutableList<User> = arrayListOf()

	private val textView by bindView<TextView>(R.id.text)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initGreenDao()
		initOrmLite()
		initRealm()
		initDBFlow()
		initSnappy()
		initOrma()
	}


	private fun initOrma() {
		orma = OrmaDatabase.builder(this)
				.readOnMainThread(AccessThreadConstraint.WARNING) // optional
				.writeOnMainThread(AccessThreadConstraint.FATAL) // optional
				.trace(false)
				.foreignKeys(false)
				.name("orma_db")
				.build();
	}


	private fun initSnappy() {
		snappy = DBFactory.open(this)
	}

	private fun initDBFlow() {
		// nothing to init
	}


	private fun initRealm() {
		// nothing to init
	}

	private fun initOrmLite() {
		ormLite = OrmLiteDB.instance
	}


	private fun initGreenDao() {
		val helper = DaoMaster.DevOpenHelper(this, "greendao_db", null)
		val db = helper.writableDatabase
		val daoMaster = DaoMaster(db)
		greenDao = daoMaster.newSession()
	}


	private fun runTests() {
		//System.gc()
		message("Run test with $ITERATIONS users, $ORDERS orders in each user.\nPlease wait until 'done' message")
		observable<Pair<Long, String>> {
			realm = Realm.getInstance(this)
			it.onNext(profile { cleanAll() } to "Clean DB")

			//it.onNext(profile { ormaWriteTest() } to "Orma write test")
			it.onNext(profile {
				orma.transactionSync(object : TransactionTask() {
					override fun execute() = ormaWriteTest()
				})
			} to "Orma write test in transaction")
			it.onNext(profile { ormaReadTest(false) } to "Orma read test")

			it.onNext(profile { TransactionManager.transact(DBFlowDatabase.NAME, { dbflowWriteTest() }) } to "DBFlow write test in transaction")
			//it.onNext(profile { dbflowReadTest(true) } to "DBFlow lazy read test")
			it.onNext(profile { dbflowReadTest(false) } to "DBFlow full read test")

			it.onNext(profile { greenDao.runInTx { greenDaoWriteTest() } } to "GreenDao write test in transaction")
			//it.onNext(profile { greenDaoReadTest(true) } to "GreenDao lazy read test")
			it.onNext(profile { greenDaoReadTest(false) } to "GreenDao full read test")

			it.onNext(profile { realm.executeTransaction { realmWriteTest() } } to "Realm write test in transaction")
			it.onNext(profile { realmReadTest() } to "Realm read test")

			it.onNext(profile { snappyWriteTest() } to "Snappy write test")
			it.onNext(profile { snappyReadTest() } to "Snappy read test")

			it.onNext(profile { ormLite.usersDao.callBatchTasks { ormLiteWriteTest() } } to "OrmLite write test in transaction")
			it.onNext(profile { ormLiteReadTest() } to "OrmLite read test")

			it.onNext(profile { memoryWriteTest() } to "Memory write test")
			it.onNext(profile { memoryReadTest() } to "Memory read test")
			/*

			it.onNext(profile { greenDao.runInTx { greenDaoWriteTest2() } } to "GreenDao flat write test in transaction")
			it.onNext(profile { greenDaoReadTest2() } to "GreenDao flat read test")
			it.onNext(profile { cleanAll() } to "Clean DB")

*/


			realm.close()
			it.onCompleted()
		}
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe ({
					            message("${it.second}: ${it.first}ms")
				            }, {
					            it.printStackTrace()
					            message("Failure")
				            }, {
					            message("done!")
					            realm = Realm.getInstance(this)
					            message("refresh realm: ${profile { realm.refresh() }}")
				            })


	}


	private fun message(t: String) {
		runOnUiThread({ textView.text += "$t\n" })
	}

	private fun profile(f: () -> Any?): Long {
		System.gc()
		val time = System.currentTimeMillis()
		f()
		return System.currentTimeMillis() - time
	}

	private fun cleanAll() {
		// greendao clean
		greenDao.userGreenDaoDao.deleteAll()
		greenDao.orderGreenDaoDao.deleteAll()

		// ormlite clean
		TableUtils.clearTable(ormLite.connectionSource, UserOrmLite::class.java)
		TableUtils.clearTable(ormLite.connectionSource, OrderOrmLite::class.java)

		// realm clean
		try {
			realm.executeTransaction {
				realm.clear(UserRealm::class.java)
				realm.clear(OrderRealm::class.java)
				Log.d("#", "realm size=${realm.where(OrderRealm::class.java).count()}")
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}

		// delete dbflow
		Delete.tables(UserDBFlow::class.java, OrderDBFlow::class.java)

		// snappy clean
		snappy.destroy()
		snappy = DBFactory.open(this)

		// orma clean
		orma.deleteFromOrderOrma().execute();
		orma.deleteFromUserOrma().execute();

		// clean memory
		memory.clear()
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private fun greenDaoWriteTest() {
		for (i in 0..ITERATIONS) {
			val u: UserGreenDao = UserGreenDao()
			u.name = names[i % names.size]
			u.age = ages[i % ages.size]
			u.height = 1.85
			u.description = DESCRIPTION
			u.login = "login"
			u.password = "password123"
			u.phone = "555-123-4567"
			u.sex = "male"
			greenDao.insert(u)
			for (k in 0..ORDERS) {
				val o = OrderGreenDao()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				o.user = u
				greenDao.insert(o)
			}
		}
	}

	private fun greenDaoReadTest(lazy: Boolean) {
		greenDao.clear()
		val users = greenDao.userGreenDaoDao.loadAll()
		if (!lazy)
			for ((i, u) in users.withIndex()) {
				for (o in u.orders) {
					if (i == 0) {
						//Log.v("#", "greendao fingerprint=${o.toString()}")
					}
				}
			}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private fun greenDaoWriteTest2() {
		FlatTests.greenDaoWriteTest2(greenDao)
	}

	private fun greenDaoReadTest2() {
		FlatTests.greenDaoReadTest2(greenDao)
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private fun ormLiteWriteTest() {
		for (i in 0..ITERATIONS) {
			val u: UserOrmLite = UserOrmLite()
			u.name = names[i % names.size]
			u.age = ages[i % ages.size]
			u.height = 1.85
			u.description = DESCRIPTION
			u.login = "login"
			u.password = "password123"
			u.phone = "555-123-4567"
			u.sex = "male"
			ormLite.usersDao.create(u)
			for (k in 0..ORDERS) {
				val o = OrderOrmLite()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				o.user = u
				ormLite.ordersDao.create(o)
			}
		}

	}

	private fun ormLiteReadTest() {
		ormLite.usersDao.clearObjectCache()
		val users = ormLite.usersDao.queryForAll()
		if (!LAZY)
			for (u in users) {
				for ((i, o) in u.orders.withIndex()) {
					if (i == 0)
					//Log.v("#", "ormlite fingerprint=${o.toString()}")
						o.title
				}
			}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private fun realmWriteTest() {
		for (i in 0..ITERATIONS) {
			val u = realm.createObject(UserRealm::class.java)
			u.id = i.toLong() + 1
			u.name = names[i % names.size]
			u.age = ages[i % ages.size]
			u.height = 1.85
			u.description = DESCRIPTION
			u.login = "login"
			u.password = "password123"
			u.phone = "555-123-4567"
			u.sex = "male"
			for (k in 0..ORDERS) {
				val o = realm.createObject(OrderRealm::class.java)
				//val o = OrderRealm()
				o.id = (i * (ORDERS + 1) + k + 1).toLong()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				u.orders.add(o)
				realm.copyToRealm(o)
			}
		}
	}

	private fun realmReadTest() {

		val users = realm.allObjects(UserRealm::class.java)
		if (!LAZY)
			for ((i, u) in users.withIndex()) {
				for (o in u.orders) {
					o.title
				}
			}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private fun dbflowWriteTest() {
		for (i in 0..ITERATIONS) {
			val u: UserDBFlow = UserDBFlow()
			u.name = names[i % names.size]
			u.age = ages[i % ages.size]
			u.height = 1.85
			u.description = DESCRIPTION
			u.login = "login"
			u.password = "password123"
			u.phone = "555-123-4567"
			u.sex = "male"
			u.save()

			for (k in 0..ORDERS) {
				val o = OrderDBFlow()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				o.setUser(u)
				//o.user_id = u.id

				//u.orders.add(o)
				o.save()
			}


		}


	}


	private fun dbflowReadTest(lazy: Boolean) {

		val users = Select()
				.from(UserDBFlow::class.java)
				.where()
				.queryList()

		if (!lazy) {
			println("not lazy!")
			for ((i, u) in users.withIndex()) {
				for (o in u.orders) {
				}
			}
		}
	}

	private fun ormaWriteTest() {
		for (i in 0..ITERATIONS) {
			val u = UserOrma()
			u.name = names[i % names.size]
			u.age = ages[i % ages.size]
			u.height = 1.85
			u.description = DESCRIPTION
			u.login = "login"
			u.password = "password123"
			u.phone = "555-123-4567"
			u.sex = "male"
			val userId = orma.insertIntoUserOrma(u)

			for (k in 0..ORDERS) {
				val o = OrderOrma()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				o.user = SingleAssociation.just(userId, u)    // throws exception if user == null :(
				orma.insertIntoOrderOrma(o)
				//Log.v("order id=", "" + o.id);
			}


		}


	}


	private fun ormaReadTest(lazy: Boolean) {

		val users = orma.selectFromUserOrma().toList()

		if (!lazy) {
			println("orma full read")
			for ((i, u) in users.withIndex()) {
				val orders = u.getOrders(orma).toList()
				Log.v("orders size " + i, "" + orders.size)
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private fun snappyWriteTest() {
		for (i in 0..ITERATIONS) {
			val u: UserSnappy = UserSnappy()
			u.id = i.toLong() + 1
			u.name = names[i % names.size]
			u.age = ages[i % ages.size]
			u.height = 1.85
			u.description = DESCRIPTION
			u.login = "login"
			u.password = "password123"
			u.phone = "555-123-4567"
			u.sex = "male"

			for (k in 0..ORDERS) {
				val o = OrderSnappy()
				o.id = (i * (ORDERS + 1) + k + 1).toLong()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				o.user = u
				u.addOrder(o)
			}

			snappy.put("user:${u.id.toString()}", u)

		}
	}

	private fun snappyReadTest() {
		val keys = snappy.findKeysIterator("user")
		keys.use {
			for (k in keys.next(ITERATIONS)) {
				val u = snappy.getObject(k, UserSnappy::class.java)
				if (!LAZY)
					for ((i, o) in u.orders.withIndex()) {
					}

			}
		}


	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private fun memoryWriteTest() {
		for (i in 0..ITERATIONS) {
			val u: UserSnappy = UserSnappy()
			u.id = i.toLong() + 1
			u.name = names[i % names.size]
			u.age = ages[i % ages.size]
			u.height = 1.85
			u.description = DESCRIPTION
			u.login = "login"
			u.password = "password123"
			u.phone = "555-123-4567"
			u.sex = "male"

			for (k in 0..ORDERS) {
				val o = OrderSnappy()
				o.id = (i * (ORDERS + 1) + k + 1).toLong()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				o.user = u
				u.addOrder(o)
			}

			memory.add(u)

		}
	}

	private fun memoryReadTest() {

		for (u in memory) {
			if (!LAZY)
				for ((i, o) in u.orders.withIndex()) {
				}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.main, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.run -> runTests()
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onDestroy() {
		super.onDestroy()
		ormLite.close()
		greenDao.database.close()
	}
}
