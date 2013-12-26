package ua.kpi.sc.model;

/**
 * Created by manilo on 19.12.13.
 */
public enum FolderType {
    INBOX, SENT;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
