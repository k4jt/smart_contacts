package ua.kpi.sc.sms;

/**
 * Created by Smolin on 15.12.13.
 */
public class SmsPersonStatistics {

    /**
     * number of symbols of all sms recieved (total inbox sms length)
     */
    public int inboxTotalSymbols;
    /**
     * Number of recieved sms (total inbox sms count)
     */
    public int inboxSmsCount;

    /**
     * the same as {@link ua.kpi.sc.sms.SmsPersonStatistics#inboxTotalSymbols} but for
     * sent sms (outbox)
     */
    public int sentTotalSymbols;

    /**
     * the same as {@link ua.kpi.sc.sms.SmsPersonStatistics#inboxSmsCount} but for
     * sent sms (outbox)
     */
    public int sentSmsCount;

    @Override
    public String toString() {
        return "SmsPersonStatistics{" +
                "inboxTotalSymbols=" + inboxTotalSymbols +
                ", inboxSmsCount=" + inboxSmsCount +
                ", sentTotalSymbols=" + sentTotalSymbols +
                ", sentSmsCount=" + sentSmsCount +
                '}';
    }
}
