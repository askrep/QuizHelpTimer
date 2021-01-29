package com.kas.quizhelptimer.ui.main;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kas.quizhelptimer.Repository;

import java.time.Duration;
import java.time.LocalTime;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    
    private static final String TAG = "#_MainViewModel";
    private Repository repository;
    
    private MutableLiveData<String> numberQuestionsLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> maxTimeLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> startTimeLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> answerTimeCounterLiveData = new MutableLiveData<>("");
    private MutableLiveData<Integer> leftQuestionsLiveData = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> quizStartedLiveData = new MutableLiveData<>(false);
    private LocalTime startLocalTime;
    
    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
    }
    
    public void onStartPressed() {
        Log.d(TAG, "onStartPressed: TRUE");
        quizStartedLiveData.setValue(true);
        
        startLocalTime = LocalTime.now();
        Log.d(TAG, "onStartPressed: startTime == " + this.startLocalTime.getHour());
        Log.d(TAG, "onStartPressed: startTime == " + this.startLocalTime.getMinute());
        Log.d(TAG, "onStartPressed: startTime == " + this.startLocalTime.getSecond());
        
    }
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onAnsweredPressed() {
        Log.d(TAG, "onAnsweredPressed: TRUE" + Duration.between(startLocalTime, LocalTime.now()).getSeconds());
        
    }
    
    public boolean getActionButtonState() {
        return quizStartedLiveData.getValue();
    }
    
    public LiveData<String> getNumberQuestionsLiveData() {
        return numberQuestionsLiveData;
    }
    
    public void setNumberQuestionsLiveData(String numberQuestions) {
        numberQuestionsLiveData.setValue(numberQuestions);
    }
    
    public LiveData<String> getMaxTimeLiveData() {
        return maxTimeLiveData;
    }
    
    public void setMaxTimeLiveData(String maxTime) {
        this.maxTimeLiveData.setValue(maxTime);
    }
    
    public LiveData<String> getStartTimeLiveData() {
        return startTimeLiveData;
    }
    
    public void setStartTimeLiveData(String startTime) {
        this.startTimeLiveData.setValue(startTime);
    }
    
    public LiveData<String> getAnswerTimeCounterLiveData() {
        return answerTimeCounterLiveData;
    }
    
    public void setAnswerTimeCounterLiveData(String answerTimeCounter) {
        this.answerTimeCounterLiveData.setValue(answerTimeCounter);
    }
    
    public LiveData<Integer> getLeftQuestionsLiveData() {
        return leftQuestionsLiveData;
    }
    
    public void setLeftQuestionsLiveData(Integer leftQuestions) {
        this.leftQuestionsLiveData.setValue(leftQuestions);
    }
    
    public LiveData<Boolean> getQuizStartedLiveData() {
        return quizStartedLiveData;
    }
    
    public void setQuizStartedLiveData(Boolean quizStarted) {
        this.quizStartedLiveData.setValue(quizStarted);
    }
}