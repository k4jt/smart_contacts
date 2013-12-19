package ua.kpi.sc.control;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ua.kpi.sc.dao.ContactDAO;
import ua.kpi.sc.model.sms.FolderType;
import ua.kpi.sc.model.sms.Sms;
import ua.kpi.sc.model.sms.SmsStatistics;

import static android.util.Log.d;


/**
 * Created by Smolin on 14.12.13.
 */
public class StatisticsCollector {

    public static List<Sms> getAllSms(Activity mActivity) {
        List<Sms> lstSms = new ArrayList<Sms>();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = mActivity.getContentResolver();

        Cursor c = null;

        try {
            c = cr.query(message, null, null, null, null);
            if (c != null) {
                mActivity.startManagingCursor(c);
                int totalSMS = c.getCount();

                if (c.moveToFirst()) {
                    int typeColumnIndex = c.getColumnIndexOrThrow("type");
                    int personColumnIndex = c.getColumnIndexOrThrow("person");
                    int idColumnIndex = c.getColumnIndexOrThrow("_id");
                    int addressColumnIndex = c.getColumnIndexOrThrow("address");
                    int bodyColumnIndex = c.getColumnIndexOrThrow("body");
                    int readColumnIndex = c.getColumnIndex("read");
                    int dateColumnIndex = c.getColumnIndexOrThrow("date");

                    for (int i = 0; i < totalSMS; i++) {

                        String person = c.getString(personColumnIndex);
                        //we need to store sms only for existing contacts
                        if (person == null) {
                            c.moveToNext();
                            continue;
                        }

                        Sms objSms = new Sms();
                        objSms.setFolderName(c.getString(typeColumnIndex).equals("1") ? FolderType.INBOX : FolderType.SENT);
                        objSms.setPerson(person);
                        objSms.setId(c.getString(idColumnIndex));
                        objSms.setAddress(c.getString(addressColumnIndex));
                        objSms.setMsg(c.getString(bodyColumnIndex));
                        objSms.setReadState(c.getString(readColumnIndex));
                        objSms.setTime(c.getString(dateColumnIndex));

                        lstSms.add(objSms);
                        c.moveToNext();
                    }
                }
            }

        } finally {
            if (c != null) c.close();
        }

        return lstSms;
    }

    public static HashMap<String, SmsStatistics> collectStatistics(Activity mActivity) {
        List<Sms> lstSms = getAllSms(mActivity);

        //contactId --> smsStatistics
        HashMap<String, SmsStatistics> smsStatistics = new HashMap<String, SmsStatistics>();

        ContactDAO contactDAO = ContactDAO.getInstance(mActivity);
        // check if phones loading completed
        while (!contactDAO.isFinishLoading()) try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Sms sms : lstSms) {
            String phoneNumber = sms.getAddress();
            String id = contactDAO.getContactId(phoneNumber);
            SmsStatistics smsStat = smsStatistics.get(id);
            //if not exist create Entry in Map
            if (smsStat == null) {
                smsStat = new SmsStatistics();
                smsStatistics.put(id, smsStat);
            }
            //if recieved
            if (sms.getFolderType() == FolderType.INBOX) {
                smsStat.addInboxSms(sms.getTime(), sms.getMsg().length());
            } else { //if sent
                smsStat.addSentSms(sms.getTime(), sms.getMsg().length());
            }
        }

        return smsStatistics;
    }
}

