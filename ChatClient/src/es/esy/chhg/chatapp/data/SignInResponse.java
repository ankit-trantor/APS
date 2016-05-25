package es.esy.chhg.chatapp.data;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {

    @SerializedName("success")
    private boolean mSuccess;

    @SerializedName("email_exist")
    private boolean mEmailExist;

    @SerializedName("username_exist")
    private boolean mUsernameExist;

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

    public boolean isEmailExist() {
        return mEmailExist;
    }

    public void setEmailExist(boolean emailExist) {
        mEmailExist = emailExist;
    }

    public boolean isUsernameExist() {
        return mUsernameExist;
    }

    public void setUsernameExist(boolean usernameExist) {
        mUsernameExist = usernameExist;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}