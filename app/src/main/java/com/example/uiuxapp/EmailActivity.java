package com.example.uiuxapp;

import android.app.ProgressDialog; import android.content.Intent; import android.content.SharedPreferences; import android.os.Bundle; import android.support.annotation.NonNull; import android.util.Log; import android.widget.Button; import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener; import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError; import com.android.volley.Request; import com.android.volley.RequestQueue; import com.android.volley.Response; import com.android.volley.VolleyError; import com.android.volley.toolbox.JsonObjectRequest; import com.android.volley.toolbox.StringRequest; import com.android.volley.toolbox.Volley; import com.google.android.gms.auth.api.signin.GoogleSignIn; import com.google.android.gms.auth.api.signin.GoogleSignInAccount; import com.google.android.gms.auth.api.signin.GoogleSignInClient; import com.google.android.gms.auth.api.signin.GoogleSignInOptions; import com.google.android.gms.common.api.ApiException; import com.google.android.gms.tasks.Task; import com.google.firebase.auth.AuthCredential; import com.google.firebase.auth.FirebaseAuth; import com.google.firebase.auth.FirebaseUser; import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray; import org.json.JSONException; import org.json.JSONObject;

import java.util.HashMap; import java.util.Map; import java.util.Random;

public class EmailActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String fcmToken;

    private String email; // Define variable for email
    private String referralCode;
    private RequestQueue requestQueue;
    private String webClientId = "961613805468-t18fuo1llnibe18djm1si7ro3bddgrhh.apps.googleusercontent.com";
    private static final String SERVER_CHECK_USER = "https://app-api.gamazingstudios.com/Routes/users.php";
    private static final String SERVER_REFFERAL_CODE = "https://app-api.gamazingstudios.com/Routes/refferal_code.php";
    private ProgressDialog progressDialog;
    static final String PREFS_NAME = "UserPrefs";

    public static final String KEY_REFERRAL_CODE = "referal_code";
    private static final String KEY_APP_ID = "appId";
    private static final String KEY_DEVICE_TOKEN = "deviceToken";
    private static final String KEY_FCM_TOKEN = "fcmToken";
    private static final String KEY_AUTH_TOKEN = "authToken";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_screen);

        // Initialize Firebase Auth and Volley RequestQueue
        mAuth = FirebaseAuth.getInstance();
        requestQueue = Volley.newRequestQueue(this);
        preferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.d("EmailActivity", "GoogleSignInClient initialized");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Set to horizontal style
        progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.horizontal_progress_bar)); // Use custom drawable
        progressDialog.setCancelable(false);

        // Check if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("EmailActivity", "User is already signed in, fetching user data.");
            // Fetch user data if already signed in
            progressDialog.show();
            sendUserDataToServer(currentUser);
            return;
        }

        // Set onClickListener for continue button
        Button continueButton = findViewById(R.id.continue_1);
        continueButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                // User is already authenticated with Firebase
                Log.d("EmailActivity", "User is already signed in");
                // Redirect user to HomeActivity
                startActivity(new Intent(EmailActivity.this, HomeActivity.class));
                finish();
            } else {
                // User is not authenticated with Firebase
                Log.d("EmailActivity", "User is not signed in");
                // Prompt user to sign in with Google again
                signIn();
            }
        });


        // Check if referral code is passed
        String referralCode = getReferralCode();
        if (referralCode != null) {
            Log.d("EmailActivity", "Referral code retrieved: " + referralCode);
            // Optionally use referral code here
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d("EmailActivity", "Google Sign-In intent started.");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d("EmailActivity", "Handling sign-in result.");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("EmailActivity", "Google sign-in successful.");
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            String errorMessage = "Google sign-in failed. Error code: " + e.getStatusCode();
            Log.w("EmailActivity", errorMessage);
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("EmailActivity", "Firebase authentication successful.");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail();
                            checkIfUserExists(email);
                        }
                    } else {
                        String errorMessage = "Authentication Failed. " + (task.getException() != null ? task.getException().getMessage() : "Unknown error");
                        Log.w("EmailActivity", errorMessage);
                        Toast.makeText(EmailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkIfUserExists(String email) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("FetchLoginData", "true");
            jsonData.put("AndroidDevelopmentServer", "true");
            jsonData.put("email", email);
            jsonObject.put("request", new Base64Encode(String.valueOf(jsonData)).encode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SERVER_CHECK_USER, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CheckUsersExistsOrNot", "Response: " + response.toString()); // Log the response
                        try {
                            if (Integer.parseInt(String.valueOf(response.get("countData"))) == 0) {
//                                Toast.makeText(EmailActivity.this, "User not founded", Toast.LENGTH_SHORT).show();
                                registerUser();
                            } else {
                                JSONArray jsonArray = response.getJSONArray("response"); for (int i = 0; i < jsonArray.length(); i++) { JSONObject fetchData = jsonArray.getJSONObject(i);

                                    SharedPreferences.Editor edit = preferences.edit();
                                    edit.putBoolean("isLogin", true);
                                    edit.putString("id", fetchData.getString("id"));
                                    edit.putString("name", fetchData.getString("name"));
                                    edit.putString("email", fetchData.getString("email"));
                                    edit.putString("mobile", fetchData.getString("mobile"));
                                    edit.putString("profile_photo", fetchData.getString("profile_photo"));
                                    edit.putString("referal_code", fetchData.getString("referal_code"));
                                    edit.putString("fcmtoken", fetchData.getString("fcmtoken"));
                                    edit.putString("authtoken", fetchData.getString("authtoken"));
                                    edit.apply();
                                    Toast.makeText(EmailActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EmailActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CheckUsersExistsOrNot", "Error: " + error.toString()); // Log the error
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + new getbearerID(EmailActivity.this).deviceID());
                headers.put("device_token", new getDeviceID(EmailActivity.this).deviceID());
                headers.put("app_id", new getAppID(EmailActivity.this).appID());
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private String generateReferralCode(String email) {
        StringBuilder referralCode = new StringBuilder();

        String username = email.split("@")[0];

        referralCode.append(username.toUpperCase());

        Random random = new Random();
        int randomNum1 = random.nextInt(10);  // Generates a number between 0-9
        int randomNum2 = random.nextInt(10);  // Generates another number between 0-9

        referralCode.append(randomNum1).append(randomNum2);

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 2; i++) {  // Add 2 random characters
            int index = random.nextInt(characters.length());
            referralCode.append(characters.charAt(index));
        }

        return referralCode.toString();
    }


    private void registerUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String id = new DateTimeClass().getCurrectMillis() + new Random().nextInt(10000);
            String name = user.getDisplayName();
            String email = user.getEmail();
            String profilePhotoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("profilePhotoUrl", profilePhotoUrl);
            edit.apply();
            String mobile = "";
            String referralCode = generateReferralCode(email);
            String authToken = generateAuthToken();
            Log.d("EmailActivity", "Registering user with referral code: " + referralCode);

            StringRequest request = new StringRequest(Request.Method.POST, ServerURL.SERVER_USERS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("EmailActivity", "User registration response: " + response);
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putBoolean("isLogin", true);
                            edit.putString("id", id);
                            edit.putString("name", name);
                            edit.putString("email", email);
                            edit.putString("mobile", mobile);
                            edit.putString("profile_photo", profilePhotoUrl);
                            edit.putString("referal_code", referralCode);
                            edit.putString("authtoken", authToken);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("authtoken", authToken);
                            editor.apply();
                                    // edit.putString("fcmtoken", fcmToken); edit.apply();
                            sendReferralCode(email, referralCode);
                            EmailScreenPopup emailScreenPopup = EmailScreenPopup.newInstance(email, referralCode);
                            emailScreenPopup.show(getSupportFragmentManager(), "EmailScreenPop");
                            Toast.makeText(EmailActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("EmailActivity", "User registration failed: " + error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + new getbearerID(EmailActivity.this).deviceID());
//                    Log.d("EmailActivity", "Bearer Token: " + new getbearerID(EmailActivity.this).deviceID());
                    headers.put("device_token", new getDeviceID(EmailActivity.this).deviceID());
                    headers.put("app_id", new getAppID(EmailActivity.this).appID());
                    return headers; }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("InsertLoginData", "true");
                    params.put("AndroidDevelopmentServer", "true");
                    params.put("name", name);
                    params.put("email", email);
                    params.put("mobile", mobile);
                    params.put("profile_photo", profilePhotoUrl);
                    params.put("referal_code", referralCode);
                    params.put("authtoken", authToken);
                    params.put("created_at", String.valueOf(System.currentTimeMillis()));
                    return params;
                }


            };

            requestQueue.add(request);
        }
    }
    private String generateAuthToken() {
        return "Auth_" + System.currentTimeMillis() + new Random().nextInt(10000);
    }

    private String getReferralCode() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String referralCode = preferences.getString(KEY_REFERRAL_CODE, null);
        Log.d("EmailActivity", "Retrieved referral code: " + referralCode);
        return referralCode;
    }

    private void sendUserDataToServer(FirebaseUser user) {
        if (user != null) { String name = user.getDisplayName();
            String email = user.getEmail();
            String mobile = ""; // Default to empty if not available
            String profilePhoto = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";

            String authToken = "Auth_" + System.currentTimeMillis() + new Random().nextInt(10000);
            String fcmToken = ""; // Default to empty if not available

            // Generate referral code and save it to SharedPreferences
            String referralCode = generateReferralCode(email);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("referal_code", referralCode);
            editor.apply();

            // Get App ID and Device Token
            String appId = String.valueOf(new getAppID(this));
            String deviceToken = String.valueOf(new getDeviceID(this));

            StringRequest request = new StringRequest(Request.Method.POST, SERVER_CHECK_USER,
                    response ->{ Log.d("EmailActivity", "User data sent successfully. Response: " + response);
                        progressDialog.dismiss();
                    },

                    error -> {
                        if (error.networkResponse != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            Log.e("EmailActivity", "Error code: " + error.networkResponse.statusCode);
                            Log.e("EmailActivity", "Error body: " + errorResponse);
                            Toast.makeText(EmailActivity.this, "Error: " + errorResponse, Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("EmailActivity", "Error: " + error.toString());
                            Toast.makeText(EmailActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    data.put("InsertLoginData", "true");
                    data.put("AndroidDevelopmentServer", "true");
                    data.put("name", name);
                    data.put("email", email);
                    data.put("mobile", mobile);
                    data.put("profile_photo", profilePhoto);
                    data.put("referal_code", referralCode);
                    data.put("fcmtoken", fcmToken);
                    data.put("authtoken", authToken);
                    Log.d("EmailActivity", "auth code: " + authToken);
                    data.put("created_at", new DateTimeClass().millisToDate(new DateTimeClass().getCurrectMillis(), "HH:mm:ss dd-MM-yyyy"));

                    return data;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + new getbearerID(EmailActivity.this).deviceID());
                    headers.put("device_token", new getDeviceID(EmailActivity.this).deviceID());
                    headers.put("app_id", new getAppID(EmailActivity.this).appID());
                    return headers;
                }
            };

            requestQueue.add(request);
        }
    }
    private void sendReferralCode(String email, String referralCode) {
        String authToken = "Auth_" + System.currentTimeMillis() + new Random().nextInt(10000); // Generate the authorization token
        String id = new DateTimeClass().getCurrectMillis() + new Random().nextInt(10000);

        StringRequest request = new StringRequest(Request.Method.POST, SERVER_REFFERAL_CODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("EmailActivity", "Referral code response: " + response);
                        // Handle the response from the server here
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("EmailActivity", "Referral code request failed: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("InsertReferralDataData","true");
                params.put("AndroidDevelopmentServer", "true");
                params.put("rcode_used_by_email", email);
                params.put("rcode_used_by_id",id);
                params.put("created_at", String.valueOf(System.currentTimeMillis()));
                params.put("referal_code", referralCode);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization" , authToken);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(request);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("EmailActivity", "onStart called");
    }

//    private void showEmailScreenPop() {
//        if (email != null) {
//            EmailScreenPopup emailScreenPopup = EmailScreenPopup.newInstance(email, referralCode);
//            emailScreenPopup.show(getSupportFragmentManager(), "EmailScreenPop");
//        } else {
//            Log.e("EmailActivity", "Email is null.");
//        }
//    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("EmailActivity", "onStop called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("EmailActivity", "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("EmailActivity", "onPause called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("EmailActivity", "onDestroy called");
    }
}
