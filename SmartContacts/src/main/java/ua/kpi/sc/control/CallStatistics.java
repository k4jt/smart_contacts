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

import ua.kpi.sc.model.Statistics;

/**
 * Created by manilo on 24.11.13.
 */
public class CallStatistics {

    private Activity activity;
    private static String columns[] = {
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE};

    public CallStatistics(Activity activity) {
        this.activity = activity;
    }


    public Map<Long, Statistics> getStatistics() {

        /*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
        CursorLoader callCursorLoader = new CursorLoader(activity.getApplicationContext(),
                CallLog.Calls.CONTENT_URI, columns, null, null, "Calls._ID DESC");
        */

        Cursor callCursor = activity.managedQuery(CallLog.Calls.CONTENT_URI, columns, null, null, "Calls._ID DESC");

        int number = callCursor.getColumnIndex(CallLog.Calls.NUMBER);
        //int type = callCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = callCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = callCursor.getColumnIndex(CallLog.Calls.DURATION);

        Map<Long, Statistics> statistics = new HashMap<Long, Statistics>();

        while (callCursor.moveToNext()) {
            long phNumber = Long.parseLong(callCursor.getString(number));
            //String callType = callCursor.getString(type);
            Date callDayTime = new Date(Long.valueOf(callCursor.getString(date)));
            int callDuration = Integer.parseInt(callCursor.getString(duration));

            if (statistics.containsKey(phNumber)) {
                statistics.get(phNumber).addInfo(callDayTime, callDuration);
            } else {
                Statistics info = new Statistics();
                info.addInfo(callDayTime, callDuration);
                statistics.put(phNumber, info);
            }
        }

        return statistics;
    }




}
