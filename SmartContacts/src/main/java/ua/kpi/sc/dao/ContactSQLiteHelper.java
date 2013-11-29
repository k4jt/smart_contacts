package ua.kpi.sc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by manilo on 26.11.13.
 */
public class ContactSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = DB.NAME;
    private static final int DATABASE_VERSION = DB.VERSION;

    public ContactSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
