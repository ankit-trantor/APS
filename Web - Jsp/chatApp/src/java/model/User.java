package model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id_user")
    private long mIdUser;

    @SerializedName("username")
    private String mUsername;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    public long getIdUser() {
        return mIdUser;
    }

    public void setIdUser(long idUser) {
        this.mIdUser = idUser;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }
}