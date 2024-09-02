package com.example.uiuxapp;

//import static com.example.uiuxapp.EmailActivity.KEY_REFERRAL_CODE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final String SERVER_URL = "https://app-api.gamazingstudios.com/Routes/device_info.php";
    private static final int RC_SIGN_IN = 9001;
    private SharedPreferences preferences;
    private static final int TOAST_DISPLAY_DURATION = 3000; // 5 seconds

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Configure Google Sign-In
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check if the user is already signed in
        checkUserSignInStatus();

    }

    private void checkUserSignInStatus() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            // User is signed in with Google
            String email = account.getEmail();
            String username = account.getDisplayName();
            Log.d(TAG, "User Email: " + email);
            Log.d(TAG, "Username: " + username);

            new Handler().postDelayed(() -> {
                Intent homeIntent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }, TOAST_DISPLAY_DURATION);

        } else {
            // User is not signed in, proceed with normal flow
            generateAndSendDeviceToken();

            // Proceed to OnboardingActivity after a delay
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
                startActivity(intent);
                finish();
            }, 1000);
        }
    }

    private void generateAndSendDeviceToken() {
        // Generate a random UUID as device token
        String deviceToken = UUID.randomUUID().toString();
        Log.d(TAG, "Device Token: " + deviceToken);

        // Store the device token in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DEVICE_TOKEN", deviceToken);
        editor.apply();

        // Send the device token to your server
        sendTokenToServer(deviceToken);
    }

    private void sendTokenToServer(String deviceToken) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("app_id", getPackageName());
            jsonBody.put("device_token", deviceToken);
            jsonBody.put("InsertDeviceInfo", "true");
            jsonBody.put("AndroidDevelopmentServer", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, SERVER_URL, jsonBody,
                response -> Log.d(TAG, "Response: " + response.toString()),
                error -> Log.e(TAG, "Error: " + error.toString())
        );

        requestQueue.add(jsonObjectRequest);
    }
}