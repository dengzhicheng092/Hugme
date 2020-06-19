package com.qboxus.hugme.LiveStreaming.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.qboxus.hugme.LiveStreaming.Constants;


public class PrefManager {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
