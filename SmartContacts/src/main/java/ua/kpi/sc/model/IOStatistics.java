package ua.kpi.sc.model;

/**
 * Created by manilo on 19.12.13.
 */
public class IOStatistics {

    private Statistics input, output;

    {
        input = new Statistics();
        output = new Statistics();
    }

    public void addInbox(String date, int totalAmount) {
        input.addInfo(date, totalAmount);
    }

    public void addSent(String date, int totalAmount) {
        output.addInfo(date, totalAmount);
    }

    /**
     *
     * @param totalAmount - call duration or sms size for example
     * @param ioType - TRUE is Inbox, FALSE is Sent
     */
    public void addInfo(String date, int totalAmount, boolean ioType) {
        if (ioType)
            input.addInfo(date, totalAmount);
        else
            output.addInfo(date, totalAmount);
    }

    /**
     * number of symbols/total call duration/ of all sms/calls recieved (total input sms length)
     */
    public long getTotalInputAmount() {
        return input.getTotalAmount();
    }

    /**
     * Number of recieved sms/calls (total input sms/calls count)
     */
    public int getTotalInputCount() {
        return input.getTotalCount();
    }

    /**
     * the same as {@link PersonStatistics#inboxTotalSymbols} but for
     * output sms/calls (outbox)
     */
    public long getTotalOutputAmount() {
        return output.getTotalAmount();
    }

    /**
     * the same as {@link PersonStatistics#inboxSmsCount} but for
     * output sms (outbox)
     */
    public int getTotalOutputCount() {
        return output.getTotalCount();
    }

}
