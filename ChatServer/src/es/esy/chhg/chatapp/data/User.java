package es.esy.chhg.chatapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private final String FIELD_ID = "id_user";
    private final String FIELD_USERNAME = "username";
    private final String FIELD_EMAIL = "email";

    private long mId;
    private String mUsername;
    private String mEmail;

    public User fromJson(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            fromJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public User fromJson(JSONObject jsonObject) {
        try {
            if (jsonObject.has(FIELD_ID)) {
                mId = jsonObject.getLong(FIELD_ID);
            }
            if (jsonObject.has(FIELD_USERNAME) && !jsonObject.isNull(FIELD_USERNAME)) {
                mUsername = jsonObject.getString(FIELD_USERNAME);
            }
            if (jsonObject.has(FIELD_EMAIL) && !jsonObject.isNull(FIELD_EMAIL)) {
                mEmail = jsonObject.getString(FIELD_EMAIL);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(FIELD_ID, mId);
            jsonObject.put(FIELD_USERNAME, mUsername == null ? JSONObject.NULL : mUsername);
            jsonObject.put(FIELD_EMAIL, mEmail == null ? JSONObject.NULL : mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

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