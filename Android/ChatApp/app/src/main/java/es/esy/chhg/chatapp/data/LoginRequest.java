package es.esy.chhg.chatapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest {
    final String FIELD_USERNAME = "username";
    final String FIELD_PASSWORD = "password";

    private String mUsername;
    private String mPassword;

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(FIELD_USERNAME, mUsername);
            jsonObject.put(FIELD_PASSWORD, mPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }
}