package ua.kpi.sc.model;

/**
 * Created by Smolin on 14.12.13.
 */
public class Sms {
    private String _id;
    private String _address;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private FolderType _folderType;
    private String _person;

    public String getId() {
        return _id;
    }

    public String getAddress() {
        return _address;
    }

    public String getMsg() {
        return _msg;
    }

    public String getReadState() {
        return _readState;
    }

    public String getTime() {
        return _time;
    }

    public String getFolderName() {
        return _folderType.toString();
    }

    public FolderType getFolderType() {
        return _folderType;
    }

    public void setId(String id) {
        _id = id;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public void setMsg(String msg) {
        _msg = msg;
    }

    public void setReadState(String readState) {
        _readState = readState;
    }

    public void setTime(String time) {
        _time = time;
    }

    public void setFolderName(FolderType folderType) {
        _folderType = folderType;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "_id='" + _id + '\'' +
                ", _address='" + _address + '\'' +
                ", _msg='" + _msg + '\'' +
                ", _readState='" + _readState + '\'' +
                ", _time='" + _time + '\'' +
                ", _folderName='" + _folderType.toString() + '\'' +
                ", _person='" + _person + '\'' +
                '}';
    }

    public String getPerson() {
        return _person;
    }

    public void setPerson(String _person) {
        this._person = _person;
    }
}