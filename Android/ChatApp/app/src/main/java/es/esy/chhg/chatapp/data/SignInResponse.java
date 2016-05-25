package es.esy.chhg.chatapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInResponse {
    private final String FIELD_SUCCESS = "success";
    private final String FILED_USER = "user";
    private final String FIELD_USER_EXIST = "username_exist";
    private final String FIELD_EMAIL_EXIST = "email_exist";

    private boolean mSuccess;
    private boolean mUserExist;
    private boolean mEmailExist;
    private User mUser;

    public SignInResponse() {

    }

    public SignInResponse fromJson(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.has(FIELD_SUCCESS)) {
                mSuccess = jsonObject.getBoolean(FIELD_SUCCESS);
            }

            if (jsonObject.has(FIELD_USER_EXIST)) {
                mUserExist = jsonObject.getBoolean(FIELD_USER_EXIST);
            }

            if (jsonObject.has(FIELD_EMAIL_EXIST)) {
                mEmailExist = jsonObject.getBoolean(FIELD_EMAIL_EXIST);
            }
            if (jsonObject.has(FILED_USER)) {
                mUser = new User().fromJson(jsonObject.getJSONObject(FILED_USER));
            } else {
                mUser = new User();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
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
        return mUserExist;
    }

    public void setUserExist(boolean userExist) {
        mUserExist = userExist;
    }

    public boolean isEmailExist() {
        return mEmailExist;
    }
}