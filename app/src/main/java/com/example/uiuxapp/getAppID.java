package com.example.uiuxapp;

import android.content.Context;

import com.makeramen.roundedimageview.BuildConfig;

public class getAppID {

    Context context;

    public getAppID(Context context) {
        this.context = context;
    }

    public String appID(){

        return "com.example.uiuxapp";
    }
}
