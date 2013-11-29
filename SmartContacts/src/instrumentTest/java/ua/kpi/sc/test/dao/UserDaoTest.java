package ua.kpi.sc.test.dao;

import android.test.AndroidTestCase;

import ua.kpi.sc.control.UserControl;
import ua.kpi.sc.dao.DB;
import ua.kpi.sc.model.User;

/**
 * Created by manilo on 24.11.13.
 */
public class UserDaoTest extends AndroidTestCase {

    @Override
    public void setUp() {
        DB.createTables(mContext);
    }

    @Override
    public void tearDown() {
        DB.dropTables(mContext);
    }

    public void testCheckUser() {
        UserControl userControl = new UserControl(mContext);

        User user = new User();
        user.setFirstName("First_name");
        user.setLastName("Last_name");
        user.setEmail("email@dot.com");
        user.setLogin("login");
        user.setPassword("password");

        userControl.addUser(user);

        assertEquals(true, userControl.checkUser(user));
    }

}
