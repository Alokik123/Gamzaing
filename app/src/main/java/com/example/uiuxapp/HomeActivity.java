package com.example.uiuxapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri; import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64; // Remove if not using Base64 import java.util.HashMap; import java.util.List; import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final String SHARE_URL = "https://www.gamazingstudios.com/";
    private static final String BANNERS_URL = "https://app-api.gamazingstudios.com/Routes/banners.php";
    private ViewPager2 viewPager;
    private BannersAdapter bannersAdapter;
    private List<String> bannerImageUrls = new ArrayList<>();
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 3000; // Delay in milliseconds
    private int period = 3000; // Period in milliseconds
    private SharedPreferences preferences;
    private ImageView profilePicture; // Added for profile photo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // Initialize SharedPreferences and views
        preferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        viewPager = findViewById(R.id.viewPager);
        profilePicture = findViewById(R.id.profile_picture);
        ImageView whatsappIcon = findViewById(R.id.i12);
        ImageView instagramIcon = findViewById(R.id.i13);
        ImageView facebookIcon = findViewById(R.id.i14);
        ImageView linkedinIcon = findViewById(R.id.i15);


        // Set up ViewPager2
        bannersAdapter = new BannersAdapter(bannerImageUrls);
        viewPager.setAdapter(bannersAdapter);
        String authToken = preferences.getString("authtoken", "");


        profilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
            startActivity(intent);
        });

        ImageView imageView6 = findViewById(R.id.i6);
        imageView6.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TrainingWebActivity.class);
            startActivity(intent);
        });

        ImageView imageView7 = findViewById(R.id.i7);
        imageView7.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, InternshipWebActivity.class);
            startActivity(intent);
        });

        Button signUpButton = findViewById(R.id.Sign_Up);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MentorWebActivity.class);
            startActivity(intent);
        });

        com.google.android.material.card.MaterialCardView shareCardView = findViewById(R.id.share_with_friends);
        shareCardView.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            SharedPreferences preferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
            String referralCode = preferences.getString("referal_code", "");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Share your referral code: " + referralCode);
            startActivity(Intent.createChooser(shareIntent, "Share Referral Code"));
        });

        com.google.android.material.card.MaterialCardView websiteCardView = findViewById(R.id.website);
        websiteCardView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SHARE_URL));
            startActivity(browserIntent);
        });

        com.google.android.material.card.MaterialCardView myAccountCardView = findViewById(R.id.my_account);
        myAccountCardView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
            startActivity(intent);
        });

        whatsappIcon.setOnClickListener(v -> openUrl("https://web.whatsapp.com/"));
        instagramIcon.setOnClickListener(v -> openUrl("https://www.instagram.com/"));
        facebookIcon.setOnClickListener(v -> openUrl("https://www.facebook.com/"));
        linkedinIcon.setOnClickListener(v -> openUrl("https://in.linkedin.com/"));

        // Fetch banners from the server
        FetchBannersData();

        loadProfilePicture(); // Load profile data if needed
    }

    // Method to open a URL in a browser
    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void FetchBannersData() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("GetBannersData", "true");
            jsonData.put("AndroidDevelopmentServer", "true");
            String base64EncodedRequest = new Base64Encode(String.valueOf(jsonData)).encode();
            jsonObject.put("request", base64EncodedRequest);
            Log.e(TAG, "Base64 Encoded Request: " + base64EncodedRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,ServerURL.SERVER_BANNERS, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.optInt("countData", 0) > 0) {
                        JSONArray jsonArray = response.getJSONArray("response");
                        if (jsonArray.length() > 0) {
                            bannerImageUrls.clear(); // Clear the list before adding new data
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject fetchData = jsonArray.getJSONObject(i);
                                String bannerImageUrl = fetchData.getString("banner_image");
                                if (!TextUtils.isEmpty(bannerImageUrl)) {
                                    bannerImageUrls.add(bannerImageUrl);
                                    Log.d(TAG, "Banner Image URL: " + bannerImageUrl);
                                }
                            }
                            if (!bannerImageUrls.isEmpty()) { // Check if the list is not empty
                                bannersAdapter.notifyDataSetChanged();
                                startAutoSlide();
                            } else {
                                Log.d(TAG, "No banners available.");
                            }
                        } else {
                            Log.d(TAG, "No banners available.");
                        }
                    } else {
                        Log.d(TAG, "Count less than 0.");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage();
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errorMessage = new String(error.networkResponse.data);
                        }
                        Log.e(TAG, "Error fetching banners data: " + errorMessage);
                        Toast.makeText(HomeActivity.this, "Error fetching banners", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String authToken = preferences.getString("authtoken", "");
                Log.d(TAG, "Auth Token: " + authToken);
                map.put("Authorization",  authToken);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void startAutoSlide() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() == bannerImageUrls.size() - 1) {
                    viewPager.setCurrentItem(0, true);
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
                handler.postDelayed(this, period);
            }
        };
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void loadProfilePicture() {
        SharedPreferences preferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        String profilePhotoUrl = preferences.getString("profilePhotoUrl", "");

        if (!profilePhotoUrl.isEmpty()) {
            Glide.with(this).load(profilePhotoUrl).placeholder(R.drawable.profile_orange_svg).into(profilePicture);
        } else {
            profilePicture.setImageResource(R.drawable.profile_orange_svg);
        }
    }}