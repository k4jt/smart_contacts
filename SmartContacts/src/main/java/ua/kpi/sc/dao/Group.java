package ua.kpi.sc.dao;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manilo on 20.12.13.
 */
public class Group {

    private static final int HIDE = 1;
    private static final int SHOW = 0;
    private Activity _activity;

    public Group(Activity activity) {
        _activity = activity;
    }

    public void createGroup(String groupName)
    {
        ContentResolver cr = _activity.getContentResolver();
        ContentValues groupValues = new ContentValues();
        groupValues.put(ContactsContract.Groups.TITLE, groupName);
        groupValues.put(ContactsContract.Groups.GROUP_VISIBLE, 0);

        cr.insert(ContactsContract.Groups.CONTENT_URI, groupValues);
    }

    public void hideAllGroups() {
        ContentResolver cr = _activity.getContentResolver();

        Cursor c = null;
        List<String> groupIds = new ArrayList<String>();
        try {
            c = cr.query(ContactsContract.Groups.CONTENT_URI, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex(ContactsContract.Groups._ID));
                    groupIds.add(id);
                }
            }
        } finally {
            if (c != null) c.close();
        }
        for (String id : groupIds) {
            ContentValues groupValues = new ContentValues();
            groupValues.put(ContactsContract.Groups.DELETED, HIDE);
            cr.update(ContactsContract.Groups.CONTENT_URI, groupValues,
                    ContactsContract.Groups._ID + "=?", new String[]{id});
        }
    }

    public void showAllGroups() {
        ContentResolver cr = _activity.getContentResolver();

        Cursor c = null;
        List<String> groupIds = new ArrayList<String>();
        try {
            c = cr.query(ContactsContract.Groups.CONTENT_URI, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex(ContactsContract.Groups._ID));
                    groupIds.add(id);
                }
            }
        } finally {
            if (c != null) c.close();
        }
        for (String id : groupIds) {
            ContentValues groupValues = new ContentValues();
            groupValues.put(ContactsContract.Groups.DELETED, SHOW);
            cr.update(ContactsContract.Groups.CONTENT_URI, groupValues,
                    ContactsContract.Groups._ID + "=?", new String[]{id});
        }
    }

    public void deleteContactFromAllGroups(int contactId) {
        int rawContactId = ContactDAO.getInstance(_activity).getRawContactId(contactId);
        deleteRawContactFromAllGroups(rawContactId);
    }

    private void deleteRawContactFromAllGroups(int rawContactId) {
        _activity.getContentResolver().delete(ContactsContract.Data.CONTENT_URI,                ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID + "=? AND "
                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "=?",
                new String[]{String.valueOf(rawContactId),
                        ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE});
    }

    public void addToGroup(int personId, int groupId) {

        int rawContactId = ContactDAO.getInstance(_activity).getRawContactId(personId);

        deleteRawContactFromAllGroups(rawContactId);

        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, groupId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);

         _activity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
    }




}