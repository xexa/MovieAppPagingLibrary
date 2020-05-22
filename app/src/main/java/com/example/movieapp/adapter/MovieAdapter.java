package com.example.movieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.model.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewholder> {

    private Context context;
    private List<Result> movies;

    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MovieAdapter(Context context, List<Result> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        holder.titleTextView.setText(movies.get(position).getOriginalTitle());
        holder.ratingTextView.setText(String.valueOf(movies.get(position).getVoteAverage()));

        String imagePath = "https://image.tmdb.org/t/p/w500/" + movies.get(position).getPosterPath();


        Picasso.get()
                .load(imagePath)
                .placeholder(R.drawable.loading)
                .into(holder.movieImage);

    }

    @Override
    public int getItemCount() {
        if (movies.size() > 0)
            return movies.size();
        return 0;
    }


    class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView movieImage;
        private TextView titleTextView;
        private TextView ratingTextView;


        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            movieImage = itemView.findViewById(R.id.ivMovie);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            ratingTextView = itemView.findViewById(R.id.tvRating);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Triggers click upwards to the adapter on click
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position);
                }
            }
        }
    }
}
