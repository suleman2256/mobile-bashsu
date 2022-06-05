package ru.bsu.application.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ru.bsu.application.R;
import ru.bsu.application.activity.LoginActivity;
import ru.bsu.application.dto.Constants;
import ru.bsu.application.dto.Employee;

public class AccountHeaderFragment extends Fragment {

    private SharedPreferences sPref;

    public AccountHeaderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_header, container, false);

        Employee employee = null;

        TextView textViewFIO = view.findViewById(R.id.textViewFIO);
        TextView textViewGroup = view.findViewById(R.id.textViewGroup);
        TextView textViewStage = view.findViewById(R.id.textViewStage);
        Button logout = view.findViewById(R.id.button_logout);
        Button buttonNotification = view.findViewById(R.id.button_notification);
        Button buttonSettings = view.findViewById(R.id.button_settings);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = v.getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
                preferences.edit().clear().apply();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationFragment notificationFragment = new NotificationFragment();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.contentAccount, notificationFragment, Constants.NOTIFICATION_FRAGMENT);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.contentAccount, settingsFragment, " ");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Employee e = loadEmployee();
        if (e != null) {
            textViewFIO.setText(String.format("%s %s %s", e.getLastName(), e.getFirstName(), e.getMiddleName()));
            textViewGroup.setText(e.getStudentGroup());
            textViewStage.setText(e.getStage());
        } else {
            if (textViewFIO.getText().length() == 0) {
                Bundle bundle = this.getArguments();
                try {
                    if (bundle != null) {
                        JSONObject jsonObject = new JSONObject(bundle.getString(Constants.EMPLOYEE));
                        Gson gson = new Gson();
                        employee = gson.fromJson(jsonObject.toString(), Employee.class);
                        saveEmployee(employee);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                if (employee != null) {
                    textViewFIO.setText(String.format("%s %s %s", employee.getLastName(), employee.getFirstName(), employee.getMiddleName()));
                    textViewGroup.setText(employee.getStudentGroup());
                    textViewStage.setText(employee.getStage());
                }
            }
        }

        return view;
    }

    void saveEmployee(Employee employee) {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(employee);
        prefsEditor.putString(Constants.EMPLOYEE, json);
        prefsEditor.apply();
    }

    Employee loadEmployee() {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sPref.getString(Constants.EMPLOYEE, "");
        return gson.fromJson(json, Employee.class);
    }
}