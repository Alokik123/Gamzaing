//package com.example.uiuxapp;
//
//import static com.example.uiuxapp.Base64Utils.data;
//
//import android.util.Base64;
//
//public class Base64Encode {
//
//    public Base64Encode(String string) {
//    }
//
//    // Encode the data to Base64
//    public static String encode() {
//        if (data == null) {
//            return null;
//        }
//        return Base64.encodeToString(data.getBytes(), Base64.NO_WRAP);
//    }
//
//    // Decode the data from Base64
//    public static String decode(String data) {
//        if (data == null) {
//            return null;
//        }
//        byte[] decodedBytes = Base64.decode(data, Base64.NO_WRAP);
//        return new String(decodedBytes);
//    }
//}
package com.example.uiuxapp;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.util.Base64;

public class Base64Encode {

    String data;

    public Base64Encode(String data) {
        this.data = data;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encode(){
        if (TextUtils.isEmpty(data)){
            return "";
        }
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decode(){
        if (TextUtils.isEmpty(data)){
            return "";
        }
        byte[] decode = Base64.getDecoder().decode(data);
        return new String(decode);
    }
}
