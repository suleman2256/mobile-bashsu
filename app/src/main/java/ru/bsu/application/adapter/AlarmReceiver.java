package ru.bsu.application.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bsu.application.R;
import ru.bsu.application.activity.SplashActivity;
import ru.bsu.application.api.RetrofitClient;
import ru.bsu.application.dto.Constants;
import ru.bsu.application.dto.Notification;

public class AlarmReceiver extends BroadcastReceiver {

    private SharedPreferences sPref;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String login = loadLogin(context);
        String token = loadToken(context);

        Call<List<Notification>> call = RetrofitClient
                .getInstance()
                .getMyApi()
                .getNotification(token, login);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                int s = response.code();

                if (s == 200) {
                    List<Notification> responseNotification = response.body();
                    List<Notification> notificationList = loadNotifications(context);
                    if (responseNotification != null) {
                        if (responseNotification.size() > notificationList.size()) {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notificationId")
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle("Вам пришло уведомление!")
                                    .setContentText("Нажмите, чтобы прочесть его.")
                                    .setAutoCancel(true)
                                    .setShowWhen(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                CharSequence name = "bashSUChannel";
                                String description = "Channel for Alarm Manager";
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel channel = new NotificationChannel("notificationId", name, importance);
                                channel.setDescription(description);

                                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);
                            }

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                            notificationManagerCompat.notify(1, builder.build());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {

            }
        });



    }

    private String loadToken(Context context) {
        sPref = context.getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.TOKEN,"");
    }

    List<Notification> loadNotifications(Context context) {
        sPref = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sPref.getString(Constants.NOTIFICATIONS, "");
        return gson.fromJson(json, new TypeToken<List<Notification>>(){}.getType());
    }

    private String loadLogin(Context context) {
        sPref = context.getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.LOGIN, "");
    }
}
