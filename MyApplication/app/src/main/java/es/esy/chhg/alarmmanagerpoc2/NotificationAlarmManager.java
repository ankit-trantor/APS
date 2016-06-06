package es.esy.chhg.alarmmanagerpoc2;

import android.app.AlarmManager;
import android.content.Context;

import java.util.Calendar;

public class NotificationAlarmManager {
    private static final int ALARM_NOTIFICATION_ID = 5;

    private static long getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);

        return calendar.getTimeInMillis();
    }

    public static void startNotificationAlarm(Context context) {
        long intervalAlarm = AlarmManager.INTERVAL_HOUR * 2; // 2 horas

        AlarmUtil.scheduleAlarmWithRepeat(
                context, ALARM_NOTIFICATION_ID,
                NotificationService.getIntentNotificationService(context),
                getTime(),
                intervalAlarm
        );
    }
}