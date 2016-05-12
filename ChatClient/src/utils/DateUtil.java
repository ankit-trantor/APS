package utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.naming.Context;

public class DateUtil {

    public static String getTime(Long time, Context context) {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return simpleDateFormat.format(time);
    }
}