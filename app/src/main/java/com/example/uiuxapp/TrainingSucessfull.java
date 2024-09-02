package com.example.uiuxapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TrainingSucessfull extends AppCompatActivity {
    private static final int DELAY_MILLIS = 5000; // 5 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_successfull);

        // Social media ImageView setup
        ImageView whatsappIcon = findViewById(R.id.i12);
        ImageView instagramIcon = findViewById(R.id.i13);
        ImageView facebookIcon = findViewById(R.id.i14);
        ImageView linkedinIcon = findViewById(R.id.i15);

        // Set OnClickListener for each icon
        whatsappIcon.setOnClickListener(v -> openLink("https://web.whatsapp.com/"));
        instagramIcon.setOnClickListener(v -> openLink("https://www.instagram.com/"));
        facebookIcon.setOnClickListener(v -> openLink("https://www.facebook.com/"));
        linkedinIcon.setOnClickListener(v -> openLink("https://in.linkedin.com/"));

        // Delay of 5 seconds before redirecting
//        new Handler().postDelayed(() -> {
//            Intent intent = new Intent(TrainingSucessfull.this, ArticleScreen.class);
//            startActivity(intent);
//            finish(); // Close this activity
//        }, DELAY_MILLIS);
    }

    // Helper method to open links
    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
