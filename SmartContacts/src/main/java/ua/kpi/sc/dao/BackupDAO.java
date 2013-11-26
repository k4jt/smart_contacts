package ua.kpi.sc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.List;

import ua.kpi.sc.model.ContactGroupPair;

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

    public void createTable() {
        try {
            open();
            backupHelper.onCreate(database);
        }catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void addPairs(List<ContactGroupPair> pairs, int userId) {
        for (ContactGroupPair pair : pairs) {
            addPair(pair, userId);
        }
    }

    public void addPair(ContactGroupPair pair, int userId) {
        ContentValues values = new ContentValues();
        values.put(BackupSQLiteHelper.COLUMN_USER_ID, userId);
        values.put(BackupSQLiteHelper.COLUMN_CONTACT_ID, pair.contactId);
        values.put(BackupSQLiteHelper.COLUMN_GROUP_ID, pair.groupId);
        database.insert(BackupSQLiteHelper.TABLE_BACKUP, null, values);
    }

    public boolean isUserDidBackup(int userId) {
        Cursor cursor = null;
        try {
            cursor = database.query(
                    BackupSQLiteHelper.TABLE_BACKUP,
                    BackupSQLiteHelper.ALL_COLUMNS,
                    BackupSQLiteHelper.COLUMN_USER_ID + " =?",
                    new String[]{String.valueOf(userId)}, null, null, null);

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                return true;
            }

        } finally {
            if (cursor != null) cursor.close();
        }

        return false;
    }

    public void deletePair(ContactGroupPair pair) {
        database.delete(BackupSQLiteHelper.TABLE_BACKUP, BackupSQLiteHelper.COLUMN_ID + " =?", new String[]{String.valueOf(pair.id)});
    }

    public void dropTable() {
        backupHelper.dropTable(database);
    }

}
