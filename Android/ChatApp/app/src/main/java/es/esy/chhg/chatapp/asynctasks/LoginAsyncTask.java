package es.esy.chhg.chatapp.asynctasks;

import android.content.Context;

import java.lang.ref.WeakReference;

import es.esy.chhg.chatapp.constants.Constants;
import es.esy.chhg.chatapp.data.LoginResponse;
import es.esy.chhg.chatapp.utils.NetworkApiUtil;

public class LoginAsyncTask extends BaseAsyncTask<Void, Void, LoginResponse> {
    private String mApiUrl;
    private String mUserName;
    private String mPassword;
    private WeakReference<Context> mContextReference;

    public LoginAsyncTask(Context context, String username, String password, AsyncTaskListener<LoginResponse> listener) {
        super(listener);
        mContextReference = new WeakReference<Context>(context);
        mUserName = username;
        mPassword = password;
        mApiUrl = "http://" + Constants.SocketConstants.ADDRESS_IP + ":8080/chat/login.jsp";
    }

    @Override
    public boolean isSuccess(LoginResponse loginResponse) {
        return loginResponse != null;
    }

    @Override
    protected LoginResponse doInBackground(Void... params) {
        return NetworkApiUtil.doLogin(mContextReference.get(), mApiUrl, mUserName, mPassword);
    }
}
