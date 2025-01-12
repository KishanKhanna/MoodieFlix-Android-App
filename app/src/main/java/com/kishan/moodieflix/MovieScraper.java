package com.kishan.moodieflix;

import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MovieScraper {

    // Define an interface to handle callback once movies are fetched
    public interface MovieFetchListener {
        void onMoviesFetched(List<Movie> movies);
    }

    // Public method to fetch movies based on genre
    public static void fetchMovies(String genre,String sort,int page, MovieFetchListener listener) {
        // Execute the task to fetch movies in background
        new FetchMoviesTask(genre,sort,page, listener).execute();
    }

    // AsyncTask to handle the network operations in background
    private static class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
        private final String genre; // Genre passed from the calling activity
        private final String sort;
        private final int page;
        private final MovieFetchListener listener; // Callback listener to return data

        // Constructor to initialize the genre and listener
        FetchMoviesTask(String genre, String sort, int page, MovieFetchListener listener) {
            this.genre = genre;
            this.sort = sort;
            this.page = page;
            this.listener = listener;
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            // List to hold fetched movie objects
            List<Movie> movieList = new ArrayList<>();
            try {
                // Construct the URL based on genre
                String url = "https://www.rottentomatoes.com/browse/movies_at_home/genres:" + genre + sort +"?page=" + page;

                Document doc = Jsoup.connect(url).get();
                Elements movies = doc.select("div.flex-container");

                for (Element movie : movies) {
                    // Fetch movie title
                    String name = movie.select("span[data-qa=discovery-media-list-item-title]").text();

                    // Fetch movie URL
                    String relativeUrl = movie.select("a[data-qa=discovery-media-list-item-caption]").attr("href");
                    String movieUrl = "https://www.rottentomatoes.com" + relativeUrl;

                    // Fetch critic rating
                    String criticRating = movie.select("rt-text[slot=criticsScore]").text();
                    criticRating = criticRating.isEmpty() ? "N/A" : criticRating;

                    // Fetch audience rating
                    String audienceRating = movie.select("rt-text[slot=audienceScore]").text();
                    audienceRating = audienceRating.isEmpty() ? "N/A" : audienceRating;

                    // Fetch image source
                    String imageSource = movie.select("rt-img").attr("src");

                    // Fetch streaming date
                    String dateCreated = movie.select("span[data-qa=discovery-media-list-item-start-date]").text();

                    // Add the movie object to the list
                    movieList.add(new Movie(name, movieUrl, dateCreated, imageSource,criticRating, audienceRating));
                }
            } catch (Exception e) {
                // Handle any exceptions (network, parsing, etc.)
                e.printStackTrace();
            }
            return movieList; // Return the list of movieList fetched
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            // Once background task is done, pass the movies list to the listener
            if (listener != null) {
                listener.onMoviesFetched(movies);
            }
        }
    }
}



