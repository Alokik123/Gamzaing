package com.example.uiuxapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class InternshipSucessfull extends AppCompatActivity {
    private static final int DELAY_MILLIS = 5000; // 5 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internship_successfull);

        // Set up social media links
        ImageView whatsappIcon = findViewById(R.id.i12);
        ImageView instagramIcon = findViewById(R.id.i13);
        ImageView facebookIcon = findViewById(R.id.i14);
        ImageView linkedinIcon = findViewById(R.id.i15);

        whatsappIcon.setOnClickListener(v -> openUrl("https://web.whatsapp.com/"));
        instagramIcon.setOnClickListener(v -> openUrl("https://www.instagram.com/"));
        facebookIcon.setOnClickListener(v -> openUrl("https://www.facebook.com/"));
        linkedinIcon.setOnClickListener(v -> openUrl("https://in.linkedin.com/"));

        // Delay of 5 seconds before redirecting
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Start the next activity
//                Intent intent = new Intent(InternshipSucessfull.this, ArticleScreen.class);
//                startActivity(intent);
//                finish(); // Close this activity
//            }
//        }, DELAY_MILLIS);
    }

    // Helper method to open a URL
    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
