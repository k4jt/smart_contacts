package ua.kpi.sc.dao;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.ContactsContract;

/**
 * Created by manilo on 20.12.13.
 */
public class Group {

    private Activity _activity;

    public Group(Activity activity) {
        _activity = activity;
    }

    public void createGroup(String groupName)
    {
        ContentResolver cr = _activity.getContentResolver();
        ContentValues groupValues = new ContentValues();
        groupValues.put(ContactsContract.Groups.TITLE, groupName);
        groupValues.put(ContactsContract.Groups.GROUP_VISIBLE, 1);

        cr.insert(ContactsContract.Groups.CONTENT_URI, groupValues);
    }
}
