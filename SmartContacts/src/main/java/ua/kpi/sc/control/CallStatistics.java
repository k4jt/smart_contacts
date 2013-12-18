package ua.kpi.sc.control;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ua.kpi.sc.dao.ContactDAO;
import ua.kpi.sc.model.Statistics;

/**
 * Created by manilo on 24.11.13.
 */
public class CallStatistics {

    private ContactDAO contactDAO;
    private Activity activity;
    private static String columns[] = {
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE};

    private int phoneNumberColumnIndx;
    private int dateColumnIndx;
    private int callDurationColumnIndx;
    private int typeColumnIndx;

    private static final int PRIORITY_FACTOR = 3;

    public CallStatistics(Activity activity) {
        this.activity = activity;
        contactDAO = ContactDAO.getInstance(activity);
    }

    private void addStatisticsFromCursor(Cursor callCursor, Map<String, Statistics> statistics) {
        String phNumber = callCursor.getString(phoneNumberColumnIndx);
        int callDuration = Integer.parseInt(callCursor.getString(callDurationColumnIndx));
        Date callDayTime = new Date(Long.parseLong(callCursor.getString(dateColumnIndx)));
        String contacId = contactDAO.getContactId(phNumber);
        int type = Integer.parseInt(callCursor.getString(typeColumnIndx));

        if (type == CallLog.Calls.OUTGOING_TYPE) callDuration *= PRIORITY_FACTOR;

        if (contacId != null && statistics.containsKey(contacId)) {
            statistics.get(contacId).addInfo(callDayTime, callDuration);
        } else if (contacId != null && callDuration > 0) {
            Statistics info = new Statistics();
            info.addInfo(callDayTime, callDuration);
            statistics.put(contacId, info);
        }
    }

    public Map<String, Statistics> getStatistics() {
        Map<String, Statistics> statistics = new HashMap<String, Statistics>();

        Cursor callCursor = null;

        try {
            callCursor  = activity.managedQuery(CallLog.Calls.CONTENT_URI, columns, null, null, null);

            if (callCursor != null && callCursor.getCount() > 0 /*&& callCursor.moveToFirst()*/) {

                phoneNumberColumnIndx = callCursor.getColumnIndex(CallLog.Calls.NUMBER);
                dateColumnIndx = callCursor.getColumnIndex(CallLog.Calls.DATE);
                callDurationColumnIndx = callCursor.getColumnIndex(CallLog.Calls.DURATION);
                typeColumnIndx = callCursor.getColumnIndex(CallLog.Calls.TYPE);

                //addStatisticsFromCursor(callCursor, statistics);
                while (callCursor.moveToNext()) {
                    addStatisticsFromCursor(callCursor, statistics);
                }
            }

        } finally {
            if (callCursor != null) callCursor.close();
        }

        return statistics;
    }




}
