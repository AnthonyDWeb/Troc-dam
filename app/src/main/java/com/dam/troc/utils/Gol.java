package com.dam.troc.utils;

import android.util.Log;

public class Gol {
    private static final String TAG = "Cycle de vie";
    public static void addLog(String emplacement, String message){
        Log.i(TAG, emplacement + " " + message);
    }
}
