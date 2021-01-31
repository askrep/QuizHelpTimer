package com.kas.quizhelptimer.util;

import android.util.Log;

public class DateTimeUtils {
    
    private static final String TAG = "#_DateTimeUtils";
    
    public static long covertMinutesToSeconds(int minutes) {
        long seconds = (long) minutes * 60;
        Log.d(TAG, "covertMinutesToSeconds: IN == " + minutes + ", OUT == " + seconds);
        return seconds;
    }

}
