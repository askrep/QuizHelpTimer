package com.kas.quizhelptimer;

import android.util.Log;

import com.kas.quizhelptimer.data.LocalDataSource;
import com.kas.quizhelptimer.data.RemoteDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Repository {
    
    private static final String TAG = "#_Repository";
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    
    @Inject
    public Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        Log.d(TAG, "Repository: created");
    }
}
