package com.kas.quizhelptimer.data;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RemoteDataSource {
    
    private static final String TAG = "#_RemoteDataSource";
    
    @Inject
    public RemoteDataSource() {
        Log.d(TAG, "RemoteDataSource: created");
    }
}
