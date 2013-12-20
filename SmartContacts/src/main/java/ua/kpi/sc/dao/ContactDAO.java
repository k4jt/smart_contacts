package ua.kpi.sc.dao;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manilo on 26.11.13.
 */
public class ContactDAO {

    private SQLiteDatabase database;
    private ContactSQLiteHelper contactHelper;
    private Map<String, String> cache;
    private Map<Integer, Integer> cacheContactIdToRawContactId;

    private static Activity activity;
    private static ContactDAO instance;
    private boolean finishLoading = false;

    public static ContactDAO getInstance(Activity inputActivity) {
        activity = inputActivity;
        if (instance == null) {
            instance = new ContactDAO(inputActivity);
        }

        return instance;
    }

    private ContactDAO(Activity activity) {
        contactHelper = new ContactSQLiteHelper(activity.getApplicationContext());
        this.activity = activity;
        cache = new HashMap<String, String>();
        cacheContactIdToRawContactId = new HashMap<Integer, Integer>();
        loadPhones();
    }

    public void open() throws SQLiteException {
        database = contactHelper.getWritableDatabase();
    }

    public void close() {
        contactHelper.close();
    }

    private void loadPhonesById(String contactId) {
        Cursor cursorPhone = null;
        try {
            // Using the contact ID now we will get contact phone number
            cursorPhone = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                    new String[]{contactId},
                    null);

            if (cursorPhone != null && cursorPhone.getCount() > 0 /*&& cursorPhone.moveToFirst()*/) {
                /*String phoneNumber = cursorPhone.getString(0);
                cache.put(phoneNumber, contactId);*/
                while(cursorPhone.moveToNext()) {
                    String phoneNumber = cursorPhone.getString(0);
                    cache.put(normalizePhoneNumber(phoneNumber), contactId);
                }
            }

        } finally {
            if (cursorPhone != null) cursorPhone.close();
        }
    }

    private void loadPhones() {

        Cursor cursor = null;
        try {
            open();
            cursor = activity.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.CONTACT_ID},
                    null, null, null);

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                String contactId = cursor.getString(0);
                loadPhonesById(contactId);
                while(cursor.moveToNext()) {
                    contactId = cursor.getString(0);
                    loadPhonesById(contactId);
                }
            }

        } finally {
            if (cursor != null) cursor.close();
            close();
        }

        finishLoading = true;
    }

    public static String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber.charAt(0) == '+')
            return phoneNumber.substring(3);
        else
            return phoneNumber;
    }

    public String getContactId(String phoneNumber) {
       String result = null;
       String key = normalizePhoneNumber(phoneNumber);
       if (cache.containsKey(key)) {
            result = cache.get(key);
       }

       return result;
    }

    public boolean isFinishLoading() {
        return finishLoading;
    }

    public int getRawContactId(int contactId) {
        Integer rawContactId = cacheContactIdToRawContactId.get(contactId);
        if (rawContactId == null) {
            String[] projection=new String[]{ContactsContract.RawContacts._ID};
            String selection=ContactsContract.RawContacts.CONTACT_ID+"=?";
            String[] selectionArgs=new String[]{String.valueOf(contactId)};
            Cursor c = activity.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,projection,selection,selectionArgs , null);
            if (c != null && c.moveToFirst()) {
                rawContactId = c.getInt(c.getColumnIndex(ContactsContract.RawContacts._ID));
                c.close();
            }

            if (rawContactId != null) {
                cacheContactIdToRawContactId.put(contactId, rawContactId);
                return rawContactId;
            }
        }

        return 0;
    }

}
