package com.example.movieapp.model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.movieapp.R;
import com.example.movieapp.service.MovieDataService;
import com.example.movieapp.service.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Long, Result> {

    private MovieDataService movieDataService;
    private Application application;

    public MovieDataSource(MovieDataService movieDataService, Application application) {
        this.movieDataService = movieDataService;
        this.application = application;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Result> callback) {
        movieDataService = RetrofitClient.getService();

        Call<MovieDBResponse> call = movieDataService.getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.app_key),1);

        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();

                ArrayList<Result> movies = new ArrayList<>();

                if (movieDBResponse != null && movieDBResponse.getResults() != null) {

                    movies = (ArrayList<Result>) movieDBResponse.getResults();

                    callback.onResult(movies, null, (long) 2);
                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Result> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Result> callback) {
        movieDataService = RetrofitClient.getService();

        Call<MovieDBResponse> call = movieDataService.getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.app_key),params.key);

        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();

                ArrayList<Result> movies = new ArrayList<>();

                if (movieDBResponse != null && movieDBResponse.getResults() != null) {

                    movies = (ArrayList<Result>) movieDBResponse.getResults();

                    callback.onResult(movies, params.key + 1);
                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
