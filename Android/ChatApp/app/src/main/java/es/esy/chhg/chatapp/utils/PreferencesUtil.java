package es.esy.chhg.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import es.esy.chhg.chatapp.data.User;

public class PreferencesUtil {
    public static final String USER = "user";

    public static void saveLoginUser(Context context, String jsonUser) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(USER, jsonUser).apply();
    }

    public static boolean isUserConnected(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String user = sharedPreferences.getString(USER, null);

        return user != null;
    }

    public static User getUserConnected(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonUser = sharedPreferences.getString(USER, null);
        return new User().fromJson(jsonUser);
    }
}