package es.esy.chhg.chatapp.data;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {

    @SerializedName("success")
    private boolean mSuccess;

    @SerializedName("user_not_exist")
    private boolean mUserNotExist;

    @SerializedName("user")
    private User mUser;

    public SignInResponse() {

    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public boolean isUserExist() {
        return mUserNotExist;
    }

    public void setUserExist(boolean userExist) {
        mUserNotExist = userExist;
    }
}