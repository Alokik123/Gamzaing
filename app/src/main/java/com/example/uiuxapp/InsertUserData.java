package com.example.uiuxapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class InsertUserData {
    public static void insertUser(Context context, RequestQueue requestQueue, String id, String name, String email, String mobile, String profilePhoto, String referralCode, String fcmToken, String authToken, String createdAt, InsertUserCallback callback) {
        String url = "https://app-api.gamazingstudios.com/Routes/users.php";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("InsertLoginData", true);
            jsonBody.put("AndroidDevelopmentServer", true);
            jsonBody.put("id", id);
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("mobile", mobile);
            jsonBody.put("profile_photo", profilePhoto);
            jsonBody.put("referal_code", referralCode);
            jsonBody.put("fcmtoken", fcmToken);
            jsonBody.put("authtoken", authToken);
            jsonBody.put("created_at", createdAt);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.getString("message").equals("DATA INSERTED")) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> callback.onError());

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public interface InsertUserCallback {
        void onSuccess();
        void onFailure();
        void onError();
    }
}
