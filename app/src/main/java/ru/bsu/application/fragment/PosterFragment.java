package ru.bsu.application.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bsu.application.R;
import ru.bsu.application.adapter.PosterAdapter;
import ru.bsu.application.api.RetrofitClient;
import ru.bsu.application.dto.Constants;
import ru.bsu.application.dto.Poster;
import ru.bsu.application.dto.RecyclerItemPoster;

public class PosterFragment extends Fragment {

    public PosterFragment() {
    }

    private SharedPreferences sPref;
    private RecyclerView recyclerView;
    private PosterAdapter adapter;
    private List<RecyclerItemPoster> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poster, container, false);

        recyclerView = view.findViewById(R.id.posterList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String token = loadToken();

        Call<List<Poster>> call = RetrofitClient
                .getInstance()
                .getMyApi()
                .getPoster(token);

        call.enqueue(new Callback<List<Poster>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<Poster>> call, Response<List<Poster>> response) {
                int s = response.code();

                if (s == 200) {
                    List<Poster> responseP = response.body();
                    itemList = new ArrayList<>();

                    for (Poster n : responseP) {
                        RecyclerItemPoster poster = new RecyclerItemPoster();
                        poster.setTitle(n.getName());
                        poster.setTime("Начало: " + n.getTime());
                        if (n.getCost() == 0) {
                            poster.setCost("Вход: Свободный");
                        } else {
                            poster.setCost("Вход: " + n.getCost() + "₽");
                        }
                        poster.setLocation("Место: " + n.getLocation());
                        poster.setReason(n.getReasonCancellation());
                        poster.setDate("Дата: " + n.getDate());
                        itemList.add(poster);
                    }

                    adapter = new PosterAdapter(itemList, getContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Poster>> call, Throwable t) {

            }
        });


        return view;
    }

    private String loadToken() {
        sPref = getContext().getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.TOKEN,"");
    }
}