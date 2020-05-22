package com.example.movieapp.model;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.R;
import com.example.movieapp.service.MovieDataService;
import com.example.movieapp.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private List<Result> movies = new ArrayList<>();
    private MutableLiveData<List<Result>> mutableLiveData = new MutableLiveData<>();

    private Application application;

    public MovieRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Result>> getMutableLiveData() {

        final MovieDataService movieDataService = RetrofitClient.getService();

        Call<MovieDBResponse> call = movieDataService.getPopularMovies(application.getApplicationContext().getString(R.string.app_key));

        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();

                if (movieDBResponse != null && movieDBResponse.getResults() != null){

                    movies = new ArrayList<>(movieDBResponse.getResults());

                    mutableLiveData.setValue(movies);
                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });

        return mutableLiveData;
    }
}
