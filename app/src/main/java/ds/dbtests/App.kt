package ds.dbtests

import android.app.Application
import com.facebook.stetho.Stetho
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowLog
import com.raizlabs.android.dbflow.config.FlowManager
import com.uphyca.stetho_realm.RealmInspectorModulesProvider

class App : Application() {
	override fun onCreate() {
		super.onCreate()
		instance = this
		Stetho.initializeWithDefaults(this)
		Stetho.initialize(Stetho.newInitializerBuilder(this)
				                  .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
				                  .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
				                  .build())

		/*val config = RealmConfiguration.Builder(this)
			.name("default")
			.schemaVersion(1)
			.deleteRealmIfMigrationNeeded()
			.build()

		Realm.setDefaultConfiguration(config)*/

		// init dbflow
		val flowConfig = FlowConfig.Builder(this).build()
		FlowManager.init(flowConfig)
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.E);
	}

	companion object {
		lateinit var instance: App
	}
}