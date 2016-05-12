package es.esy.chhg.chatapp.data;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id_user")
    private long mId;

    @SerializedName("username")
    private String mUsername;

    @SerializedName("email")
    private String mEmail;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
}