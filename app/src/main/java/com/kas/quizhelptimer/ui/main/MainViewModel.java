package com.kas.quizhelptimer.ui.main;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kas.quizhelptimer.Repository;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;



//TODO need to refactoring the view model and repository
@HiltViewModel
public class MainViewModel extends ViewModel {

    private static final String TAG = "#_MainViewModel";
    private Repository repository;

    /* VIEW FIELDS LIVE DATA*/
    private MutableLiveData<String> questionsNumberLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> quizDurationMinutesLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> leftQuestionsLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> leftTimeToAnswerLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> averageTimeToAnswerSecondsLiveData = new MutableLiveData<>("0");
    private MutableLiveData<String> quizRemainingTimeLiveData = new MutableLiveData<>("0");

    /* SERVICE LIVE DATA*/
    private MutableLiveData<LocalTime> startTimeLiveData = new MutableLiveData<>();
    private MutableLiveData<LocalTime> finishTimeLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isQuizStartedLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isFinishedLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> timerFinishedLiveData = new MutableLiveData<>(false);
    ;

    private MutableLiveData<Long> countDownTimerMsLiveData = new MutableLiveData<>(0L);

    private LocalTime startLocalTime;

    private CountDownTimer countDownTimer;

    private String quizFinishedDurationTime;
    private Long testNow = 0L;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
    }

    public void onStartPressed() {
        if ((Integer.parseInt(questionsNumberLiveData.getValue()) >= 1) &&
                (Integer.parseInt(quizDurationMinutesLiveData.getValue()) >= 1)) {

            isQuizStartedLiveData.setValue(true);
            startTimeLiveData.setValue(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
            finishTimeLiveData.setValue(startTimeLiveData.getValue().plus(
                    Long.parseLong(quizDurationMinutesLiveData.getValue()), ChronoUnit.MINUTES));

            String string = quizDurationMinutesLiveData.getValue();
            long parseLong = 60 * 1000 * (long) Float.parseFloat(string);
            startCountDownTimer(parseLong, 1000);

            testNow = Long.parseLong(quizDurationMinutesLiveData.getValue().trim()) * 60 * 1000;
        }
    }

    public void onAnsweredPressed() {
        calculateLeftQuestionsNumber();

        String value = leftQuestionsLiveData.getValue();
        if (!value.equals("") && !value.equals("0")) {
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (countDownTimerMsLiveData.getValue() / Integer.parseInt(value)) / 1000));
            testNow = countDownTimerMsLiveData.getValue();
        } else {
            isQuizFinished();
        }

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

                long ms = millisUntilFinished - (testNow -
                        Long.parseLong(averageTimeToAnswerSecondsLiveData.getValue().trim()) *
                                1000);

                leftTimeToAnswerLiveData.setValue(String.valueOf(ms / 1000));
            }

            /**
             * Callback fired when the time is up
             */
            @Override
            public void onFinish() {
                isQuizFinished();

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
                //startQuizDurationTimerMs();
            } else if (num == 1) {
                leftQuestionsLiveData.setValue("0");
            }
        }
    }

    private void isQuizFinished() {

        LocalTime finishLocalTime = LocalTime.now();
        quizFinishedDurationTime = getQuizTimeDuration(startTimeLiveData.getValue(),
                finishLocalTime).toString();
        resetWhenFinished();
        isFinishedLiveData.setValue(true);
    }

    private LocalTime getQuizTimeDuration(LocalTime start, LocalTime finish) {
        LocalTime duration = LocalTime.ofSecondOfDay(
                finish.toSecondOfDay() - start.toSecondOfDay());
        return duration;
    }

    private void resetWhenFinished() {
        isQuizStartedLiveData.setValue(false);
        questionsNumberLiveData.setValue("1");
        quizDurationMinutesLiveData.setValue("1");
        leftQuestionsLiveData.setValue("0");
        leftTimeToAnswerLiveData.setValue("0");
        averageTimeToAnswerSecondsLiveData.setValue("0");
        countDownTimer.cancel();
        isFinishedLiveData.setValue(false);
        Log.d(TAG, "onResetClicked: ");

    }

    public boolean onResetClicked() {
        isQuizStartedLiveData.setValue(false);
        isFinishedLiveData.setValue(false);
        questionsNumberLiveData.setValue("1");
        quizDurationMinutesLiveData.setValue("1");
        leftQuestionsLiveData.setValue("");
        leftTimeToAnswerLiveData.setValue("");
        averageTimeToAnswerSecondsLiveData.setValue("");
        Log.d(TAG, "onResetClicked: ");
        countDownTimer.cancel();
        return true;
    }

    /* ###  +/- BUTTONS ### */
    public void stepDecrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number > 1) {
            questionsNumberLiveData.setValue(String.valueOf(number - 1));
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (Integer.parseInt(quizDurationMinutesLiveData.getValue()) * 60 /
                            Integer.parseInt(questionsNumberLiveData.getValue()))));
            calculateLeftQuestionsNumber();
        }
    }

    public void stepIncrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number >= 0 && number <= 999) {
            questionsNumberLiveData.setValue(String.valueOf(number + 1));
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (Integer.parseInt(quizDurationMinutesLiveData.getValue()) * 60 /
                            Integer.parseInt(questionsNumberLiveData.getValue()))));
            calculateLeftQuestionsNumber();
        }
    }

    public void stepDecrementMaxTime() {
        int timeMinutes = Integer.parseInt(quizDurationMinutesLiveData.getValue());

        if (timeMinutes > 1) {
            quizDurationMinutesLiveData.setValue(String.valueOf(timeMinutes - 1));
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (Integer.parseInt(quizDurationMinutesLiveData.getValue()) * 60 /
                            Integer.parseInt(questionsNumberLiveData.getValue()))));
        }
    }

    public void stepIncrementMaxTime() {
        int timeMinutes = Integer.parseInt(quizDurationMinutesLiveData.getValue());

        if (timeMinutes >= 0 && timeMinutes <= 999) {
            quizDurationMinutesLiveData.setValue(String.valueOf(timeMinutes + 1));
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (Integer.parseInt(quizDurationMinutesLiveData.getValue()) * 60 /
                            Integer.parseInt(questionsNumberLiveData.getValue()))));
        }
    }

    public boolean fastDecrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number > 1) {
            questionsNumberLiveData.setValue(String.valueOf(number - 10));
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (Integer.parseInt(quizDurationMinutesLiveData.getValue()) * 60 /
                            Integer.parseInt(questionsNumberLiveData.getValue()))));
            calculateLeftQuestionsNumber();
            return true;
        }
        return false;
    }

    public boolean fastIncrementQuestionsNumber() {
        int number = Integer.parseInt(questionsNumberLiveData.getValue());
        if (number >= 0 && number <= 999) {
            questionsNumberLiveData.setValue(String.valueOf(number + 10));
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (Integer.parseInt(quizDurationMinutesLiveData.getValue()) * 60 /
                            Integer.parseInt(questionsNumberLiveData.getValue()))));
            calculateLeftQuestionsNumber();
            return true;
        }
        return false;
    }

    public boolean fastDecrementMaxTime() {
        int timeMinutes = Integer.parseInt(quizDurationMinutesLiveData.getValue());

        if (timeMinutes > 1) {
            quizDurationMinutesLiveData.setValue(String.valueOf(timeMinutes - 10));
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (Integer.parseInt(quizDurationMinutesLiveData.getValue()) * 60 /
                            Integer.parseInt(questionsNumberLiveData.getValue()))));
            return true;
        }
        return false;
    }

    public boolean fastIncrementMaxTime() {
        int timeMinutes = Integer.parseInt(quizDurationMinutesLiveData.getValue());

        if (timeMinutes >= 0 && timeMinutes <= 999) {
            quizDurationMinutesLiveData.setValue(String.valueOf(timeMinutes + 10));
            averageTimeToAnswerSecondsLiveData.setValue(String.valueOf(
                    (Integer.parseInt(quizDurationMinutesLiveData.getValue()) * 60 /
                            Integer.parseInt(questionsNumberLiveData.getValue()))));
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

    public LiveData<String> getQuizDurationMinutesLiveData() {
        return quizDurationMinutesLiveData;
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

    public LiveData<String> getAverageTimeToAnswerSecondsLiveData() {
        return averageTimeToAnswerSecondsLiveData;
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

    public LiveData<LocalTime> getStartTimeLiveData() {
        return startTimeLiveData;
    }
}