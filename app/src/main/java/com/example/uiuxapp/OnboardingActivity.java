package com.example.uiuxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;
    private TextView skipText;
    private Button nextButton;
    private ImageView[] indicators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.view_pager);
        skipText = findViewById(R.id.skip_text);
        nextButton = findViewById(R.id.next_button);

        adapter = new OnboardingAdapter(this);
        viewPager.setAdapter(adapter);

        // Initialize indicators
        initializeIndicators();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);

                // Update button text based on the current page
                if (position == adapter.getItemCount() - 1) {
                    nextButton.setText("Finish");
                } else {
                    nextButton.setText("Next");
                }
            }
        });

        // Set click listeners for the Skip text and Next button
        skipText.setOnClickListener(v -> redirectToEmailActivity());

        nextButton.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() == adapter.getItemCount() - 1) {
                // Last page, redirect to email activity
                redirectToEmailActivity();
            } else {
                // Move to the next page
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
    }

    private void initializeIndicators() {
        indicators = new ImageView[adapter.getItemCount()];
        LinearLayout indicatorLayout = findViewById(R.id.indicator_layout);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = (ImageView) indicatorLayout.getChildAt(i);

            // Set initial indicator image
            indicators[i].setImageResource(i == 0 ? R.drawable.ellipse_selected : R.drawable.ellipse_unselected);
        }
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setImageResource(i == position ? R.drawable.ellipse_selected : R.drawable.ellipse_unselected);
        }
    }

    private void redirectToEmailActivity() {
        Intent intent = new Intent(OnboardingActivity.this, EmailActivity.class);
        startActivity(intent);
        finish(); // Finish onboarding activity to prevent going back
    }
}
