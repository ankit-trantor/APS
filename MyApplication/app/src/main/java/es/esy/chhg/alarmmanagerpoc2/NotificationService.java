package es.esy.chhg.alarmmanagerpoc2;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class NotificationService extends IntentService {
    public static final String TAG = NotificationService.class.getSimpleName();

    public NotificationService() {
        super(TAG);
    }

    public static Intent getIntentNotificationService(Context context) {
        return new Intent(context, NotificationService.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            int hours = calendar.get(Calendar.HOUR_OF_DAY);

            if (hours == 9 || hours == 13 || hours == 22) {

                Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                NotificationUtil.create(getApplicationContext(), 1, newIntent, android.R.drawable.ic_delete, "Teste Title", "Teste subtitle");
            }
        }
    }
}