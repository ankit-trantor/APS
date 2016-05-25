package es.esy.chhg.chatapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import es.esy.chhg.chatapp.utils.MimeTypeUtil;

public class Chat implements Serializable, Parcelable {

    private static final long serialVersionUID = 1l;

    public Chat(){

    }

    protected Chat(Parcel in) {
        mNameUser = in.readString();
        mMessage = in.readString();
        mFileByte = in.createByteArray();
        mNameReserved = in.readString();
        mTimeMessage = in.readLong();
        mMimeType = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNameUser);
        dest.writeString(mMessage);
        dest.writeByteArray(mFileByte);
        dest.writeString(mNameReserved);
        dest.writeLong(mTimeMessage);
        dest.writeString(mMimeType);
    }

    public enum Action {

        Connect, Disconnect, SendOne, SendAll, UsersOnline, File
    }

    private String mNameUser;
    private String mMessage;
    private byte[] mFileByte;
    private String mNameReserved;
    private long mTimeMessage;
    private Set<String> mUsersOnline = new HashSet<>();
    private Action mAction;
    private String mMimeType; // Tipo do arquivo (jpg, etc...)

    private transient File mFile;
    private transient int mSoundTime;

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

    public byte[] getFileByte() {
        return mFileByte;
    }

    public void setFileByte(byte[] fileByte) {
        mFileByte = fileByte;
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
        return mUsersOnline;
    }

    public void setUsersOnlines(Set<String> usersOnlines) {
        mUsersOnline = usersOnlines;
    }

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
        mMimeType = MimeTypeUtil.getMimeType(file.getAbsolutePath());
    }

    public boolean isImage() {
        return mMimeType.startsWith("image");
    }

    public boolean isVideo() {
        return mMimeType.startsWith("video");
    }

    public boolean isAudio() {
        return mMimeType.startsWith("audio");
    }

    public int getSoundTime() {
        return mSoundTime;
    }

    public void setSoundTime(int soundTime) {
        mSoundTime = soundTime;
    }

    public String getMimeType() {
        return mMimeType;
    }
}