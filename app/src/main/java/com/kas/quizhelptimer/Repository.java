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
    
    public String getAverageQuestionTime(String numberQuestion, String time) {
        int number = Integer.parseInt(numberQuestion);
        int seconds = Integer.parseInt(time) * 60;
        String result = String.valueOf(localDataSource.calcAverageQuestionTimeInSeconds(number, seconds));
        Log.d(TAG, "getAverageQuestionTimeSeconds: " + result);
        return result;
    }
    
}
