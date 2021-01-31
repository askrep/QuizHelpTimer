package com.kas.quizhelptimer.ui.main;

import android.util.Log;

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
    
    private LocalTime startLocalTime;
    
    /* VIEW FIELDS LIVE DATA*/
    private MutableLiveData<String> numberQuestionsLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> maxTimeLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> leftQuestionsLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> leftTimeToAnswerLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> averageTimeToAnswerLiveData = new MutableLiveData<>("");
    
    /* SERVICE LIVE DATA*/
    private MutableLiveData<String> startTimeLiveData = new MutableLiveData<>("0");
    
    private MutableLiveData<Boolean> isQuizStartedLiveData = new MutableLiveData<>(false);
    
    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
    }
    
    private String getAverageQuestionTime() {
        return repository.getAverageQuestionTime(numberQuestionsLiveData.getValue(), maxTimeLiveData.getValue());
    }
    
    public void onQuestionsNumberChanged(String value) {
        numberQuestionsLiveData.setValue(value);
        if (!numberQuestionsLiveData.getValue().equals("") && !maxTimeLiveData.getValue().equals(""))
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
    }
    
    public void onMaxTimeChanged(String value) {
        maxTimeLiveData.setValue(value);
        if (!numberQuestionsLiveData.getValue().equals("0") && !maxTimeLiveData.getValue().equals("0"))
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
    }
    
    public void onStartPressed() {
        isQuizStartedLiveData.setValue(true);
        startLocalTime = LocalTime.now();
    }
    
    public void onAnsweredPressed() {
        Log.d(TAG, "onAnsweredPressed: TRUE" + Duration.between(startLocalTime, LocalTime.now()).getSeconds());
        
    }
    
    public boolean getActionButtonState() {
        return isQuizStartedLiveData.getValue();
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
    
    public LiveData<String> getLeftTimeToAnswerLiveData() {
        return leftTimeToAnswerLiveData;
    }
    
    public void setLeftTimeToAnswerLiveData(String answerTimeCounter) {
        this.leftTimeToAnswerLiveData.setValue(answerTimeCounter);
    }
    
    public LiveData<String> getLeftQuestionsLiveData() {
        return leftQuestionsLiveData;
    }
    
    public void setLeftQuestionsLiveData(String leftQuestions) {
        this.leftQuestionsLiveData.setValue(leftQuestions);
    }
    
    public LiveData<Boolean> getIsQuizStartedLiveData() {
        return isQuizStartedLiveData;
    }
    
    public void setIsQuizStartedLiveData(Boolean quizStarted) {
        this.isQuizStartedLiveData.setValue(quizStarted);
    }
    
    public LiveData<String> getAverageTimeToAnswerLiveData() {
        return averageTimeToAnswerLiveData;
    }
    
    public boolean onResetClicked() {
        isQuizStartedLiveData.setValue(false);
        numberQuestionsLiveData.setValue("");
        maxTimeLiveData.setValue("");
        leftQuestionsLiveData.setValue("");
        leftTimeToAnswerLiveData.setValue("");
        averageTimeToAnswerLiveData.setValue("");
        Log.d(TAG, "onResetClicked: ");
        
        return true;
    }
}