package es.esy.chhg.chatapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInRequest {
    private final String FIELD_USERNAME = "username";
    private final String FIELD_EMAIL = "email";
    private final String FIELD_PASSWORD = "password";

    private String mUsername;
    private String mEmail;
    private String mPassword;

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(FIELD_USERNAME, mUsername);
            jsonObject.put(FIELD_EMAIL, mEmail);
            jsonObject.put(FIELD_PASSWORD, mPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
}