package ua.kpi.sc.dao;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

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
}
