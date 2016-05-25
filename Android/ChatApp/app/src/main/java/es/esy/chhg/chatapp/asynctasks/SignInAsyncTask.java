package es.esy.chhg.chatapp.asynctasks;

import android.content.Context;

import java.lang.ref.WeakReference;

import es.esy.chhg.chatapp.constants.Constants;
import es.esy.chhg.chatapp.data.SignInResponse;
import es.esy.chhg.chatapp.utils.NetworkApiUtil;

public class SignInAsyncTask extends BaseAsyncTask<Void, Void, SignInResponse> {
    private String mApiUrl;
    private String mUserName;
    private String mEmail;
    private String mPassword;
    private WeakReference<Context> mContextReference;

    public SignInAsyncTask(Context context, String username, String email, String password, AsyncTaskListener<SignInResponse> listener) {
        super(listener);
        mContextReference = new WeakReference<Context>(context);
        mUserName = username;
        mEmail = email;
        mPassword = password;
        mApiUrl = "http://" + Constants.SocketConstants.ADDRESS_IP + ":8080/chat/signIn.jsp";
    }

    @Override
    public boolean isSuccess(SignInResponse signInResponse) {
        return signInResponse != null;
    }

    @Override
    protected SignInResponse doInBackground(Void... params) {
        return NetworkApiUtil.doSignIn(mContextReference.get(), mApiUrl, mUserName, mEmail, mPassword);
    }
}