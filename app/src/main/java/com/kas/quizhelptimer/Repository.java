package com.kas.quizhelptimer;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kas.quizhelptimer.data.LocalDataSource;
import com.kas.quizhelptimer.data.RemoteDataSource;

import java.net.HttpCookie;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Repository {

    private static final String TAG = "#_Repository";
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    private MutableLiveData<Long> countDownTimerMsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> countDownTimerFinishedLiveData = new MutableLiveData<>();
    private CountDownTimer countDownTimer;
    private MutableLiveData<Long> countDownTimerAverageMsLiveData = new MutableLiveData<>();

    @Inject
    public Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        Log.d(TAG, "Repository: created");
    }

    public String getAverageQuestionTime(String numberQuestion, String time) {
        int number = Integer.parseInt(numberQuestion);
        int seconds = Integer.parseInt(time) * 60;
        String result = String.format("%10.1f",
                localDataSource.calcAverageQuestionTimeInSeconds(number, seconds));
        return result;
    }

    public String getAverageQuestionTimeMillis(String numberQuestion, long millis) {
        int number = Integer.parseInt(numberQuestion);
        String result = String.format("%10.1f",
                localDataSource.calcAverageQuestionTimeInMillis(number, millis));
        return result;
    }

    /**
     * Return -1 if  number <= 0 or @param maxDuration <= 0
     *
     * @param number      int
     * @param maxDuration long milliseconds
     * @return average milliseconds, -1 if @number or @maxDuration <=0
     */
    public long getAverageQuestionTimeMs(int number, long maxDuration) {
        if (number > 0 && maxDuration > 0) {
            return (long) maxDuration / number;
        } else return -1;
    }
    /*
     *//**
     * @param millisInFuture    long: The number of millis in the future from the call to start() until the countdown is done and onFinish() is called
     * @param countDownInterval long: The interval along the way to receive onTick(long) callbacks
     *//*
    public void startCountDownTimer(long millisInFuture, long countDownInterval) {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            *//**
     * @param millisUntilFinished: The amount of time until finished*//*
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimerMsLiveData.setValue(millisUntilFinished);
            }

            */

    /**
     * Callback fired when the time is up
     *//*
            @Override
            public void onFinish() {
                countDownTimerFinishedLiveData.setValue(true);
            }
        }.start();
    }*/
    public LiveData<Long> getCountDownTimerMsLiveData() {
        return countDownTimerMsLiveData;
    }

    public LiveData<Boolean> getCountDownTimerFinishedLiveData() {
        return countDownTimerFinishedLiveData;
    }

    public LiveData<Long> countDownTimerAverageMsLiveData() {
        return countDownTimerAverageMsLiveData;
    }
}
