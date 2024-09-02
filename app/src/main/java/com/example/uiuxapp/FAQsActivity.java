package com.example.uiuxapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FAQsActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private ImageView backButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqs_screen);

        // Initialize WebView, ProgressBar, and back button
        webView = findViewById(R.id.article_webview_faqs);
        progressBar = findViewById(R.id.progress_bar_faqs);
        backButton = findViewById(R.id.back_button_faqs);

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
                Toast.makeText(FAQsActivity.this, "Failed to load page", Toast.LENGTH_SHORT).show();
            }
        });

        // Load the URL
        String webViewUrl = "https://www.gamazingstudios.com/";
        webView.loadUrl(webViewUrl);

        // Set up back button
        backButton.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack(); // Go back to the previous page if possible
            } else {
                onBackPressed(); // Otherwise, use the default back press behavior
            }
        });
    }
}
