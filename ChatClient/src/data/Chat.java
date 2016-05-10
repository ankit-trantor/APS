package data;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Chat implements Serializable {

    public enum Action {

        Connect, Disconnect, SendOne, SendAll, UsersOnline, File
    }

    private String mNameUser;
    private String mMessage;
    private File mFile;
    private String mNameReserved;
    private long mTimeMessage;
    private Set<String> mUsersOnlines = new HashSet<>();
    private Action mAction;

    public String getNameUser() {
        return mNameUser;
    }

    public void setNameUser(String nameUser) {
        mNameUser = nameUser;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public String getNameReserved() {
        return mNameReserved;
    }

    public void setNameReserved(String nameReserved) {
        mNameReserved = nameReserved;
    }

    public long getTimeMessage() {
        return mTimeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        mTimeMessage = timeMessage;
    }

    public Set<String> getUsersOnlines() {
        return mUsersOnlines;
    }

    public void setUsersOnlines(Set<String> usersOnlines) {
        mUsersOnlines = usersOnlines;
    }

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }
}