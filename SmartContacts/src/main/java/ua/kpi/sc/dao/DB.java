package ua.kpi.sc.dao;

import android.content.Context;

/**
 * Created by manilo on 23.11.13.
 */
public class DB {
    public static final String NAME = "backup.db";
    public static final int VERSION = 1;

    public static void createTables(Context context) {
        UserDAO userDAO = new UserDAO(context);
        BackupDAO backupDAO = new BackupDAO(context);

        userDAO.createTable();
        backupDAO.createTable();
    }

    public static void dropTables(Context context) {
        UserDAO userDAO = new UserDAO(context);
        BackupDAO backupDAO = new BackupDAO(context);

        userDAO.dropTable();
        backupDAO.dropTable();
    }


}
