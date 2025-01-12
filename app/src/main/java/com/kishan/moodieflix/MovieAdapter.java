package com.kishan.moodieflix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.MessageFormat;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final List<Movie> movieList;

    // Constructor
    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieName.setText(movie.name());
        holder.criticRating.setText(MessageFormat.format("Critics: {0}", movie.criticRating() != null ? movie.criticRating() : "N/A"));
        holder.audienceRating.setText(MessageFormat.format("Audience: {0}", movie.criticRating() != null ? movie.audienceRating() : "N/A"));
        holder.releaseDate.setText((movie.releaseDate()));



        // Load movie image using Glide or Picasso (optional, but recommended for image loading)

        Glide.with(holder.movieImage.getContext())
                .load(movie.imageSource())
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView movieName, criticRating, audienceRating, releaseDate;
        public ImageView movieImage;

        public MovieViewHolder(View view) {
            super(view);
            movieName = view.findViewById(R.id.movie_name);
            criticRating = view.findViewById(R.id.critic_rating);
            audienceRating =view.findViewById(R.id.audience_rating);
            movieImage = view.findViewById(R.id.movie_image);
            releaseDate = view.findViewById(R.id.release_date);
        }
    }
}

