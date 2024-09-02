package com.example.uiuxapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MentorWebActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_web);

        preferences = getSharedPreferences("app_master", MODE_PRIVATE);

        Button applyButton = findViewById(R.id.apply_button_2);
        ImageView backButton = findViewById(R.id.back_button2);
        webView = findViewById(R.id.mentor_webview);
        progressBar = findViewById(R.id.progress_bar);

        applyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MentorWebActivity.this, MentorActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> finish());

        // Set up WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE); // Show ProgressBar when page starts loading
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE); // Hide ProgressBar when page finishes loading
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false; // Allow the WebView to handle URL loading
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE); // Hide ProgressBar on error
                // Optionally, display an error message or load a local error page
            }
        });

        // Load the URL from SharedPreferences
        String url = preferences.getString("mentorship_webview_link", "https://www.gamazingstudios.com/");
        if (!url.isEmpty()) {
            webView.loadUrl(url);
        }

        // Handle Download button click
        findViewById(R.id.download_2).setOnClickListener(v -> {
            String downloadLink = preferences.getString("mentorship_download_link", "https://www.gamazingstudios.com/");
            if (!downloadLink.isEmpty()) {
                Uri uri = Uri.parse(downloadLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // Handle Share button click
        findViewById(R.id.share_2).setOnClickListener(v -> {
            String shareLink = preferences.getString("mentorship_share_link", "https://www.gamazingstudios.com/");
            if (!shareLink.isEmpty()) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
