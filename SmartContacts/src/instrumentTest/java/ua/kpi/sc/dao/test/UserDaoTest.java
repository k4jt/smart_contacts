package ua.kpi.sc.dao.test;

import android.test.ActivityTestCase;

import ua.kpi.sc.control.UserControl;
import ua.kpi.sc.dao.DB;
import ua.kpi.sc.model.User;

/**
 * Created by manilo on 24.11.13.
 */
public class UserDaoTest extends ActivityTestCase {


    public void testCheckUser() {
        DB.createTables(getActivity().getApplicationContext());

        UserControl userControl = new UserControl(getActivity());

        User user = new User();
        user.setFirstName("First_name");
        user.setLastName("Last_name");
        user.setEmail("email@dot.com");
        user.setLogin("login");
        user.setPassword("password");

        userControl.addUser(user);

        assertEquals(true, userControl.checkUser(user));
        DB.dropTables(getActivity().getApplicationContext());
    }

}
