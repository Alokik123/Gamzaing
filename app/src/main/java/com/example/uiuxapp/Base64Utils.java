package com.example.uiuxapp;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.util.Base64;

public class Base64Utils {

    // Method to encode a string
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encode(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        return Base64.getEncoder().encodeToString(s.getBytes());
    }

    // Method to decode a Base64-encoded string
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decode(String base64EncodedResponse) {
        if (TextUtils.isEmpty(base64EncodedResponse)) {
            return "";
        }
        try {
            byte[] decode = Base64.getDecoder().decode(base64EncodedResponse);
            return new String(decode);
        } catch (IllegalArgumentException e) {
            // Handle the case where the input is not valid Base64
            e.printStackTrace();
            return "";
        }
    }
}
