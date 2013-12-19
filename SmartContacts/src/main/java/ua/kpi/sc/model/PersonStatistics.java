package ua.kpi.sc.model;

/**
 * Created by Smolin on 15.12.13.
 */
public class PersonStatistics {

    /**
     * number of symbols of all sms received (total inbox sms length)
     */
    public long inboxTotalSymbols;
    /**
     * Number of received sms (total inbox sms count)
     */
    public int inboxSmsCount;

    /**
     * the same as {@link PersonStatistics#inboxTotalSymbols} but for
     * sent sms (outbox)
     */
    public long sentTotalSymbols;

    /**
     * the same as {@link PersonStatistics#inboxSmsCount} but for
     * sent sms (outbox)
     */
    public int sentSmsCount;

    /**
     * duration of all received calls
     */
    public long incomingTotalDuration;
    /**
     * Number of received calls
     */
    public int incomingCallsCount;

    /**
     * the same as {@link PersonStatistics#incomingTotalDuration} but for
     * outgoing calls
     */
    public long outgoingTotalDuration;

    /**
     * the same as {@link PersonStatistics#incomingCallsCount} but for
     * outgoing calls
     */
    public int outgoingCallsCount;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("PersonStatistics{");
        sb.append(", inboxTotalSymbols=").append(inboxTotalSymbols);
        sb.append(", inboxSmsCount=").append(inboxSmsCount);
        sb.append(", sentTotalSymbols=").append(sentTotalSymbols);
        sb.append(", sentSmsCount=").append(sentSmsCount);
        sb.append(", incomingTotalDuration=").append(incomingTotalDuration);
        sb.append(", incomingCallsCount=").append(incomingCallsCount);
        sb.append(", outgoingTotalDuration=").append(outgoingTotalDuration);
        sb.append(", outgoingCallsCount=").append(outgoingCallsCount);
        sb.append("}");

        return sb.toString();
    }
}
