package ru.bsu.application.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bsu.application.R;
import ru.bsu.application.adapter.AlarmReceiver;
import ru.bsu.application.adapter.NotificationAdapter;
import ru.bsu.application.api.RetrofitClient;
import ru.bsu.application.dto.Constants;
import ru.bsu.application.dto.Notification;
import ru.bsu.application.dto.RecyclerItemNotification;

public class NotificationFragment extends Fragment {

    public NotificationFragment() {
    }

    private SharedPreferences sPref;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<RecyclerItemNotification> itemList;
    private AlarmReceiver alarmReceiver;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.notificationList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String login = loadLogin();
        String token = loadToken();

        Call<List<Notification>> call = RetrofitClient
                .getInstance()
                .getMyApi()
                .getNotification(token, login);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                int s = response.code();

                if (s == 200) {
                    List<Notification> responseN = response.body();
                    itemList = new ArrayList<>();

                    for (Notification n : responseN) {
                        itemList.add(new RecyclerItemNotification(n.getName(), n.getDescription(), View.INVISIBLE));
                    }

                    List<Notification> notificationList = loadNotifications();

                    if (notificationList != null) {
                        if (notificationList.size() != itemList.size()) {
                            int i = itemList.size() - notificationList.size();
                            if (i > 0) {
                                for (int j = 0; j <= i-1; j++) {
                                    itemList.get(j).setVisible(View.VISIBLE);
                                }
                            }
                        }
                    } else {
                        for (int k = 0; k < itemList.size(); k++) {
                            itemList.get(k).setVisible(View.VISIBLE);
                        }
                    }

                    saveNotifications(responseN);

                    adapter = new NotificationAdapter(itemList, getContext());
                    recyclerView.setAdapter(adapter);
                    cancelAlarm();
                    setAlarm();
                } else {
                    Toast.makeText(getContext(), "Ошибка получение данных! Перезайдите в аккаунт.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {

            }
        });

        return view;
    }

    private void setAlarm() {
        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60, pendingIntent);
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        if (alarmManager == null) {
            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
    }

    private String loadLogin() {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.LOGIN, "");
    }

    private String loadToken() {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.TOKEN,"");
    }

    void saveNotifications(List<Notification> notification) {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(notification);
        prefsEditor.putString(Constants.NOTIFICATIONS, json);
        prefsEditor.apply();
    }

    List<Notification> loadNotifications() {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sPref.getString(Constants.NOTIFICATIONS, "");
        return gson.fromJson(json, new TypeToken<List<Notification>>(){}.getType());
    }
}