package com.example.uiuxapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MentorSucessfull extends AppCompatActivity {
    // private static final int DELAY_MILLIS = 5000; // 5 seconds delay (commented out)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_successfull);

        // Uncomment this if you need to use ProgressBar
        // @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        // ProgressBar progressBar = findViewById(R.id.progress_bar3);

        // Uncomment to make ProgressBar visible if you use it
        // progressBar.setVisibility(ProgressBar.VISIBLE);

        // Set up the social media links
        setupSocialMediaLinks();

        // Delay of 5 seconds before redirecting (commented out)
        // new Handler().postDelayed(new Runnable() {
        //     @Override
        //     public void run() {
        //         // Uncomment to hide ProgressBar before starting the next activity
        //         // progressBar.setVisibility(ProgressBar.GONE);
        //
        //         // Start ArticleScreen activity
        //         Intent intent = new Intent(MentorSucessfull.this, ArticleScreen.class);
        //         startActivity(intent);
        //         finish(); // Close this activity
        //     }
        // }, DELAY_MILLIS);
    }

    private void setupSocialMediaLinks() {
        ImageView whatsappIcon = findViewById(R.id.i12);
        ImageView instagramIcon = findViewById(R.id.i13);
        ImageView facebookIcon = findViewById(R.id.i14);
        ImageView linkedinIcon = findViewById(R.id.i15);

        // Set up click listeners for each social media icon
        whatsappIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://web.whatsapp.com/");
            }
        });

        instagramIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.instagram.com/");
            }
        });

        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.facebook.com/");
            }
        });

        linkedinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://in.linkedin.com/");
            }
        });
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
