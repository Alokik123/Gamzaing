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

public class InternshipWebActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internship_web);

        preferences = getSharedPreferences("app_master", MODE_PRIVATE);

        Button applyButton = findViewById(R.id.apply_button_3);
        ImageView backButton = findViewById(R.id.back_button3);
        webView = findViewById(R.id.internship_webview);
        progressBar = findViewById(R.id.progress_bar);

        applyButton.setOnClickListener(v -> {
            Intent intent = new Intent(InternshipWebActivity.this, InternshipActivity.class);
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
        String webViewUrl = preferences.getString("internship_webview_link", "https://www.gamazingstudios.com/");
        if (!webViewUrl.isEmpty()) {
            webView.loadUrl(webViewUrl);
        }

        // Handle the Download button click
        findViewById(R.id.download_1).setOnClickListener(v -> {
            String downloadLink = preferences.getString("internship_download_link", "https://www.gamazingstudios.com/");
            if (!downloadLink.isEmpty()) {
                Uri uri = Uri.parse(downloadLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // Handle the Share button click
        findViewById(R.id.share_1).setOnClickListener(v -> {
            String shareLink = preferences.getString("internship_share_link", "https://www.gamazingstudios.com/");
            if (!shareLink.isEmpty()) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


    // Handle the Back button click
        backButton.setOnClickListener(v -> {
        if (webView.canGoBack()) {
            webView.goBack(); // Go back to the previous page if possible
        } else {
            onBackPressed(); // Otherwise, use the default back press behavior
        }
    });
}

@Override
public void onBackPressed() {
    if (webView.canGoBack()) {
        webView.goBack(); // Go back to the previous page if possible
    } else {
        super.onBackPressed(); // Otherwise, perform the default back press action
    }
}
}
