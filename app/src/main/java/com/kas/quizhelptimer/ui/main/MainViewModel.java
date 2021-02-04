package com.kas.quizhelptimer.ui.main;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kas.quizhelptimer.Repository;
import com.kas.quizhelptimer.CalcUtils;

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
    private MutableLiveData<String> leftQuestionsLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> leftTimeToAnswerLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> averageTimeToAnswerLiveData = new MutableLiveData<>("0");

    /* SERVICE LIVE DATA*/
    private MutableLiveData<String> startTimeLiveData = new MutableLiveData<>("0");

    private MutableLiveData<Boolean> isQuizStartedLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isFinishedLiveData = new MutableLiveData<>(false);

    /*REPOSITORY LIVE DATA*/

    private MutableLiveData<Long> countDownTimerMsLiveData;
    private MutableLiveData<Long> countDownTimerAverageMsLiveData;
    private MutableLiveData<Boolean> timerFinishedLiveData;
    private CountDownTimer countDownTimer;

    private String quizFinishedDurationTime;
    private LocalTime testNow;
    private LocalTime testStartTime;
    private LocalTime testEndTime;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
 /*       countDownTimerMsLiveData = repository.getCountDownTimerMsLiveData();
        countDownTimerAverageMsLiveData = repository.countDownTimerAverageMsLiveData();
        timerFinishedLiveData = repository.getCountDownTimerFinishedLiveData();*/

    }


    private void startQuizDurationTimerMs() {
        String string = quizDurationLiveData.getValue();
        long parseLong = 60 * 1000 * (long) Float.parseFloat(string);
        // repository.startCountDownTimer(parseLong, (long) 1000);
        startCountDownTimer(parseLong, (long) 1000);
    }


    public void onStartPressed() {
        isQuizStartedLiveData.setValue(true);
        startLocalTime = LocalTime.now();
        startQuizDurationTimerMs();

        testNow = LocalTime.now();
        testStartTime = LocalTime.now();
        Log.d(TAG, "onStartPressed: start==" + testStartTime);
        long millis = Long.parseLong(quizDurationLiveData.getValue());
        testEndTime = LocalTime.now().plus(Duration.ofMillis(millis * 1000 * 60));
        Log.d(TAG, "onStartPressed: end==" + testEndTime);

    }

    public void onAnsweredPressed() {
        calculateLeftQuestionsNumber();


        testNow = LocalTime.now();
        String value = questionsNumberLiveData.getValue();
        averageTimeToAnswerLiveData.setValue(getAverageQuestionTimeString(value, quizDurationLiveData.getValue()));
        testNow.plus(Duration.ofMillis(Long.parseLong(value)));
    }

    private String getAverageQuestionTimeString(String num, String time) {
        return repository.getAverageQuestionTime(num, time);
    }

    public long getAnswerTimeAverageMs(int sec) {
        String numStr = questionsNumberLiveData.getValue();
        return CalcUtils.getRemainingAverageTimeMs(Integer.parseInt(numStr), sec);
    }

    /**
     * @param millisInFuture    long: The number of millis in the future from the call to start() until the countdown is done and onFinish() is called
     * @param countDownInterval long: The interval along the way to receive onTick(long) callbacks
     */
    public void startCountDownTimer(long millisInFuture, long countDownInterval) {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            /**
             * @param millisUntilFinished: The amount of time until finished*/
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimerMsLiveData.setValue(millisUntilFinished);

                //TODO Add QuizRemainingTimer and AnswerRemainingTimeAverage
                // T0 = now(); --N; tavg=Dur/num;
                // avg = D- (T0 + tavg)
            }

            /**
             * Callback fired when the time is up
             */
            @Override
            public void onFinish() {
                timerFinishedLiveData.setValue(true);
            }
        }.start();
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


    /* ###  +/- BUTTONS ### */
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


    /* ###  GETTERS  ### */
    public boolean getActionButtonState() {
        return isQuizStartedLiveData.getValue();
    }

    public LiveData<String> getQuestionsNumberLiveData() {
        return questionsNumberLiveData;
    }

    public LiveData<String> getQuizDurationLiveData() {
        return quizDurationLiveData;
    }

    public LiveData<String> getLeftTimeToAnswerLiveData() {
        return leftTimeToAnswerLiveData;
    }

    public LiveData<String> getLeftQuestionsLiveData() {
        return leftQuestionsLiveData;
    }

    public LiveData<Boolean> getIsQuizStartedLiveData() {
        return isQuizStartedLiveData;
    }

    public LiveData<String> getAverageTimeToAnswerLiveData() {
        return averageTimeToAnswerLiveData;
    }

    public LiveData<Boolean> getIsQuizFinishedLiveData() {
        return isFinishedLiveData;
    }

    public LiveData<Long> getCountDownTimerMsLiveData() {
        return countDownTimerMsLiveData;
    }

    public LiveData<Boolean> getTimerFinishedLiveData() {
        return timerFinishedLiveData;
    }

    public String getQuizFinishedDurationTime() {
        return quizFinishedDurationTime;
    }
}