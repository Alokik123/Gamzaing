package com.example.uiuxapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchUserData {
    public static void fetchUser(Context context, RequestQueue requestQueue, String email, FetchUserCallback callback) {
        String url = "https://app-api.gamazingstudios.com/Routes/users.php";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("FetchLoginData", true);
            jsonBody.put("AndroidDevelopmentServer", true);
            jsonBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        if (response.getString("message").equals("DATA FOUND")) {
                            callback.onSuccess(response.getJSONArray("response").getJSONObject(0));
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

    public interface FetchUserCallback {
        void onSuccess(JSONObject userData);
        void onFailure();
        void onError();
    }
}
