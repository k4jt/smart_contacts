package ua.kpi.sc.model;

import java.util.Date;

/**
 * Created by manilo on 25.11.13.
 */
public class Statistics {

    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;
    private static final double MILLIS_IN_SECOND = 1000.0;
    private static final double MILLIS_IN_DAY = MILLIS_IN_SECOND * SECONDS_IN_MINUTE
                                                * MINUTES_IN_HOUR * HOURS_IN_DAY;


    private Date theMostRecent;
    private Date theMostPassed;
    private int totalCount;
    private long totalAmount;

    public double getFrequency() {
        double days = convertMillisecondsToDays(theMostRecent.getTime() - theMostPassed.getTime());
        if (days == 0.0f) return 0.0f;
        return totalCount / days;
    }

    public double getAverageAmount() {
        if (totalCount == 0) return 0;
        return ((double)totalAmount) / totalCount;
    }

    private double convertMillisecondsToDays(long timeInMillis) {
        return timeInMillis / MILLIS_IN_DAY;
    }

    public void addInfo(Date date, int amount) {
        if (amount != 0) {
            ++totalCount;
            totalAmount += amount;

            setTheMostPassedDate(date);
            setTheMostRecentDate(date);
        }
    }

    private void setTheMostRecentDate(Date date) {
        if (theMostRecent == null) {
            theMostRecent = date;
        } else if (theMostRecent.before(date)) {
            theMostRecent = date;
        }
    }

    private void setTheMostPassedDate(Date date) {
        if (theMostPassed == null) {
            theMostPassed = date;
        } else if (theMostPassed.after(date)) {
            theMostPassed = date;
        }
    }

}
