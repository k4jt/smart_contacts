package ua.kpi.sc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.List;

/**
 * Created by manilo on 22.11.13.
 */
public class BackupDAO {
    private SQLiteDatabase database;
    private BackupSQLiteHelper backupHelper;

    public BackupDAO(Context context) {
        backupHelper = new BackupSQLiteHelper(context);
    }

    public void open() throws SQLiteException {
        database = backupHelper.getWritableDatabase();
    }

    public void close() {
        backupHelper.close();
    }

    public void addPairs(List<ContactGroupPair> pairs) {
        for (ContactGroupPair pair : pairs) {
            addPair(pair);
        }
    }

    public void addPair(ContactGroupPair pair) {
        ContentValues values = new ContentValues();
        values.put(BackupSQLiteHelper.COLUMN_CONTACT_ID, pair.contactId);
        values.put(BackupSQLiteHelper.COLUMN_GROUP_ID, pair.groupId);
        database.insert(BackupSQLiteHelper.TABLE_BACKUP, null, values);
    }

    public void deletePair(ContactGroupPair pair) {
        database.delete(BackupSQLiteHelper.TABLE_BACKUP, BackupSQLiteHelper.COLUMN_ID + " = " + pair.id, null);
    }


}
