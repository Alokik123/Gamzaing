package com.example.uiuxapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MyAccountActivity extends AppCompatActivity {

    private ImageView profileImageView, backbutton;
    private TextView nameTextView, phoneTextView;
    private SharedPreferences preferences;
    private CardView profileCard, privacyPolicyCard, aboutUsCard, faqsCard;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;

    private BroadcastReceiver refreshProfilePhotoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("REFRESH_PROFILE_PHOTO")) {
                loadProfileData(); // Refresh the profile data
                Log.d("MyAccountActivity", "Received broadcast to refresh profile photo");
            }
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        // Initialize views
        mAuth= FirebaseAuth.getInstance();
        profileImageView = findViewById(R.id.profile_orange_svg_1);
        backbutton = findViewById(R.id.back_button_account);
        nameTextView = findViewById(R.id.name_text_view);
        phoneTextView = findViewById(R.id.number_text_view);
        Button logoutButton = findViewById(R.id.logout_button);
        profileCard = findViewById(R.id.profile_card);
        privacyPolicyCard = findViewById(R.id.privacy_policy);
        aboutUsCard = findViewById(R.id.about_us);
        faqsCard = findViewById(R.id.faqs_card);
        registerReceiver(refreshProfilePhotoReceiver, new IntentFilter("REFRESH_PROFILE_PHOTO"));


        preferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);

        // Initialize Google Sign-In client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        loadProfileData();

        // Set click listeners
        backbutton.setOnClickListener(v -> onBackPressed());

        profileCard.setOnClickListener(v -> {
            // Navigate to ProfileActivity to update profile using SharedPreferences
            Intent intent = new Intent(MyAccountActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        privacyPolicyCard.setOnClickListener(v -> {
            Intent intent = new Intent(MyAccountActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        aboutUsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MyAccountActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });

        faqsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MyAccountActivity.this, FAQsActivity.class);
            startActivity(intent);
        });


        // Logout button implementation
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Google
                googleSignInClient.signOut()
                        .addOnCompleteListener(MyAccountActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Sign out from Firebase
                                mAuth.signOut();
                                SharedPreferences preferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
                                preferences.edit().clear().apply();

                                Intent intent = new Intent(MyAccountActivity.this, SplashActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(refreshProfilePhotoReceiver);
    }
            private void loadProfileData() {
                // Retrieve profile data from SharedPreferences
                String profilePhotoUrl = preferences.getString("profilePhotoUrl", "");
                String name = preferences.getString("name", "Aloo");
                String mobile = preferences.getString("mobile", "9xxxx");
                // Log the retrieved values
                Log.d("MyAccountActivity", "Retrieved profile photo URL: " + profilePhotoUrl);
                Log.d("MyAccountActivity", "Retrieved name: " + name);
                Log.d("MyAccountActivity", "Retrieved mobile: " + mobile);

                // Load profile photo using Glide
                if (!profilePhotoUrl.isEmpty()) {
                    Glide.with(this).load(profilePhotoUrl).placeholder(R.drawable.profile_orange_svg).into(profileImageView);
                } else {
                    profileImageView.setImageResource(R.drawable.profile_orange_svg);
                }

                // Set name and phone number
                nameTextView.setText(name);
                phoneTextView.setText(mobile);
            }
        }