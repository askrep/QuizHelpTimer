package com.kas.quizhelptimer.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizDao {
    
    @Insert
    void insertQuestion(Question question);
    
    /** Question number 0 is Start quiz*/
    @Query("Select * From first_quiz where questionNumber = :number")
    LiveData<Question> getQuestionByNumber(int number);
    
    @Query("Select * From first_quiz")
    LiveData<List<Question>> getAllQuestions();
    
}
