# MoodieFlix: Android Application Documentation

## Introduction

MoodieFlix is an Android application designed to provide mood-based movie recommendations. Users can explore movies tailored to their mood, sort them by various criteria, and discover more through the "Load More" functionality. The app fetches real-time movie data using web scraping techniques and displays it in a user-friendly interface.

## Key Features

Mood-Based Recommendations: Get personalized movie suggestions based on your selected mood.

Sorting Options: Sort movies by Newest, Most Popular, A → Z, Critics (Highest), or Audience (Highest).

Load More Functionality: Dynamically fetch and display additional movies as the user explores.

Error Handling: Displays toasts for no or poor internet connection and ensures smooth user experience.

Loading Animation: A circular loading animation is displayed while fetching movies.

## How to Use

### Launching the App:

Open the app from your device's home screen.

Select a mood from the available options.

### Viewing Movie Recommendations:

Movies based on the selected mood will be displayed.

Use the spinner at the top to sort movies by different criteria.

### Fetching More Movies:

Scroll to the bottom of the list or click the "Load More" button to fetch additional movies.

### Error Handling:

If there is no internet connection, a toast message will notify you.

Retry once the connection is restored.

## Technical Details

### Tools & Frameworks:

Android Studio

Android SDK (API Level 21+)

### Libraries:

Jsoup for web scraping.

Glide for Image Handeling.

### Dependencies:

Ensure the following dependencies are added to your build.gradle:

implementation 'org.jsoup:jsoup:1.14.3'
implementation 'com.github.bumptech.glide:glide:4.16.0'

### Minimum Requirements:

Android OS: 5.0 (Lollipop) or above.

Internet connection for fetching movie data.

Known Issues

Currently, the app fetches a maximum of 50 movies per query due to web scraping limitations.

Future Enhancements

Popup Window for Movie Details:

Add a feature to show a popup window with the movie description and a trailer when a movie is clicked.

### Improved Pagination:

Implement infinite scrolling for smoother user experience.

### Offline Mode:

Allow users to save movies for offline viewing.

Screenshots

(Include screenshots of key app screens here.)

### Contact

For questions or feedback, please contact:

### Developer: Kishan

Email: kishankhanna.official@gmail.com

Thank you for using MoodieFlix!
