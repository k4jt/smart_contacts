package ua.kpi.sc.model;

/**
 * Created by Smolin on 15.12.13.
 */
public class PersonStatistics {

    /**
     * number of symbols of all sms recieved (total inbox sms length)
     */
    public int inboxTotalSymbols;
    /**
     * Number of recieved sms (total inbox sms count)
     */
    public int inboxSmsCount;

    /**
     * the same as {@link PersonStatistics#inboxTotalSymbols} but for
     * sent sms (outbox)
     */
    public int sentTotalSymbols;

    /**
     * the same as {@link PersonStatistics#inboxSmsCount} but for
     * sent sms (outbox)
     */
    public int sentSmsCount;

    @Override
    public String toString() {
        return "PersonStatistics{" +
                "inboxTotalSymbols=" + inboxTotalSymbols +
                ", inboxSmsCount=" + inboxSmsCount +
                ", sentTotalSymbols=" + sentTotalSymbols +
                ", sentSmsCount=" + sentSmsCount +
                '}';
    }
}
