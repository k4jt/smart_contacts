package ua.kpi.sc.sms;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.util.Log.d;


/**
 * Created by Smolin on 14.12.13.
 */
public class SmsUtils {
    private static final String INBOX = "inbox";
    private static final String SENT = "sent";

    public static List<Sms> getAllSms(Activity mActivity) {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = mActivity.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        mActivity.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                String person = c.getString(c.getColumnIndexOrThrow("person"));

                String folderName;
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    //we need to store sms only for existing contacts
                    if (person == null) {
                        c.moveToNext();
                        continue;
                    }
                    folderName = INBOX;
                } else {
                    folderName = SENT;
                }
                objSms = new Sms();
                objSms.setFolderName(folderName);
                objSms.setPerson(person);
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }

    public static HashMap<String, SmsPersonStatistics> collectStatistics(Activity mActivity) {
        List<Sms> lstSms = getAllSms(mActivity);

        for (Sms sms : lstSms) {
            if (sms == null) {
                d("test", "null!!!");
            } else {
                d("test", sms.toString());
            }
        }
        //tel number --> smsStatistics
        HashMap<String, SmsPersonStatistics> smsStatisticsMap = new HashMap<String, SmsPersonStatistics>();

        for (Sms sms : lstSms) {
            String tel = sms.getAddress();
            SmsPersonStatistics smsStat = smsStatisticsMap.get(tel);
            //if not exist create Entry in Map
            if (smsStat == null) {
                smsStat = new SmsPersonStatistics();
                smsStatisticsMap.put(tel, smsStat);
            }
            //if recieved
            if (sms.getFolderName() == INBOX) {
                smsStat.inboxSmsCount++;
                smsStat.inboxTotalSymbols += sms.getMsg().length();
            } else { //if sent
                smsStat.sentSmsCount++;
                smsStat.sentTotalSymbols += sms.getMsg().length();
            }
        }
        return smsStatisticsMap;
    }
}

