package com.kas.quizhelptimer;

import android.os.SystemClock;
import android.util.Log;

import java.time.LocalTime;

public class CalcUtils {
    private static final String TAG = "#_TimeUtils";

    public static long getNowTimePointMs() {

        return SystemClock.elapsedRealtime();
    }

    public static long getAverageTimeMs(int number, int remainingMs) {
        return remainingMs / number;
    }

    public static long getAverageTimeSec(int number, long remainingSec) {
        return remainingSec / number;
    }

    public static long getNowTimeMs() {
        return SystemClock.elapsedRealtime();
    }

    public static long getNowTimeSec() {
        return SystemClock.elapsedRealtime() / 1000;
    }

    public static long getRemainingAverageTimeMs(int number, int remainingMs) {
        return remainingMs - (getNowTimePointMs() + getAverageTimeMs(number, remainingMs));
    }


}
