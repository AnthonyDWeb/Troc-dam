package com.dam.troc.utils;

import android.util.Log;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;

public class Gol {
    private static final String TAG = "Cycle de vie";

    public static void addLog(String emplacement, String message){
        Log.i(TAG, emplacement + " " + message);
    }

    public static void showSnackbar(View baseView, String message) {
        Snackbar.make(baseView,message, Snackbar.LENGTH_LONG).show();
    }
}
