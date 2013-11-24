package ua.kpi.sc.control;

import android.app.Activity;
import android.database.sqlite.SQLiteException;

import ua.kpi.sc.dao.UserDAO;
import ua.kpi.sc.model.User;

/**
 * Created by manilo on 23.11.13.
 */
public class UserControl {

    private UserDAO dao;

    public UserControl(Activity activity) {
        dao = new UserDAO(activity.getApplicationContext());
    }

    public void addUser(User user) {
        try {
            dao.open();
            dao.addUser(user);
            dao.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }



}
