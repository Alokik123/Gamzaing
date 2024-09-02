package com.example.uiuxapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ArticleScreen extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_screen); // Ensure this matches your layout file name

        preferences = getSharedPreferences("app_master", MODE_PRIVATE);

        // Initialize views
        CardView applyCardView = findViewById(R.id.apply_button_card); // This is a CardView, not a Button
        ImageView backButton = findViewById(R.id.back_button3); // This is an ImageView
        webView = findViewById(R.id.article_webview); // This is a WebView
        progressBar = findViewById(R.id.progress_bar); // This is a ProgressBar

        // Set up WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE); // Show ProgressBar
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE); // Hide ProgressBar
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false; // Allow the WebView to handle URL loading
            }
        });

        // Load the URL from SharedPreferences
        String webViewUrl = preferences.getString("article_webview_link", "https://www.gamazingstudios.com/");
        if (!webViewUrl.isEmpty()) {
            webView.loadUrl(webViewUrl);
        }

        // Handle CardView click to go to InternshipActivity
        applyCardView.setOnClickListener(v -> {
            Intent intent = new Intent(ArticleScreen.this, InternshipActivity.class);
            startActivity(intent);
        });

        // Handle Back button click
        backButton.setOnClickListener(v -> finish());


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // Go back in WebView history
        } else {
            super.onBackPressed(); // Otherwise, proceed with the default back action
        }
    }
}
