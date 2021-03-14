package com.kas.quizhelptimer;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kas.quizhelptimer.data.LocalDataSource;
import com.kas.quizhelptimer.data.RemoteDataSource;
import com.kas.quizhelptimer.db.Question;
import com.kas.quizhelptimer.db.QuizDao;

import java.util.List;

import javax.inject.Inject;

public class Repository {
    
    private static final String TAG = "#_Repository";
    
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    
    private MutableLiveData<Long> countDownTimerMsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> countDownTimerFinishedLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> countDownTimerAverageMsLiveData = new MutableLiveData<>();
    
    @Inject
    public Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        Log.d(TAG, "Repository: created");
    }
    
    /**
     * Return LiveData Questions List from database
     *
     * @return LiveData
     */
    public LiveData<List<Question>> getAllQuestions() {
        return localDataSource.getAllQuestions();
    }
    
    /**
     * Return LiveData Question with particular number from database
     *
     * @return LiveData
     */
    public LiveData<Question> getQuestionByNumber(int number) {
        return localDataSource.getQuestionByNumber(number);
    }
    
    /**
     * Insert Question with particular number from database
     *
     * @return LiveData
     */
    public void insertQuestion(Question question) {
        localDataSource.insertQuestion(question);
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
    
}
