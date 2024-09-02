package com.example.uiuxapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_privacy_policy);
        preferences = getSharedPreferences("app_master", MODE_PRIVATE);
        // Initialize WebView and ProgressBar
        webView = findViewById(R.id.article_webview);
        progressBar = findViewById(R.id.progress_bar_policy);
        backButton = findViewById(R.id.back_button_profile);

        // Set up WebView
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(ProgressBar.VISIBLE); // Show progress bar while loading
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(ProgressBar.GONE); // Hide progress bar when done
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false; // Open URLs within the WebView
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE); // Hide ProgressBar on error
                // Optionally, display an error message or load a local error page
            }
        });

        String webViewUrl = preferences.getString("training_webview_link", "https://www.gamazingstudios.com/");
        if (!webViewUrl.isEmpty()) {
            webView.loadUrl(webViewUrl);
        }

        // Adjust padding to account for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack(); // Go back to the previous page if possible
            } else {
                onBackPressed(); // Otherwise, use the default back press behavior
            }
        });
    }}