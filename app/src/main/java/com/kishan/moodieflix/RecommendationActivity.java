package com.kishan.moodieflix;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter;
    private final List<Movie> movieList = new ArrayList<>();
    private int currentPage = 1; // Tracks the current page
    private String currentSort = ""; // Tracks the current sort option
    private String genre = null; // Default genre
    private boolean isLoading = false;
    private View loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommendation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Handle intent data
        genre = getIntent().getStringExtra("genre");

        //Load More button
        // Reference for the dynamically added button
        Button loadMoreButton = findViewById(R.id.loadMoreButton);
        loadMoreButton.setOnClickListener(v -> loadMoreMovies());
        setupRecyclerView();
        setupSortSpinner();

        loadingOverlay = findViewById(R.id.loading_overlay);
        fetchMovies(genre, currentSort, currentPage); // Initial fetch
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter(movieList);
        recyclerView.setAdapter(movieAdapter);
    }

    private void setupSortSpinner() {
        Spinner sortSpinner = findViewById(R.id.sort_spinner);

        // Define sorting options
        Map<String, String> sortOptions = new HashMap<>();
        sortOptions.put("Sort By", "");
        sortOptions.put("Newest", "~sort:newest");
        sortOptions.put("Most Popular", "~sort:popular");
        sortOptions.put("A → Z", "~sort:a_z");
        sortOptions.put("Critics (Highest)", "~sort:critic_highest");
        sortOptions.put("Audience (Highest)", "~sort:audience_highest");

        // Set up the spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        // Handle sort selection changes
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSort = parent.getItemAtPosition(position).toString();
                currentSort = sortOptions.get(selectedSort); // Update the sort parameter
                fetchMovies(genre, currentSort, currentPage); // Fetch movies with the updated sort
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
                fetchMovies(genre, currentSort, currentPage);
            }
        });
    }

    private void fetchMovies(String genre, String sort, int page) {

        if (!NetworkUtils.isNetworkAvailable(this)) {
            showRetrySnackbar();
            return;
        }

        if (isLoading) return; // Prevent multiple requests
        isLoading = true;

        // Show the loading overlay
        loadingOverlay.setVisibility(View.VISIBLE);
        MovieScraper.fetchMovies(genre, sort, page, new MovieScraper.MovieFetchListener(){

            @Override
            public void onMoviesFetched(List<Movie> movies) {

                // Clear the movie list and add fetched movies
                movieList.clear();
                movieList.addAll(movies);
                movieAdapter.notifyDataSetChanged();
                // Hide the loading overlay
                loadingOverlay.setVisibility(View.GONE);
                isLoading = false;
            }
        });
        Toast.makeText(this, "Hang on, Fetching movies...", Toast.LENGTH_SHORT).show();
    }

    private void loadMoreMovies() {

        if (!NetworkUtils.isNetworkAvailable(this)) {
            showRetrySnackbar();
            return;
        }

        if (isLoading) return; // Prevent multiple requests
        isLoading = true;
        MovieScraper.fetchMovies(genre, currentSort, currentPage+1, new MovieScraper.MovieFetchListener(){

            @Override
            public void onMoviesFetched(List<Movie> movies) {

                // Clear the movie list and add fetched movies
                movieList.clear();
                movieList.addAll(movies);
                movieAdapter.notifyDataSetChanged();
                isLoading = false;
                currentPage++; // Increment page number
            }
        });
        Log.d("TAG", "loadMoreMovies:RAN "+ currentPage);
    }
    private void showRetrySnackbar() {
        Snackbar.make(findViewById(R.id.main), "No internet connection.\nPlease try again", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", v -> fetchMovies(genre, currentSort, currentPage))
                .show();
    }
}
