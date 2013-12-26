package ua.kpi.sc.control;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import ua.kpi.sc.dao.UserDAO;
import ua.kpi.sc.model.User;

/**
 * Created by manilo on 23.11.13.
 */
public class UserControl {

    private UserDAO dao;

    public UserControl(Context context) {
        dao = new UserDAO(context);
    }

    public long addUser(User user) {
        long userId = 0;
        try {
            dao.open();
            userId = dao.addUser(user);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return userId;
    }

    public boolean checkUser(User user) {
        boolean result = false;
        try {
            dao.open();
            result = dao.checkUser(user);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return result;
    }

    public User getUserByLogin(String login) {
        User user = null;
        try {
            dao.open();
            user = dao.getUserByLogin(login);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return user;
    }

    public boolean isLoginExist(String login) {
        boolean result = false;
        try {
            dao.open();
            result = dao.isLoginExist(login);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return result;
    }

    public boolean isEmailExist(String email) {
        boolean result = false;
        try {
            dao.open();
            result = dao.isEmailExist(email);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return result;
    }

}
