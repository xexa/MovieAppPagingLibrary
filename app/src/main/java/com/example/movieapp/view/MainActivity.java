package com.example.movieapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.adapter.MovieAdapter;
import com.example.movieapp.model.MovieDBResponse;
import com.example.movieapp.model.Result;
import com.example.movieapp.service.MovieDataService;
import com.example.movieapp.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String APP_KEY = "c21d7c6019755303168daf82d3f09d45";

    private List<Result> movies;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        getSupportActionBar().setTitle("TMDB Popular Movies Today");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        getPopularMovies();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMovies();
            }
        });
    }

    private void getPopularMovies() {

        MovieDataService movieDataService = RetrofitClient.getService();

        Call<MovieDBResponse> call = movieDataService.getPopularMovies(APP_KEY);
        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();
                if (movieDBResponse != null && movieDBResponse.getResults() !=null){

                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    movies = response.body().getResults();

                    movieAdapter = new MovieAdapter(MainActivity.this, movies);

                    recyclerView.setItemAnimator(new SlideInUpAnimator());
                    movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            String name = movies.get(position).getOriginalTitle();
                            Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this,MovieActivity.class);


                            String title = movies.get(position).getOriginalTitle();
                            String synopsis = movies.get(position).getOverview();
                            String rating = String.valueOf(movies.get(position).getVoteAverage());
                            String releaseDate = String.valueOf(movies.get(position).getReleaseDate());
                            String image = movies.get(position).getPosterPath();

                            intent.putExtra("title",title);
                            intent.putExtra("synopsis",synopsis);
                            intent.putExtra("rating",rating);
                            intent.putExtra("releaseDate",releaseDate);
                            intent.putExtra("image",image);

                            startActivity(intent);

                        }
                    });
                    recyclerView.setAdapter(movieAdapter);

                    movieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {
                call.cancel();
                Log.i("error", Objects.requireNonNull(t.getLocalizedMessage()));
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
