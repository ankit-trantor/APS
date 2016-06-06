package es.esy.chhg.alarmmanagerpoc2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtil {
    public static void scheduleAlarmWithRepeat(Context context, int alarmId, Intent intent, long triggerAtMillis, long intervalMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(context, alarmId, intent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
    }

    public static void cancel(Context context, int alarmId, Intent intent){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(context, alarmId, intent);

        if(alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }

    public static PendingIntent getPendingIntent(Context context, int alarmId, Intent intent){
        return PendingIntent.getService(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}