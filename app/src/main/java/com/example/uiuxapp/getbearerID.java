package com.example.uiuxapp;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class getbearerID {

    Context context;

    public getbearerID(Context context) {
        this.context = context;
    }

    @SuppressLint({"HardwareIds", "MissingPermission", "RestrictedApi"})
    public String deviceID() {

        String deviceId = null;

        // Log the Android version
        Log.d(TAG, "Android Version: " + Build.VERSION.RELEASE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android Q and above
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Fetched device ID using ANDROID_ID for Android Q and above: " + deviceId);
        } else {
            // For below Android Q
            TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony != null) {
                deviceId = mTelephony.getDeviceId();
                if (deviceId != null) {
                    Log.d(TAG, "Fetched device ID using getDeviceId() for pre-Android Q: " + deviceId);
                } else {
                    // Fallback if getDeviceId() is null
                    deviceId = Settings.Secure.getString(
                            context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    Log.d(TAG, "getDeviceId() returned null, fallback to ANDROID_ID: " + deviceId);
                }
            } else {
                // Fallback if TelephonyManager is null
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Log.d(TAG, "TelephonyManager is null, fallback to ANDROID_ID: " + deviceId);
            }
        }

        // Log the final device ID
        Log.d(TAG, "Final Device ID: " + deviceId);
        return deviceId;
    }
}
