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

    public int makeBackup(int userId) {
        int addedPairs = 0;
        try {
            dao.open();
            List<ContactGroupPair> pairs = getGroups();
            dao.addPairs(pairs, userId);
            addedPairs = pairs.size();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return addedPairs;
    }

    public List<ContactGroupPair> getGroups() {

        Cursor dataCursor = activity.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.Data.CONTACT_ID,
                        ContactsContract.Data.DATA1
                },
                ContactsContract.Data.MIMETYPE + "=?",
                new String[]{ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE}, null
        );

        List<ContactGroupPair> contactGroups = new ArrayList<ContactGroupPair>();

        dataCursor.moveToFirst();
        while(!dataCursor.isAfterLast()) {
            contactGroups.add(getContactFromCursor(dataCursor));
            dataCursor.moveToNext();
        }
        dataCursor.close();
        return contactGroups;
    }

    private ContactGroupPair getContactFromCursor(Cursor cursor) {
        ContactGroupPair contactGroup = new ContactGroupPair();
        contactGroup.contactId = cursor.getInt(0);
        contactGroup.groupId = cursor.getInt(1);
        return contactGroup;
    }

}
