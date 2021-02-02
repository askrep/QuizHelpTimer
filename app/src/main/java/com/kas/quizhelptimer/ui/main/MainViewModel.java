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
    private MutableLiveData<String> questionsNumberLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> maxTimeLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> leftQuestionsLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> leftTimeToAnswerLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> averageTimeToAnswerLiveData = new MutableLiveData<>("");

    /* SERVICE LIVE DATA*/
    private MutableLiveData<String> startTimeLiveData = new MutableLiveData<>("0");

    private MutableLiveData<Boolean> isQuizStartedLiveData = new MutableLiveData<>(false);


    private MutableLiveData<Boolean> isFinishedLiveData = new MutableLiveData<>(false);
    private String quizFinishedDurationTime;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
    }

    private String getAverageQuestionTime() {
        return repository.getAverageQuestionTime(questionsNumberLiveData.getValue(), maxTimeLiveData.getValue());
    }


    public void onStartPressed() {
        isQuizStartedLiveData.setValue(true);
        startLocalTime = LocalTime.now();
    }

    public void onAnsweredPressed() {
        Log.d(TAG, "onAnsweredPressed: TRUE" + Duration.between(startLocalTime, LocalTime.now()).getSeconds());
        calculateLeftQuestionsNumber();
    }

    public String getValidStringFormat(CharSequence charSequence) {
        Log.d(TAG, "getValidStringFormat: charSequence " + charSequence);
        if (charSequence == null || charSequence.length() == 0) return "1";
        String string = charSequence.toString();

        if (string.startsWith("0") || string.startsWith("-")) {
            string = string.replaceFirst("[-,0]", "1");

        }
        int number = Integer.parseInt(string);
        if (number <= 0) return "1";
        if (number > 999) return "999";
        Log.d(TAG, "getValidStringFormat: " + string);
        return string;
    }

    public void stepDecrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number > 1) {
            questionsNumberLiveData.setValue(String.valueOf(number - 1));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
            calculateLeftQuestionsNumber();
        }
    }

    public void stepIncrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number >= 0 && number <= 999) {
            questionsNumberLiveData.setValue(String.valueOf(number + 1));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
            calculateLeftQuestionsNumber();
        }
    }

    public void stepDecrementMaxTime() {
        int timeMinutes = Integer.parseInt(maxTimeLiveData.getValue());

        if (timeMinutes > 1) {
            maxTimeLiveData.setValue(String.valueOf(timeMinutes - 1));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
        }
    }

    public void stepIncrementMaxTime() {
        int timeMinutes = Integer.parseInt(maxTimeLiveData.getValue());

        if (timeMinutes >= 0 && timeMinutes <= 999) {
            maxTimeLiveData.setValue(String.valueOf(timeMinutes + 1));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
        }
    }

    public boolean fastDecrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number > 1) {
            questionsNumberLiveData.setValue(String.valueOf(number - 5));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
            calculateLeftQuestionsNumber();
            return true;
        }
        return false;
    }

    public boolean fastIncrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number >= 0 && number <= 999) {
            questionsNumberLiveData.setValue(String.valueOf(number + 5));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
            calculateLeftQuestionsNumber();
            return true;
        }
        return false;
    }

    public boolean fastDecrementMaxTime() {
        int timeMinutes = Integer.parseInt(maxTimeLiveData.getValue());

        if (timeMinutes > 1) {
            maxTimeLiveData.setValue(String.valueOf(timeMinutes - 5));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
            return true;
        }
        return false;
    }

    public boolean fastIncrementMaxTime() {
        int timeMinutes = Integer.parseInt(maxTimeLiveData.getValue());

        if (timeMinutes >= 0 && timeMinutes <= 999) {
            maxTimeLiveData.setValue(String.valueOf(timeMinutes + 5));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
            return true;
        }
        return false;
    }

    public void calculateLeftQuestionsNumber() {
        if (!isQuizStartedLiveData.getValue()) {
            leftQuestionsLiveData.setValue(questionsNumberLiveData.getValue());
        } else {
            int num = Integer.parseInt(leftQuestionsLiveData.getValue());
            if (num > 1) {
                leftQuestionsLiveData.setValue(String.valueOf(num - 1));
            } else if (num == 1) {
                leftQuestionsLiveData.setValue("0");
                quizFinishedDurationTime = isQuizFinished();
            }
        }
    }

    private String isQuizFinished() {
        isFinishedLiveData.setValue(true);
        resetWhenFinished();
        LocalTime finishLocalTime = LocalTime.now();
        return getQuizTimeDuration(startLocalTime, finishLocalTime).toString();
    }

    private LocalTime getQuizTimeDuration(LocalTime start, LocalTime finish) {
        LocalTime duration = finish.minusHours(startLocalTime.getHour())
                .minusMinutes(startLocalTime.getMinute()).minusSeconds(startLocalTime.getSecond());
        Log.d(TAG, "getQuizTimeDuration: start==" + start + "; finish==" + finish + "; duration==" + duration);
        return duration;
    }

    private void resetWhenFinished() {
        isQuizStartedLiveData.setValue(false);
        questionsNumberLiveData.setValue("1");
        maxTimeLiveData.setValue("1");
        leftQuestionsLiveData.setValue("");
        leftTimeToAnswerLiveData.setValue("");
        averageTimeToAnswerLiveData.setValue("");
        Log.d(TAG, "onResetClicked: ");
    }

    public boolean onResetClicked() {
        isQuizStartedLiveData.setValue(false);
        questionsNumberLiveData.setValue("1");
        maxTimeLiveData.setValue("1");
        leftQuestionsLiveData.setValue("");
        leftTimeToAnswerLiveData.setValue("");
        averageTimeToAnswerLiveData.setValue("");
        Log.d(TAG, "onResetClicked: ");
        return true;
    }

    public boolean getActionButtonState() {
        return isQuizStartedLiveData.getValue();
    }

    public LiveData<String> getQuestionsNumberLiveData() {
        return questionsNumberLiveData;
    }

    public void setQuestionsNumberLiveData(String numberQuestions) {
        questionsNumberLiveData.setValue(numberQuestions);
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

    public void onQuestionsNumberChanged(String value) {
        questionsNumberLiveData.setValue(value);
        if (!questionsNumberLiveData.getValue().equals("") && !maxTimeLiveData.getValue().equals(""))
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
    }

    public void onMaxTimeChanged(String value) {
        maxTimeLiveData.setValue(value);
        if (!questionsNumberLiveData.getValue().equals("0") && !maxTimeLiveData.getValue().equals("0"))
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTime());
    }

    public String getQuizFinishedDurationTime() {
        return quizFinishedDurationTime;
    }

    public MutableLiveData<Boolean> getIsFinishedLiveData() {
        return isFinishedLiveData;
    }
}