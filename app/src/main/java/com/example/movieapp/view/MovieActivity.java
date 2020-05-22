package com.example.movieapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    private ImageView movieImage;

    private TextView movieTitle;
    private TextView movieSynopsis;
    private TextView movieRating;
    private TextView movieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


        movieImage = findViewById(R.id.ivMovieLarge);
        movieTitle = findViewById(R.id.tvMovieTitle);
        movieSynopsis = findViewById(R.id.tvPlotsynopsis);
        movieRating = findViewById(R.id.tvMovieRating);
        movieReleaseDate = findViewById(R.id.tvReleaseDate);


        Intent intent = getIntent();


        String title = intent.getStringExtra("title");
        String synopsis = intent.getStringExtra("synopsis");
        String rating = intent.getStringExtra("rating");
        String releaseDate = intent.getStringExtra("releaseDate");
        String image = intent.getStringExtra("image");

        Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();

        String path = "https://image.tmdb.org/t/p/w500" + image;

        Picasso.get()
                .load(path)
                .placeholder(R.drawable.loading)
                .into(movieImage);

        setTitle(title);

        movieTitle.setText(title);
        movieSynopsis.setText(synopsis);
        movieRating.setText(rating);
        movieReleaseDate.setText(releaseDate);

    }
}
