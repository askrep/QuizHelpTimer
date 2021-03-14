package com.kas.quizhelptimer.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.kas.quizhelptimer.db.Question;
import com.kas.quizhelptimer.db.QuizDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalDataSource {
    
    private QuizDao quizDao;
    private static final String TAG = "#_LocalDataStore";
    
    @Inject
    public LocalDataSource(QuizDao quizDao) {
        Log.d(TAG, "LocalDataSource: created");
        this.quizDao = quizDao;
    }
    
    /**
     * Calculating the average time per question
     *
     * @param numberQuestion int
     * @param timeSeconds    time seconds
     */
    public float calcAverageQuestionTimeInSeconds(int numberQuestion, int timeSeconds) {
        return numberQuestion <= 0 || timeSeconds <= 0 ? -1 : (float) timeSeconds / numberQuestion;
    }
    
    /**
     * Calculating the average time per question
     *
     * @param numberQuestion int
     * @param millis         long milliseconds
     */
    public long calcAverageQuestionTimeInMillis(int numberQuestion, long millis) {
        return numberQuestion <= 0 || millis <= 0 ? -1 : (long) millis / numberQuestion;
    }
    
    public LiveData<List<Question>> getAllQuestions() {
        return quizDao.getAllQuestions();
    }
    
    public LiveData<Question> getQuestionByNumber(int number) {
        return quizDao.getQuestionByNumber(number);
    }
    
    public void insertQuestion(Question question) {
        quizDao.insertQuestion(question);
    }
}
