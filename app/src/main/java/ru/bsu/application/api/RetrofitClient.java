package ru.bsu.application.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static  final String BASE_URL = "http://10.0.2.2:8091/api/"; //local emulator
    //private static  final String BASE_URL = "http://localhost:8091/api/"; //xiaomi
    private static RetrofitClient mInstance = null;
    private API myApi;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private RetrofitClient () {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        myApi = retrofit.create(API.class);
    }


    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public API getMyApi () {
        return myApi;
    }
}
