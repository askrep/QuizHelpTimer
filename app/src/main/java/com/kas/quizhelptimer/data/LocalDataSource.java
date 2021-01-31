package com.kas.quizhelptimer.data;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalDataSource {
    
    private static final String TAG = "#_LocalDataStore";
    
    @Inject
    public LocalDataSource() {
        Log.d(TAG, "LocalDataSource: created");
    }
    
    /**
     * Calculating the average time per question
     *
     * @param numberQuestion int
     * @param timeSeconds    time seconds
     */
    public float calcAverageQuestionTimeInSeconds(int numberQuestion, int timeSeconds) {
        return numberQuestion <= 0 || timeSeconds <= 0 ? -1 : (float) timeSeconds / numberQuestion;
    }
}
