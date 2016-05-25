package utils;

import com.google.gson.Gson;
import es.esy.chhg.chatapp.data.LoginRequest;
import es.esy.chhg.chatapp.data.LoginResponse;
import es.esy.chhg.chatapp.data.SignInRequest;
import es.esy.chhg.chatapp.data.SignInResponse;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.naming.Context;

public class NetworkApiUtil {

    public static final int CONNECT_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 10000;


    public static LoginResponse doLogin(String apiUrl, String username, String password) {
        LoginResponse response = null;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        String responseRequest = doJsonPostRequest(apiUrl, new Gson().toJson(loginRequest));
        if (responseRequest != null && !responseRequest.isEmpty()) {
            response = new Gson().fromJson(responseRequest, LoginResponse.class);
        }
        return response;
    }

    public static SignInResponse doSignIn(String apiUrl, String username, String email, String password) {
        SignInResponse response = null;
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername(username);
        signInRequest.setEmail(email);
        signInRequest.setPassword(password);
        String responseRequest = doJsonPostRequest(apiUrl, new Gson().toJson(signInRequest));
        if (responseRequest != null && !responseRequest.isEmpty()) {
            response = new Gson().fromJson(responseRequest, SignInResponse.class);
        }
        return response;
    }

    private static String doJsonPostRequest(String apiUrl, String json) {
        URL url;
        URLConnection urlConnection = null;
        DataOutputStream dataOutputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = null;
        try {
            url = new URL(apiUrl);
            urlConnection = url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            if (urlConnection instanceof HttpURLConnection) {
                ((HttpURLConnection) urlConnection).setRequestMethod("POST");
            }
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            //addLoginCredentials(context, urlConnection);
            urlConnection.connect();

            dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.write(json.getBytes("UTF-8"));
            dataOutputStream.flush();
            dataOutputStream.close();

            stringBuilder = new StringBuilder();
            inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            safeClose(dataOutputStream);
            safeClose(inputStreamReader);
            safeClose(bufferedReader);
            safeClose(urlConnection);
        }

        return stringBuilder != null ? stringBuilder.toString() : null;
    }

    private static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void safeClose(URLConnection urlConnection) {
        if (urlConnection != null) {
            if (urlConnection instanceof HttpURLConnection) {
                ((HttpURLConnection) urlConnection).disconnect();
            }
        }
    }
}