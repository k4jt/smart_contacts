package ua.kpi.sc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by manilo on 23.11.13.
 */
public class UserSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_LOGIN, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL, COLUMN_PASSWORD};

    private static final String DATABASE_NAME = DB.NAME;
    private static final int DATABASE_VERSION = DB.VERSION;

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USER + " (" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_LOGIN
            + " text not null, " + COLUMN_FIRST_NAME + " text not null, "
            + COLUMN_LAST_NAME + " text not null, " + COLUMN_EMAIL + " text not null, "
            + COLUMN_PASSWORD + " integer not null );";


    public UserSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(UserSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        dropTable(db);
        onCreate(db);
    }

    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
    }


}
