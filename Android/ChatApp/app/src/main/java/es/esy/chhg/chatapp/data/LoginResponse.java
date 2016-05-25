package es.esy.chhg.chatapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponse {
    private final String FIELD_SUCCESS = "success";
    private final String FILED_USER = "user";
    private final String FIELD_USER_NOT_FOUND = "user_not_found";
    private final String FIELD_USER_OR_PASSWORD_INVALID = "user_or_password_invalid";


    private boolean mSuccess;
    private boolean mUserNotFound;
    private boolean mUserOrPasswordIncorrect;
    private User mUser;

    public LoginResponse() {
    }

    public LoginResponse fromJson(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.has(FIELD_SUCCESS)) {
                mSuccess = jsonObject.getBoolean(FIELD_SUCCESS);
            }

            if (jsonObject.has(FIELD_USER_NOT_FOUND)) {
                mUserNotFound = jsonObject.getBoolean(FIELD_USER_NOT_FOUND);
            }

            if (jsonObject.has(FIELD_USER_OR_PASSWORD_INVALID)) {
                mUserOrPasswordIncorrect = jsonObject.getBoolean(FIELD_USER_OR_PASSWORD_INVALID);
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