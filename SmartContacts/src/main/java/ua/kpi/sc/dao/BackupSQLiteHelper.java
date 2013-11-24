package ua.kpi.sc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by manilo on 22.11.13.
 */
public class BackupSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_BACKUP = "backup_contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_USER_ID, COLUMN_CONTACT_ID, COLUMN_GROUP_ID};

    private static final String DATABASE_NAME = DB.NAME;
    private static final int DATABASE_VERSION = DB.VERSION;

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_BACKUP + " (" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_USER_ID + " integer not null, "
            + COLUMN_CONTACT_ID + " integer not null, " + COLUMN_GROUP_ID + " integer not null);";


    public BackupSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(BackupSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        dropTable(db);
        onCreate(db);
    }

    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BACKUP);
    }
}
