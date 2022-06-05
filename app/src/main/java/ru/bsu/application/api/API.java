package ru.bsu.application.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.bsu.application.dto.Employee;
import ru.bsu.application.dto.Notification;
import ru.bsu.application.dto.Poster;

public interface API {

    @POST("auth/login")
    Call<Employee> auth (@Header ("Authorization") String auth);

    @GET("employee/filter/{login}")
    Call<Object> getEmployee (@Header("x-csrf-token") String token, @Path("login") String login);

    @POST("employee")
    Call<Object> save (@Header("x-csrf-token") String token, @Body Employee employee);

    @GET("notification/filter/{login}")
    Call<List<Notification>> getNotification (@Header("x-csrf-token") String token, @Path("login") String login);

    @GET("poster/all")
    Call<List<Poster>> getPoster (@Header("x-csrf-token") String token);
}
