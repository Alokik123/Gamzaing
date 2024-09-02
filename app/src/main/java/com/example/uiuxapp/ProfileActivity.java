package com.example.uiuxapp;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final String KEY_REFERRAL_CODE = "referal_code";

    private static final String TAG = "ProfileActivity";
    private static final String PREFS_NAME = "UserPrefs";
    private static final int RESULT_OK = 1;  // Make sure RESULT_OK is defined if not already

    private RoundedImageView profilePhoto;
    private TextView nameTextProfile, referralCodeTextView;
    private EditText nameEditTextProfile, emailEditTextProfile, numberEditTextProfile, dobEditTextProfile;
    private Button applyButtonCard;
    private SharedPreferences preferences;

    private boolean isNewUser;
    private String originalMobile;
    private String referralCode; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        initViews();
        loadProfileData();

        applyButtonCard.setOnClickListener(v -> {
            if (updateProfile()) {
                sendProfileDataToServer();
            }
        });

        findViewById(R.id.back_button_profile).setOnClickListener(v -> onBackPressed());
        dobEditTextProfile.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dateOfBirth = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dobEditTextProfile.setText(dateOfBirth);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void initViews() {
        profilePhoto = findViewById(R.id.profile_orange_svg_1);
        nameTextProfile = findViewById(R.id.name_text_profile);
        referralCodeTextView = findViewById(R.id.referral_code_text_view); // Update ID as needed
        nameEditTextProfile = findViewById(R.id.name_edittext_profile);
        emailEditTextProfile = findViewById(R.id.email_edittext_profile);
        numberEditTextProfile = findViewById(R.id.number_edittext_profile);
        dobEditTextProfile = findViewById(R.id.dob_edittext_profile);
        applyButtonCard = findViewById(R.id.apply_button_card);

        preferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
    }

    private void loadProfileData() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String profilePhotoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";
            storeProfilePhotoUrlInPreferences(profilePhotoUrl); // Store the URL in SharedPreferences

            if (!profilePhotoUrl.isEmpty()) {
                Glide.with(this).load(profilePhotoUrl).placeholder(R.drawable.profile_orange_svg).into(profilePhoto);
            } else {
                profilePhoto.setImageResource(R.drawable.profile_orange_svg);
            }

            String name = account.getDisplayName();
            String email = account.getEmail();
            String mobile = preferences.getString("mobile", "");
            String dob = preferences.getString("dob", "");
            isNewUser = preferences.getBoolean("isNewUser", false);

            originalMobile = mobile;

            nameTextProfile.setText(name);
            nameEditTextProfile.setText(name);
            emailEditTextProfile.setText(email);
            numberEditTextProfile.setText(mobile);
            dobEditTextProfile.setText(dob);

            loadReferralCode(); // Load and display referral code

            emailEditTextProfile.setEnabled(false);
            emailEditTextProfile.setOnClickListener(v -> Toast.makeText(this, "You cannot change your email.", Toast.LENGTH_SHORT).show());

            numberEditTextProfile.setEnabled(true);
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeProfilePhotoUrlInPreferences(String profilePhotoUrl) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profilePhotoUrl", profilePhotoUrl);
        editor.apply();
        Log.d("ProfileActivity", "Saved profile photo URL: " + profilePhotoUrl);

    }

    private void loadReferralCode() {
        SharedPreferences preferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        referralCode = preferences.getString(KEY_REFERRAL_CODE, "No referral code available");
        Log.d(TAG, "Retrieved Referral Code: " + referralCode);
        referralCodeTextView.setText(referralCode);
        Log.d(TAG, "Referral Code Text View: " + referralCodeTextView.getText());
    }
    private boolean updateProfile() {
        String name = nameEditTextProfile.getText().toString().trim();
        String email = emailEditTextProfile.getText().toString().trim();
        String mobile = numberEditTextProfile.getText().toString().trim();
        String dob = dobEditTextProfile.getText().toString().trim();

        boolean valid = true;

        if (isNewUser) {
            if (name.isEmpty()) {
                nameEditTextProfile.setError("Name is required");
                valid = false;
            }
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditTextProfile.setError("Valid email is required");
                valid = false;
            }
            if (mobile.isEmpty() || mobile.length() < 10) {
                numberEditTextProfile.setError("Valid mobile number is required");
                valid = false;
            }
            if (dob.isEmpty()) {
                dobEditTextProfile.setError("Date of birth is required");
                valid = false;
            }
        } else {
            if (name.isEmpty()) {
                nameEditTextProfile.setError("Name is required");
                valid = false;
            }
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditTextProfile.setError("Valid email is required");
                valid = false;
            }
            if (dob.isEmpty()) {
                dobEditTextProfile.setError("Date of birth is required");
                valid = false;
            }
        }

        if (valid) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.putString("mobile", mobile);
            editor.putString("dob", dob);
            editor.apply();
            Log.d("ProfileActivity", "Saved name: " + name);
            Log.d("ProfileActivity", "Saved mobile: " + mobile);


            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Profile updated successfully!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(android.R.color.holo_green_dark));

            // Set custom margin
            View snackbarView = snackbar.getView();
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
            params.setMargins(20, 0, 20, 205); // Adjust bottom margin as needed
            snackbarView.setLayoutParams(params);

            snackbar.show();
            updateProfileInformation(); // Call the method here

        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please fill all fields correctly.", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark));

            // Set custom margin
            View snackbarView = snackbar.getView();
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
            params.setMargins(20, 0, 20, 205); // Adjust bottom margin as needed
            snackbarView.setLayoutParams(params);

            snackbar.show();
        }

        return valid;
    }
    private void updateProfileInformation() {
        // Update profile information logic here
        // ...

        // Send a broadcast to notify HomeActivity to refresh profile photo
        Intent intent = new Intent("REFRESH_PROFILE_PHOTO");
        sendBroadcast(intent);
    }

    private void sendProfileDataToServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, ServerURL.SERVER_PROFILE,
                response -> {
                    Log.d(TAG, "Response: " + response);

//                    Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
//                    startActivity(homeIntent);
                },
                error -> {
                    Log.e(TAG, "Error: " + error.toString());
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error updating profile.", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark));

                    // Set custom margin
                    View snackbarView = snackbar.getView();
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
                    params.setMargins(0, 0, 0, 200); // Adjust bottom margin as needed
                    snackbarView.setLayoutParams(params);

                    snackbar.show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", nameEditTextProfile.getText().toString().trim());
                params.put("email", emailEditTextProfile.getText().toString().trim());
                params.put("mobile", numberEditTextProfile.getText().toString().trim());
                params.put("dob", dobEditTextProfile.getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
