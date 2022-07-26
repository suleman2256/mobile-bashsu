package ru.bsu.application.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bsu.application.R;
import ru.bsu.application.api.RetrofitClient;
import ru.bsu.application.databinding.ActivityLoginBinding;
import ru.bsu.application.dto.Constants;
import ru.bsu.application.dto.Employee;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private EditText userNameET;
    private EditText passwordET;
    private SharedPreferences sPref;
    private TextView losePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        userNameET = binding.username;
        passwordET = binding.password;
        losePassword = binding.textViewLosePassword;

        losePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Обратитесь в \"Отдел рекрутинга и сопровождения иностранных обучающихся\"!", Toast.LENGTH_LONG).show();
            }
        });

        String token = loadToken();
        String username = loadLogin();
        if (!token.isEmpty() && !username.isEmpty()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .putExtra("username", username).putExtra("token", token));
        } else {
            findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginUser();
                }
            });
        }
    }

    private void loginUser() {
        String userName = userNameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (userName.isEmpty()) {
            userNameET.setError("Введите логин.");
            userNameET.requestFocus();
            return;
        } else if (password.isEmpty()) {
            passwordET.setError("Введите пароль.");
            passwordET.requestFocus();
            return;
        }


        String auth = Credentials.basic(userName, password);

        Call<Employee> call = RetrofitClient
                .getInstance()
                .getMyApi()
                .auth(auth);


        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                int s = response.code();

                if (s == 200) {
                    String token = response.headers().get("x-csrf-token");
                    saveToken(token);
                    saveLogin(userName);
                    Toast.makeText(LoginActivity.this, "Добро пожаловать!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                            .putExtra("username", userName).putExtra("token", token));
                } else {
                    Toast.makeText(LoginActivity.this, "Ошибка авторизации! Попробуйте ещё раз.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void saveToken(String token) {
        sPref = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.TOKEN, token);
        ed.apply();
    }

    void saveLogin(String login) {
        sPref = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.LOGIN, login);
        ed.apply();
    }

    private String loadLogin() {
        sPref = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.LOGIN, "");
    }

    private String loadToken() {
        sPref = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.TOKEN,"");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int[] scrcoords = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}