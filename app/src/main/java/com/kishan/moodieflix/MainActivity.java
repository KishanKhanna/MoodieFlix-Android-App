package com.kishan.moodieflix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button happyButton = findViewById(R.id.buttonHappy);
        Button sadButton = findViewById(R.id.buttonSad);
        Button angryButton = findViewById(R.id.buttonAngry);
        Button relaxedButton = findViewById(R.id.buttonRelaxed);
        Button anxiousButton = findViewById(R.id.buttonAnxious);
        Button boredButton = findViewById(R.id.buttonBored);
        Button romanticButton = findViewById(R.id.buttonRomantic);
        Button nostalgicButton = findViewById(R.id.buttonNostalgic);
        intent = new Intent(MainActivity.this, RecommendationActivity.class);

        happyButton.setOnClickListener(v -> moodToGenre("happy"));
        sadButton.setOnClickListener(v -> moodToGenre("sad"));
        angryButton.setOnClickListener(v -> moodToGenre("angry"));
        relaxedButton.setOnClickListener(v -> moodToGenre("relaxed"));
        anxiousButton.setOnClickListener(v -> moodToGenre("anxious"));
        boredButton.setOnClickListener(v -> moodToGenre("bored"));
        romanticButton.setOnClickListener(v -> moodToGenre("romantic"));
        nostalgicButton.setOnClickListener(v -> moodToGenre("nostalgic"));
    }

    private void moodToGenre(String mood){
        String genre = switch (mood) {
            case "happy" -> "adventure,animation,anime,comedy,fantasy,kids_and_family,music,romance";
            case "sad" -> "anime,biography,documentary,drama,music,romance";
            case "angry" -> "action,anime,crime,horror,mystery_and_thriller,war";
            case "relaxed" -> "anime,comedy,health_and_wellness,music,nature,romance";
            case "anxious" -> "anime,comedy,documentary,health_and_wellness,music,nature";
            case "bored" -> "action,adventure,anime,comedy,mystery_and_thriller,sci_fi";
            case "romantic" -> "anime,comedy,drama,music,romance";
            case "nostalgic" -> "animation,anime,history,holiday,kids_and_family,music";
            default -> null;
        };
        openRecommendationActivity(genre);

    }
    private void openRecommendationActivity(String genre){
        intent.putExtra("genre",genre);
        startActivity(intent);
    }
}