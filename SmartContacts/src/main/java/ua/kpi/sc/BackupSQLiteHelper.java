package ua.kpi.sc;

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
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_GROUP_ID = "group_id";

    private static final String DATABASE_NAME = "backup.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_BACKUP + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CONTACT_ID
            + " integer not null, " + COLUMN_GROUP_ID + " integer not null);";


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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BACKUP);
        onCreate(db);
    }
}
