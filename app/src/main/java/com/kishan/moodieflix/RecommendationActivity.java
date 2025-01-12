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

        if (isLoading) return; // Prevent multiple requests
        isLoading = true;
        MovieScraper.fetchMovies(genre, sort, page, movies -> {
            // Clear the movie list and add fetched movies
            movieList.clear();
            movieList.addAll(movies);
            movieAdapter.notifyDataSetChanged();
        });
        Toast.makeText(this, "Hang on, Fetching movies...", Toast.LENGTH_SHORT).show();
    }

    private void loadMoreMovies() {
        currentPage++; // Increment page number
        fetchMovies(genre, currentSort, currentPage); // Fetch next page of movies
        Log.d("TAG", "loadMoreMovies:RAN "+ currentPage);
    }
}
