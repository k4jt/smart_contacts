package ua.kpi.sc.control;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import ua.kpi.sc.dao.BackupDAO;
import ua.kpi.sc.model.ContactGroupPair;
import ua.kpi.sc.view.MainActivity;

/**
 * Created by manilo on 22.11.13.
 */
public class BackupGroups {

    private BackupDAO dao;

    private final Activity activity;

    public BackupGroups(Activity activity) {
        this.activity = activity;
        dao = new BackupDAO(activity.getApplicationContext());
    }

    public boolean isUserDidBackup(int userId) {
        boolean result = false;
        try {
            dao.open();
            result =  dao.isUserDidBackup(userId);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return result;
    }

    public List<ContactGroupPair> makeBackup(int userId) {
        List<ContactGroupPair> pairs = getGroups();
        makeBackup(userId, pairs);
        return pairs;
    }

    public List<ContactGroupPair> getBackup(int userId) {
        List<ContactGroupPair> pairs = null;
        try {
            dao.open();
            pairs = dao.getBackup(userId);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return pairs;
    }

    public void makeBackup(int userId, List<ContactGroupPair> pairs) {
        try {
            dao.open();
            dao.addPairs(pairs, userId);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }

    public List<ContactGroupPair> getGroups() {

        Cursor dataCursor = null;
        List<ContactGroupPair> contactGroups = new ArrayList<ContactGroupPair>();;

        try {
            dataCursor = activity.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.Data.CONTACT_ID,
                        ContactsContract.Data.DATA1
                },
                ContactsContract.Data.MIMETYPE + "=?",
                new String[]{ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE}, null
                );

            if (dataCursor != null && dataCursor.getCount() > 0 /*&& dataCursor.moveToFirst()*/)
            {
                while(dataCursor.moveToNext() /*!dataCursor.isAfterLast()*/) {
                    contactGroups.add(getContactFromCursor(dataCursor));
                    /*dataCursor.moveToNext();*/
                }
            }
        } finally {
            dataCursor.close();
        }

        return contactGroups;
    }

    private ContactGroupPair getContactFromCursor(Cursor cursor) {
        ContactGroupPair contactGroup = new ContactGroupPair();
        contactGroup.contactId = cursor.getInt(0);
        contactGroup.groupId = cursor.getInt(1);
        return contactGroup;
    }

}
