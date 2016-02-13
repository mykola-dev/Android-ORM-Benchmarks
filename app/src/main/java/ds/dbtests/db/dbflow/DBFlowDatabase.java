package ds.dbtests.db.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(
        name = DBFlowDatabase.NAME,
        version = DBFlowDatabase.VERSION
        )
public class DBFlowDatabase {

    public static final String NAME = "dbflow_db";
    public static final int VERSION = 4;
}
