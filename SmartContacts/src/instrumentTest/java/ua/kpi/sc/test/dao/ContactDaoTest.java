package ua.kpi.sc.test.dao;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import ua.kpi.sc.dao.ContactDAO;

/**
 * Created by manilo on 29.11.13.
 */
public class ContactDaoTest extends AndroidTestCase {

    public void testNormalizePhoneNumber() {

        String phoneNumber = "+380639811835";
        String expected = "0639811835";

        Assert.assertEquals(expected, ContactDAO.normalizePhoneNumber(phoneNumber));

    }

}
