package es.esy.chhg.chatapp.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {
    public static String getTime(Long time, Context context) {
        SimpleDateFormat simpleDateFormat;
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        }
        return simpleDateFormat.format(time);
    }
}
