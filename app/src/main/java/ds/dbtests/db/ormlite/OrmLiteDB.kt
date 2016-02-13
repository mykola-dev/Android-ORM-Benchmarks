package ds.dbtests.db.ormlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.misc.BaseDaoEnabled
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import ds.dbtests.App

private val DATABASE_NAME: String = "ormlite_db"
private val DATABASE_VERSION: Int = 3

class OrmLiteDB(context: Context?)
: OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

	private var classes = arrayOf<Class<out BaseDaoEnabled<*, *>>>(UserOrmLite::class.java, OrderOrmLite::class.java)
	lateinit var usersDao: BaseDaoImpl<UserOrmLite, Int>
	lateinit var ordersDao: BaseDaoImpl<OrderOrmLite, Int>

	init {
		usersDao = getDao(UserOrmLite::class.java)
		ordersDao = getDao(OrderOrmLite::class.java)
	}

	override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
		for (cls in classes) {
			TableUtils.createTable(connectionSource, cls)
		}
	}

	override fun <D : Dao<T, *>, T> getDao(clazz: Class<T>): D {
		val dao: D
		dao = super.getDao<D, T>(clazz)
		dao.setObjectCache(true)
		return dao
	}

	override fun onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
		for (cls in classes) {
			dropTable(connectionSource,cls)
		}
		// recreating tables
		onCreate(database, connectionSource)
	}

	fun <T> dropTable(connectionSource: ConnectionSource, cls: Class<T>) {
		TableUtils.dropTable<T, Int>(connectionSource, cls, true)
	}

	companion object{
		val instance by lazy { OpenHelperManager.getHelper(App.instance, OrmLiteDB::class.java) }
	}
}