package com.example.movieapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.example.movieapp.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {


    private List<Result> movies;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        Objects.requireNonNull(getSupportActionBar()).setTitle("TMDB Popular Movies Today");

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);


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

        viewModel.getAllMovies().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> moviesFromLiveData) {
                movies =moviesFromLiveData;

                showOnRecyclerView();
            }
        });


    }

    public void showOnRecyclerView(){
        movieAdapter = new MovieAdapter(this, movies);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));


        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                Intent intent =  new Intent(MainActivity.this,MovieActivity.class);

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
        movieAdapter.notifyDataSetChanged();
    }
}
