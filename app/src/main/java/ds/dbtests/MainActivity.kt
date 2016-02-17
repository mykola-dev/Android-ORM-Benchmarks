package ds.dbtests

import android.content.Intent
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
import ds.dbtests.db.requery.Models
import ds.dbtests.db.requery.OrderRequeryEntity
import ds.dbtests.db.requery.UserRequeryEntity
import io.realm.Realm
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.sql.ConfigurationBuilder
import io.requery.sql.EntityDataStore
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.observable
import rx.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {
	companion object {
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
	private lateinit var requery: EntityDataStore<Persistable>
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
		initStorio()    // todo
		initRequery()
	}

	private fun initRequery() {
		// override onUpgrade to handle migrating to a new version
		val source = DatabaseSource(this, Models.DEFAULT, 1)
		val configuration = ConfigurationBuilder(source, Models.DEFAULT)
				//.useDefaultLogging()
				//.setEntityCache(EntityCacheBuilder(Models.DEFAULT)
				//.useReferenceCache(true)
				//.useSerializableCache(true)
				//.useCacheManager(cacheManager)
				//		                .build())
				.build();
		requery = EntityDataStore(configuration)//RxSupport.toReactiveStore(EntityDataStore<Persistable>(source.configuration));
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

	private fun initStorio() {
	}


	private fun initGreenDao() {
		val helper = DaoMaster.DevOpenHelper(this, "greendao_db", null)
		val db = helper.writableDatabase
		val daoMaster = DaoMaster(db)
		greenDao = daoMaster.newSession()
	}


	private fun runTests() {
		message("Run test with $ITERATIONS users, $ORDERS orders in each user.\nPlease wait until 'done' message")
		observable<TestResult> {
			realm = Realm.getInstance(this)


			it.onNext(TestResult(null, "Clean DB", profile { cleanAll() }))

			it.onNext(TestResult("ReQuery",
			                     "ReQuery write test in transaction", profile { requery.runInTransaction { requeryWriteTest() } },
			                     "ReQuery full read test", profile { requeryReadTest(false) }))

			it.onNext(TestResult("Orma",
			                     "Orma write test in transaction", profile {
				orma.transactionSync(object : TransactionTask() {
					override fun execute() = ormaWriteTest()
				})
			},
			                     "Orma read test", profile { ormaReadTest(false) }))

			it.onNext(TestResult("DBFlow",
			                     "DBFlow write test in transaction", profile { TransactionManager.transact(DBFlowDatabase.NAME, { dbflowWriteTest() }) },
			                     "DBFlow full read test", profile { dbflowReadTest(false) }))

			it.onNext(TestResult("GreenDAO",
			                     "GreenDao write test in transaction", profile { greenDao.runInTx { greenDaoWriteTest() } },
			                     "GreenDao full read test", profile { greenDaoReadTest(false) }))

			it.onNext(TestResult("Realm",
			                     "Realm write test in transaction", profile { realm.executeTransaction { realmWriteTest() } },
			                     "Realm read test", profile { realmReadTest() }))

			it.onNext(TestResult("SnappyDB",
			                     "Snappy write test", profile { snappyWriteTest() },
			                     "Snappy read test", profile { snappyReadTest() }))

			it.onNext(TestResult("ORMLite",
			                     "OrmLite write test in transaction", profile { ormLite.usersDao.callBatchTasks { ormLiteWriteTest() } },
			                     "OrmLite read test", profile { ormLiteReadTest() }))

			it.onNext(TestResult("RAM",
			                     "Memory write test", profile { memoryWriteTest() },
			                     "Memory read test", profile { memoryReadTest() }))

			realm.close()
			it.onCompleted()
		}
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe ({
					            if (it.writeMessage != null)
						            message("${it.writeMessage}: ${it.writeValue}ms")

					            if (it.readMessage != null)
						            message("${it.readMessage}: ${it.readValue}ms")

					            if (it.title != null)
						            ChartData.results.add(it)

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
		snappy.del("user")
		snappy.del("order")

		// orma clean
		orma.deleteFromOrderOrma().execute();
		orma.deleteFromUserOrma().execute();

		// requery clean
		requery.delete(UserRequeryEntity::class.java).get().value()
		requery.delete(OrderRequeryEntity::class.java).get().value()

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
		for (u in users) {
			for ((i, o) in u.orders.withIndex()) {
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
			//println("not lazy!")
			for ((i, u) in users.withIndex()) {
				for (o in u.orders) {
				}
			}
		}
	}

	private fun ormaWriteTest() {
		val userInserter = orma.prepareInsertIntoUserOrma()
		val orderInserter = orma.prepareInsertIntoOrderOrma();

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
			val userId = userInserter.execute(u)

			for (k in 0..ORDERS) {
				val o = OrderOrma()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				o.user = SingleAssociation.just(userId, u)    // throws exception if user == null :(
				orderInserter.execute(o)
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
				//Log.v("orders size " + i, "" + orders.size)
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private fun requeryWriteTest(): Unit {
		println("requery write")
		for (i in 0..ITERATIONS) {
			var u = UserRequeryEntity()
			u.name = names[i % names.size]
			u.age = ages[i % ages.size]
			u.height = 1.85
			u.description = DESCRIPTION
			u.login = "login"
			u.password = "password123"
			u.phone = "555-123-4567"
			u.sex = "male"
			//u.orders = arrayListOf()
			u = requery.insert(u)

			for (k in 0..ORDERS) {
				val o = OrderRequeryEntity()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				o.user = u
				requery.insert(o)
				//u.orders.add(o)
				//Log.v("order id=", "" + o.id);
			}

		}
	}

	private fun requeryReadTest(lazy: Boolean): Unit {
		val users = requery.select(UserRequeryEntity::class.java).get().toList()
		println("requery users=${users.size}")
		if (!lazy) {
			println("requery full read")
			for ((i, u) in users.withIndex()) {
				val orders = u.getOrders()
				for (order in orders) {
					//println(order.id)
				}
				//Log.v("orders size " + i, "" + orders.size)
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

			snappy.put("user${u.id}", u)

			for (k in 0..ORDERS) {
				val o = OrderSnappy()
				o.id = (i * (ORDERS + 1) + k + 1).toLong()
				o.title = "${u.name}'s item"
				o.price = 99.95
				o.count = k % 2 + 1
				o.created = Date()
				o.expiration = Date(System.currentTimeMillis() + 1000 * 60)
				o.description = DESCRIPTION
				//o.user = u
				//u.addOrder(o)

				val key = "order${u.id}:${o.id}"
				snappy.put(key, o)
				//println("write order [$key]")
			}


		}
	}

	private fun snappyReadTest() {
		val userKeys = snappy.findKeys("user")
		//println("snappy users size ${userKeys.size}")
		for (key in userKeys) {
			val user = snappy.getObject(key, UserSnappy::class.java)
			val orderKey = "order${user.id}:"
			val ordersKeys = snappy.findKeys(orderKey)
			//println("read order [$orderKey]")
			//println("snappy orders size ${ordersKeys.size}")
			for (key in ordersKeys) {
				val order = snappy.getObject(key, OrderSnappy::class.java)
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
			R.id.chart -> startActivity(Intent(this, ChartActivity::class.java))
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onDestroy() {
		super.onDestroy()
		ormLite.close()
		greenDao.database.close()
	}
}
