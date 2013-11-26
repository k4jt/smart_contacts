package ua.kpi.sc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import ua.kpi.sc.model.User;

/**
 * Created by manilo on 23.11.13.
 */
public class UserDAO {

    private SQLiteDatabase database;
    private UserSQLiteHelper userHelper;

    public UserDAO(Context context) {
        userHelper = new UserSQLiteHelper(context);
    }

    public void open() throws SQLiteException {
        database = userHelper.getWritableDatabase();
    }

    public void createTable() {
        try {
            open();
            userHelper.onCreate(database);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void close() {
        userHelper.close();
    }

    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UserSQLiteHelper.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(UserSQLiteHelper.COLUMN_LAST_NAME, user.getLastName());
        values.put(UserSQLiteHelper.COLUMN_EMAIL, user.getEmail());
        values.put(UserSQLiteHelper.COLUMN_LOGIN, user.getLogin());
        values.put(UserSQLiteHelper.COLUMN_PASSWORD, user.getPassword());
        database.insert(UserSQLiteHelper.TABLE_USER, null, values);

        User registeredUser = getUserByLogin(user.getLogin());
        return registeredUser.getId();
    }

    public boolean checkUser(User user) {
        Cursor cursor = null;
        try {
            cursor = database.query(
                    UserSQLiteHelper.TABLE_USER,
                    new String[]{UserSQLiteHelper.COLUMN_PASSWORD},
                    UserSQLiteHelper.COLUMN_LOGIN + " =?",
                    new String[]{user.getLogin()}, null, null, null);

            if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                int pass = cursor.getInt(0);
                return pass == user.getPassword();
            } else {
                return false;
            }
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private User getUserFromCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setLogin(cursor.getString(1));
        user.setFirstName(cursor.getString(2));
        user.setLastName(cursor.getString(3));
        user.setEmail(cursor.getString(4));
        user.setPassword(cursor.getInt(5));

        return user;
    }

    public User getUserByLogin(String login) {
        Cursor cursor = null;
        try {
            cursor = database.query(
                    UserSQLiteHelper.TABLE_USER,
                    UserSQLiteHelper.ALL_COLUMNS,
                    UserSQLiteHelper.COLUMN_LOGIN + " =?",
                    new String[]{login}, null, null, null);

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                return getUserFromCursor(cursor);
            }

        } finally {
            if (cursor != null) cursor.close();
        }

        return null;
    }

    public boolean isLoginExist(String login) {

        Cursor cursor = null;
        try {
            cursor = database.query(
                    UserSQLiteHelper.TABLE_USER,
                    UserSQLiteHelper.ALL_COLUMNS,
                    UserSQLiteHelper.COLUMN_LOGIN + " =?",
                    new String[]{login}, null, null, null);

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                return true;
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return false;
    }

    public boolean isEmailExist(String email) {
        Cursor cursor = null;
        try {
            cursor = database.query(
                    UserSQLiteHelper.TABLE_USER,
                    UserSQLiteHelper.ALL_COLUMNS,
                    UserSQLiteHelper.COLUMN_EMAIL + " =?",
                    new String[]{email}, null, null, null);

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                return true;
            }

        } finally {
            if (cursor != null) cursor.close();
        }

        return false;
    }

    public User getUserById(long id) {
        Cursor cursor = null;
        try {
            cursor = database.query(
                    UserSQLiteHelper.TABLE_USER,
                    UserSQLiteHelper.ALL_COLUMNS,
                    UserSQLiteHelper.COLUMN_ID + " =?",
                    new String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                return getUserFromCursor(cursor);
            }

        } finally {
            if (cursor != null) cursor.close();
        }

        return null;
    }

    public void dropTable() {
        userHelper.dropTable(database);
    }

}
