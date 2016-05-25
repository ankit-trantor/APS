package threads;

import es.esy.chhg.chatapp.data.SignInRequest;
import es.esy.chhg.chatapp.data.SignInResponse;
import utils.NetworkApiUtil;
import utils.PasswordUtil;

public abstract class RegisterThread implements Runnable {

    private String mUsername;
    private String mEmail;
    private String mPassword;

    public RegisterThread(String username, String email, String password) {
        mUsername = username;
        mEmail = email;
        mPassword = PasswordUtil.hashPassword(password);
    }

    @Override
    public void run() {
        SignInRequest signInRequest;
        SignInResponse signInResponse = null;

        signInRequest = new SignInRequest();

        signInRequest.setUsername(mUsername);
        signInRequest.setEmail(mEmail);
        signInRequest.setPassword(mPassword);

        signInResponse = NetworkApiUtil.doSignIn(constants.Constants.UrlHttp.REGISTER_URL, mUsername, mEmail, mPassword);

        getSignInResponse(signInResponse);
    }

    public abstract void getSignInResponse(SignInResponse signInResponse);

}