package model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean mSuccess;

    @SerializedName("user_or_password_invalid")
    private boolean mUserOrPassworInvalid;

    @SerializedName("user_not_found")
    private boolean mUserNotFound;

    @SerializedName("user")
    private User mUser;

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        this.mSuccess = success;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public boolean isUserOrPassworInvalid() {
        return mUserOrPassworInvalid;
    }

    public void setUserOrPassworInvalid(boolean userOrPassworInvalid) {
        this.mUserOrPassworInvalid = userOrPassworInvalid;
    }

    public boolean isUserNotFound() {
        return mUserNotFound;
    }

    public void setUserNotFound(boolean userNotFound) {
        this.mUserNotFound = userNotFound;
    }
}