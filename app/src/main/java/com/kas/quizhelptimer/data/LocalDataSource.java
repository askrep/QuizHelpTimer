package com.kas.quizhelptimer.data;

import android.util.Log;

import javax.inject.Inject;

public class LocalDataSource {
    
    private static final String TAG = "#_LocalDataStore";
    
    @Inject
    public LocalDataSource() {
        Log.d(TAG, "LocalDataSource: created");
    }
    
}
