package ua.kpi.sc.test.model;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.Date;

import ua.kpi.sc.model.Statistics;

/**
 * Created by manilo on 29.11.13.
 */
public class StatisticsTest extends AndroidTestCase {

    private Statistics statistics;

    public void setUp() {
        long dayInMillis = 1000 * 3600 * 24;

        Date today = new Date();
        Date yesterday = new Date(today.getTime() - dayInMillis);
        Date tomorrow = new Date(today.getTime() + dayInMillis);

        statistics = new Statistics();
        statistics.addInfo(yesterday, 2);
        statistics.addInfo(today, 4);
        statistics.addInfo(tomorrow, 6);
    }

    public void tearDown() {
        statistics = null;
    }

    public void testFrequency() {
        Assert.assertEquals(1.5, statistics.getFrequency());
    }

    public void testAverageAmount() {
        Assert.assertEquals(4, (int)statistics.getAverageAmount());
    }

}
