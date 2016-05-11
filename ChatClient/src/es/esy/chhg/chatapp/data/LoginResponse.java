package es.esy.chhg.chatapp.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean mSuccess;

    @SerializedName("user_not_found")
    private boolean mUserNotFound;

    @SerializedName("user_or_password_invalid")
    private boolean mUserOrPasswordIncorrect;

    @SerializedName("user")
    private User mUser;

    public LoginResponse() {
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

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public boolean isUserNotFound() {
        return mUserNotFound;
    }

    public boolean isUserOrPasswordIncorrect() {
        return mUserOrPasswordIncorrect;
    }
}