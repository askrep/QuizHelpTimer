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
    private MutableLiveData<String> quizDurationLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> leftQuestionsLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> leftTimeToAnswerLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> averageTimeToAnswerLiveData = new MutableLiveData<>("");

    /* SERVICE LIVE DATA*/
    private MutableLiveData<String> startTimeLiveData = new MutableLiveData<>("0");

    private MutableLiveData<Boolean> isQuizStartedLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isFinishedLiveData = new MutableLiveData<>(false);

    /*REPOSITORY LIVE DATA*/

    private LiveData<Long> millisUntilFinishedLiveData;
    private LiveData<Boolean> timerFinishedLiveData;


    private String quizFinishedDurationTime;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
        millisUntilFinishedLiveData = repository.getCountDownTimerMsLiveData();
        timerFinishedLiveData = repository.getCountDownTimerFinishedLiveData();
    }


    private void startQuizDurationTimerMs() {
        String string = quizDurationLiveData.getValue();
        long parseLong = 60 * 1000 * (long) Float.parseFloat(string);
        repository.startCountDownTimer(parseLong, (long) 1000);
    }


    public void onStartPressed() {
        isQuizStartedLiveData.setValue(true);
        startLocalTime = LocalTime.now();
        startQuizDurationTimerMs();
    }

    public void onAnsweredPressed() {
        Log.d(TAG, "onAnsweredPressed: TRUE" + Duration.between(startLocalTime, LocalTime.now()).getSeconds());
        calculateLeftQuestionsNumber();
        Long remaining = millisUntilFinishedLiveData.getValue();
        String remainingStr = String.valueOf("" + remaining / 1000);
        String averageQuestionTime = getAverageQuestionTimeString(remainingStr, quizDurationLiveData.getValue());
        Log.d(TAG, "onAnsweredPressed: AvgTime==" + averageQuestionTime);
        averageTimeToAnswerLiveData.setValue(averageQuestionTime);
        startNextAnswerTimer(LocalTime.now());
    }

    private String getAverageQuestionTimeString(String num, String time) {
        return repository.getAverageQuestionTime(num, time);
    }

    private long getAverageQuestionTimeMs(int num, int minutes) {
        return repository.getAverageQuestionTimeMs(num, minutes * 60 * 1000);
    }

    private void startNextAnswerTimer(LocalTime now) {

    }

    public void calculateLeftQuestionsNumber() {
        if (!isQuizStartedLiveData.getValue()) {
            leftQuestionsLiveData.setValue(questionsNumberLiveData.getValue());
        } else {
            int num = Integer.parseInt(leftQuestionsLiveData.getValue());
            if (num > 1) {
                leftQuestionsLiveData.setValue(String.valueOf(num - 1));
                startQuizDurationTimerMs();
            } else if (num == 1) {
                leftQuestionsLiveData.setValue("0");
                quizFinishedDurationTime = isQuizFinished();
            }
        }
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
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(questionsNumberLiveData.getValue(), quizDurationLiveData.getValue()));
            calculateLeftQuestionsNumber();
        }
    }

    public void stepIncrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number >= 0 && number <= 999) {
            questionsNumberLiveData.setValue(String.valueOf(number + 1));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(questionsNumberLiveData.getValue(), quizDurationLiveData.getValue()));
            calculateLeftQuestionsNumber();
        }
    }

    public void stepDecrementMaxTime() {
        int timeMinutes = Integer.parseInt(quizDurationLiveData.getValue());

        if (timeMinutes > 1) {
            quizDurationLiveData.setValue(String.valueOf(timeMinutes - 1));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(questionsNumberLiveData.getValue(), quizDurationLiveData.getValue()));
        }
    }

    public void stepIncrementMaxTime() {
        int timeMinutes = Integer.parseInt(quizDurationLiveData.getValue());

        if (timeMinutes >= 0 && timeMinutes <= 999) {
            quizDurationLiveData.setValue(String.valueOf(timeMinutes + 1));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(questionsNumberLiveData.getValue(), quizDurationLiveData.getValue()));
        }
    }

    public boolean fastDecrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number > 1) {
            questionsNumberLiveData.setValue(String.valueOf(number - 5));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(questionsNumberLiveData.getValue(), quizDurationLiveData.getValue()));
            calculateLeftQuestionsNumber();
            return true;
        }
        return false;
    }

    public boolean fastIncrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number >= 0 && number <= 999) {
            questionsNumberLiveData.setValue(String.valueOf(number + 5));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(questionsNumberLiveData.getValue(), quizDurationLiveData.getValue()));
            calculateLeftQuestionsNumber();
            return true;
        }
        return false;
    }

    public boolean fastDecrementMaxTime() {
        int timeMinutes = Integer.parseInt(quizDurationLiveData.getValue());

        if (timeMinutes > 1) {
            quizDurationLiveData.setValue(String.valueOf(timeMinutes - 5));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(questionsNumberLiveData.getValue(), quizDurationLiveData.getValue()));
            return true;
        }
        return false;
    }

    public boolean fastIncrementMaxTime() {
        int timeMinutes = Integer.parseInt(quizDurationLiveData.getValue());

        if (timeMinutes >= 0 && timeMinutes <= 999) {
            quizDurationLiveData.setValue(String.valueOf(timeMinutes + 5));
            averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(questionsNumberLiveData.getValue(), quizDurationLiveData.getValue()));
            return true;
        }
        return false;
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
        quizDurationLiveData.setValue("1");
        leftQuestionsLiveData.setValue("");
        leftTimeToAnswerLiveData.setValue("");
        averageTimeToAnswerLiveData.setValue("");
        Log.d(TAG, "onResetClicked: ");
    }

    public boolean onResetClicked() {
        isQuizStartedLiveData.setValue(false);
        questionsNumberLiveData.setValue("1");
        quizDurationLiveData.setValue("1");
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

    public LiveData<String> getQuizDurationLiveData() {
        return quizDurationLiveData;
    }

    public void setQuizDurationLiveData(String maxTime) {
        this.quizDurationLiveData.setValue(maxTime);
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


    public String getQuizFinishedDurationTime() {
        return quizFinishedDurationTime;
    }

    public MutableLiveData<Boolean> getIsQuizFinishedLiveData() {
        return isFinishedLiveData;
    }

    public LiveData<Long> getMillisUntilFinishedLiveData() {
        return millisUntilFinishedLiveData;
    }

    public LiveData<Boolean> getTimerFinishedLiveData() {
        return timerFinishedLiveData;
    }
}