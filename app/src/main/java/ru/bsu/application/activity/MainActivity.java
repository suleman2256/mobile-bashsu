package ru.bsu.application.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.yandex.mapkit.MapKitFactory;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bsu.application.R;
import ru.bsu.application.api.RetrofitClient;
import ru.bsu.application.dto.Constants;
import ru.bsu.application.fragment.AccountFragment;
import ru.bsu.application.fragment.AccountHeaderFragment;
import ru.bsu.application.fragment.CampusFragment;
import ru.bsu.application.fragment.FAQFragment;
import ru.bsu.application.fragment.NavigationFragment;
import ru.bsu.application.fragment.NotificationFragment;
import ru.bsu.application.fragment.OrderDocumentsFragment;
import ru.bsu.application.fragment.PosterFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    JSONObject jsonObject;
    String employeeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Bundle arguments = getIntent().getExtras();

        String token = arguments.get("token").toString();
        String userName = arguments.get("username").toString();

        Call<Object> call = RetrofitClient
                .getInstance()
                .getMyApi()
                .getEmployee(token, userName);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    employeeString = jsonObject.getJSONObject("result").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                bundle.putString(Constants.EMPLOYEE, employeeString);

                AccountFragment accountFragment = new AccountFragment();
                FragmentTransaction fragmentTransaction0 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction0.replace(R.id.content, accountFragment, "");
                fragmentTransaction0.addToBackStack(null);
                fragmentTransaction0.commit();

                AccountHeaderFragment accountHeaderFragment = new AccountHeaderFragment();
                accountHeaderFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.accountHeaderFragment, accountHeaderFragment, "");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                NotificationFragment notificationFragment = new NotificationFragment();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.contentAccount, notificationFragment, "");
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setOnItemSelectedListener(selectedListener);
    }

    private final NavigationBarView.OnItemSelectedListener selectedListener = new NavigationBarView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.accountFragment:
                    AccountFragment accountFragment = new AccountFragment();
                    FragmentTransaction fragmentTransaction00 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction00.replace(R.id.content, accountFragment, "");
                    fragmentTransaction00.addToBackStack(null);
                    fragmentTransaction00.commit();

                    AccountHeaderFragment accountHeaderFragment = new AccountHeaderFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.accountHeaderFragment, accountHeaderFragment, "");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    NotificationFragment notificationFragment = new NotificationFragment();
                    FragmentTransaction fragmentTransaction0 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction0.replace(R.id.contentAccount, notificationFragment, "");
                    fragmentTransaction0.addToBackStack(null);
                    fragmentTransaction0.commit();
                    return true;
                case R.id.posterFragment:
                    PosterFragment posterFragment = new PosterFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, posterFragment, "");
                    fragmentTransaction1.commit();
                    return true;
                case R.id.campusFragment:
                    CampusFragment campusFragment = new CampusFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, campusFragment, "");
                    fragmentTransaction2.addToBackStack(null);
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigationFragment:
                    NavigationFragment navigationFragment = new NavigationFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, navigationFragment, "");
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };

    public void faq(View view) {
        FAQFragment FAQFragment = new FAQFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, FAQFragment, Constants.FAQ_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content);
        if (currentFragment.getTag().equals(Constants.FAQ_FRAGMENT)) {
            CampusFragment campusFragment = new CampusFragment();
            FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.replace(R.id.content, campusFragment, "");
            fragmentTransaction2.addToBackStack(null);
            fragmentTransaction2.commit();
        } else if (currentFragment.getTag().equals(Constants.ORDER_DOCUMENTS)){
            CampusFragment campusFragment = new CampusFragment();
            FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.replace(R.id.content, campusFragment, "");
            fragmentTransaction2.addToBackStack(null);
            fragmentTransaction2.commit();
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    public void orderDocuments(View view) {
        OrderDocumentsFragment orderDocumentsFragment = new OrderDocumentsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, orderDocumentsFragment, Constants.ORDER_DOCUMENTS);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static final class MapKitInitializer {
        private static boolean initialized = false;
        @NotNull
        public static final MapKitInitializer INSTANCE;

        public void initialize(@NotNull String apiKey, @NotNull Context context) {
            Intrinsics.checkNotNullParameter(apiKey, "apiKey");
            Intrinsics.checkNotNullParameter(context, "context");
            if (!initialized) {
                MapKitFactory.setApiKey(apiKey);
                MapKitFactory.initialize(context);
            }
            initialized = true;
        }

        private MapKitInitializer() {
        }

        static {
            INSTANCE = new MapKitInitializer();
        }
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
            EditText editTextEmail = findViewById(R.id.editTextEmail);
            EditText editTextCurrentPassword = findViewById(R.id.editTextCurrentPassword);
            EditText editTextNewPassword = findViewById(R.id.editTextNewPassword);
            EditText editTextRepeatNewPassword = findViewById(R.id.editTextRepeatNewPassword);
            editTextEmail.clearFocus();
            editTextCurrentPassword.clearFocus();
            editTextNewPassword.clearFocus();;
            editTextRepeatNewPassword.clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

}