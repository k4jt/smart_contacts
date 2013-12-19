package ua.kpi.sc.control;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.kpi.sc.dao.ContactDAO;
import ua.kpi.sc.model.FolderType;
import ua.kpi.sc.model.IOStatistics;
import ua.kpi.sc.model.PersonStatistics;
import ua.kpi.sc.model.Sms;

import static android.util.Log.d;


/**
 * Created by Smolin on 14.12.13.
 */
public class StatisticsCollector {

    private ContactDAO contactDAO;
    private Activity activity;
    private static String callColumns[] = {
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE};

    public StatisticsCollector(Activity activity) {
        this.activity = activity;
        contactDAO = ContactDAO.getInstance(activity);

        // check if phones loading completed
        while (!contactDAO.isFinishLoading()) try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Map<String, IOStatistics> collectCallStatistics() {
        Map<String, IOStatistics> statistics = new HashMap<String, IOStatistics>();

        Cursor callCursor = null;

        try {
            callCursor  = activity.managedQuery(CallLog.Calls.CONTENT_URI, callColumns, null, null, null);

            if (callCursor != null && callCursor.getCount() > 0 /*&& callCursor.moveToFirst()*/) {

               int phoneNumberColumnIndex = callCursor.getColumnIndex(CallLog.Calls.NUMBER);
               int dateColumnIndex = callCursor.getColumnIndex(CallLog.Calls.DATE);
               int callDurationColumnIndex = callCursor.getColumnIndex(CallLog.Calls.DURATION);
               int typeColumnIndex = callCursor.getColumnIndex(CallLog.Calls.TYPE);

                while (callCursor.moveToNext()) {

                    String contactId = contactDAO.getContactId(callCursor.getString(phoneNumberColumnIndex));
                    //we need to store calls only for existing contacts
                    if (contactId == null) continue;

                    int callDuration = Integer.parseInt(callCursor.getString(callDurationColumnIndex));
                    //we need to store meaningful calls
                    if (callDuration == 0) continue;

                    String callDayTime = callCursor.getString(dateColumnIndex);
                    int callType = Integer.parseInt(callCursor.getString(typeColumnIndex));

                    IOStatistics callStat = statistics.get(contactId);
                    if (callStat == null) {
                        callStat = new IOStatistics();
                        statistics.put(contactId, callStat);
                    }

                    callStat.addInfo(callDayTime, callDuration, (callType == CallLog.Calls.INCOMING_TYPE));
                }
            }

        } finally {
            if (callCursor != null) callCursor.close();
        }

        return statistics;
    }

    private List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();

        Cursor c = null;

        try {
            c = cr.query(message, null, null, null, null);
            if (c != null) {
                //activity.startManagingCursor(c);
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

    private Map<String, IOStatistics> collectSmsStatistics() {
        List<Sms> lstSms = getAllSms();

        //contactId --> smsStatistics
        Map<String, IOStatistics> smsStatistics = new HashMap<String, IOStatistics>();

        for (Sms sms : lstSms) {
            String contactId = contactDAO.getContactId(sms.getAddress());

            if (contactId == null) continue;

            IOStatistics smsStat = smsStatistics.get(contactId);
            if (smsStat == null) {
                smsStat = new IOStatistics();
                smsStatistics.put(contactId, smsStat);
            }

            smsStat.addInfo(sms.getTime(), sms.getMsg().length(), (sms.getFolderType() == FolderType.INBOX));
        }

        return smsStatistics;
    }

    public Map<String, PersonStatistics> getStatistics() {
        StatisticsThread smsThread = new StatisticsThread(StatisticsType.SMS);
        smsThread.start();
        StatisticsThread callThread = new StatisticsThread(StatisticsType.CALL);
        callThread.start();

        if (smsThread.isAlive()) {
            try {
                smsThread.join();
            } catch (InterruptedException e) {}
        }

        if (callThread.isAlive()) {
            try {
                callThread.join();
            } catch (InterruptedException e) {}
        }

        return merge(smsThread.statistics, callThread.statistics);
    }

    private Map<String, PersonStatistics> merge(Map<String, IOStatistics> smsStatistics, Map<String, IOStatistics> callStatistics) {
        Map<String, PersonStatistics> statistics = new HashMap<String, PersonStatistics>();

        if (smsStatistics.size() >= callStatistics.size()) {
            for (Map.Entry smsEntry : smsStatistics.entrySet()) {
                String id = (String)smsEntry.getKey();
                IOStatistics smsData = (IOStatistics)smsEntry.getValue();
                IOStatistics callData = callStatistics.get(id);

                PersonStatistics stat = new PersonStatistics();
                statistics.put(id, stat);
                stat.inboxSmsCount = smsData.getTotalInputCount();
                stat.inboxTotalSymbols = smsData.getTotalInputAmount();
                stat.sentSmsCount = smsData.getTotalOutputCount();
                stat.sentTotalSymbols = smsData.getTotalOutputAmount();

                if (callData != null) {
                    stat.incomingCallsCount = callData.getTotalInputCount();
                    stat.incomingTotalDuration = callData.getTotalInputAmount();
                    stat.outgoingCallsCount = callData.getTotalOutputCount();
                    stat.outgoingTotalDuration = callData.getTotalOutputAmount();
                }
            }
        } else {
            for (Map.Entry callEntry : callStatistics.entrySet()) {
                String id = (String)callEntry.getKey();
                IOStatistics callData = (IOStatistics)callEntry.getValue();
                IOStatistics smsData = smsStatistics.get(id);

                PersonStatistics stat = new PersonStatistics();
                statistics.put(id, stat);
                if (smsData != null) {
                    stat.inboxSmsCount = smsData.getTotalInputCount();
                    stat.inboxTotalSymbols = smsData.getTotalInputAmount();
                    stat.sentSmsCount = smsData.getTotalOutputCount();
                    stat.sentTotalSymbols = smsData.getTotalOutputAmount();
                }

                stat.incomingCallsCount = callData.getTotalInputCount();
                stat.incomingTotalDuration = callData.getTotalInputAmount();
                stat.outgoingCallsCount = callData.getTotalOutputCount();
                stat.outgoingTotalDuration = callData.getTotalOutputAmount();
            }
        }

        return statistics;
    }

    public enum StatisticsType {
        SMS, CALL
    }

    public class StatisticsThread extends Thread {

        Map<String, IOStatistics> statistics = null;
        private StatisticsType type;

        public StatisticsThread(StatisticsType type) {
            this.type = type;
        }

        @Override
        public void run() {
            if (type == StatisticsType.SMS)
                statistics = collectSmsStatistics();
            else
                statistics = collectCallStatistics();
        }

    }


}

