package ru.bsu.application.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bsu.application.R;
import ru.bsu.application.api.RetrofitClient;
import ru.bsu.application.dto.Constants;
import ru.bsu.application.dto.Employee;

public class SettingsFragment extends Fragment {

    private SharedPreferences sPref;
    private JSONObject jsonObject;
    private EditText eTEmail;
    private EditText eTCurrentPassword;
    private EditText eTNewPassword;
    private EditText eTRepeatNewPassword;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView textViewEmail = view.findViewById(R.id.textViewEmail);
        Button buttonChangeEmail = view.findViewById(R.id.buttonChangeEmail);
        eTEmail = view.findViewById(R.id.editTextEmail);
        Button buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        eTCurrentPassword = view.findViewById(R.id.editTextCurrentPassword);
        eTNewPassword = view.findViewById(R.id.editTextNewPassword);
        eTRepeatNewPassword = view.findViewById(R.id.editTextRepeatNewPassword);

        Employee e = loadEmployee();
        if (e != null) {
            if (e.getEmail() != null) {
                textViewEmail.setText(e.getEmail());
            } else {
                textViewEmail.setText("Почта не указана.");
            }
        }

        buttonChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee employee = loadEmployee();

                eTEmail = view.findViewById(R.id.editTextEmail);

                String email = eTEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    eTEmail.setError("Введите почту.");
                    eTEmail.requestFocus();
                    return;
                } else {
                    boolean isEmailValid = isEmailValid(email);
                    if (!isEmailValid) {
                        eTEmail.setError("Введите корректное значение.");
                        eTEmail.requestFocus();
                        return;
                    }
                }

                eTEmail.clearFocus();

                employee.setEmail(eTEmail.getText().toString().trim());
                String token = loadToken();

                Call<Object> call = RetrofitClient
                        .getInstance()
                        .getMyApi()
                        .save(token,employee);

                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        int s = response.code();
                        Employee responseEmployee = null;

                        if (s == 200) {
                            try {
                                String employeeString = new JSONObject(new Gson().toJson(response.body())).getJSONObject("result").toString();
                                jsonObject = new JSONObject(employeeString);
                                responseEmployee = new Gson().fromJson(jsonObject.toString(), Employee.class);
                                saveEmployee(responseEmployee);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            TextView textViewEmail = view.findViewById(R.id.textViewEmail);
                            textViewEmail.setText(responseEmployee.getEmail());
                            eTEmail.setText("");
                            Toast.makeText(getContext(), "Успешно!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Ошибка сохранения данных! Перезайдите в аккаунт.", Toast.LENGTH_LONG).show();
                        }

                    }


                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee employee = loadEmployee();

                eTCurrentPassword = view.findViewById(R.id.editTextCurrentPassword);
                eTNewPassword = view.findViewById(R.id.editTextNewPassword);
                eTRepeatNewPassword = view.findViewById(R.id.editTextRepeatNewPassword);

                String currentPassword = eTCurrentPassword.getText().toString().trim();
                String newPassword = eTNewPassword.getText().toString().trim();
                String repeatNewPassword = eTRepeatNewPassword.getText().toString().trim();

                if (currentPassword.isEmpty()) {
                    eTCurrentPassword.setError("Введите текущий пароль.");
                    eTCurrentPassword.requestFocus();
                    return;
                } else if (newPassword.isEmpty()){
                    eTNewPassword.setError("Введите новый пароль.");
                    eTNewPassword.requestFocus();
                    return;
                } else if (repeatNewPassword.isEmpty()) {
                    eTRepeatNewPassword.setError("Повторите новый пароль.");
                    eTRepeatNewPassword.requestFocus();
                    return;
                }

                if (!currentPassword.equals(employee.getPassword())) {
                    eTCurrentPassword.setError("Текущий пароль не верный.");
                    eTCurrentPassword.requestFocus();
                    return;
                }

                if (!newPassword.equals(repeatNewPassword)) {
                    eTRepeatNewPassword.setError("Пароли не совпадают.");
                    eTRepeatNewPassword.requestFocus();
                    return;
                }

                eTCurrentPassword.clearFocus();
                eTNewPassword.clearFocus();
                eTRepeatNewPassword.clearFocus();

                employee.setPassword(eTRepeatNewPassword.getText().toString().trim());
                String token = loadToken();

                Call<Object> call = RetrofitClient
                        .getInstance()
                        .getMyApi()
                        .save(token,employee);

                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        int s = response.code();
                        Employee responseEmployee = null;

                        if (s == 200) {
                            try {
                                String employeeString = new JSONObject(new Gson().toJson(response.body())).getJSONObject("result").toString();
                                jsonObject = new JSONObject(employeeString);
                                responseEmployee = new Gson().fromJson(jsonObject.toString(), Employee.class);
                                saveEmployee(responseEmployee);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            eTCurrentPassword.setText("");
                            eTNewPassword.setText("");
                            eTRepeatNewPassword.setText("");
                            Toast.makeText(getContext(), "Успешно!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Ошибка сохранения данных! Перезайдите в аккаунт.", Toast.LENGTH_LONG).show();
                        }

                    }


                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }

    Employee loadEmployee() {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sPref.getString(Constants.EMPLOYEE,"");
        return gson.fromJson(json, Employee.class);
    }


    void saveEmployee(Employee employee) {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(employee);
        prefsEditor.putString(Constants.EMPLOYEE, json);
        prefsEditor.apply();
    }

    private String loadToken() {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.TOKEN,"");
    }

    private boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}