package threads;

import es.esy.chhg.chatapp.data.LoginRequest;
import es.esy.chhg.chatapp.data.LoginResponse;
import utils.NetworkApiUtil;
import utils.PasswordUtil;

public abstract class LoginThread implements Runnable {

    private String mUsername;
    private String mPassword;

    public LoginThread(String username, String password) {
        mUsername = username;
        mPassword = PasswordUtil.hashPassword(password); // Criptografa a senha
    }

    @Override
    public void run() {
        LoginRequest loginRequest;
        LoginResponse loginResponse;

        loginRequest = new LoginRequest();

        loginRequest.setUsername(mUsername);
        loginRequest.setPassword(mPassword);

        loginResponse = NetworkApiUtil.doLogin(constants.Constants.UrlHttp.LOGIN_URL, mUsername, mPassword);

        getLoginResponse(loginResponse);
    }

    public abstract void getLoginResponse(LoginResponse loginResponse);
}