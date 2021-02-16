package com.kas.quizhelptimer.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

@Entity(tableName = "first_quiz")
public class Question {
    
    @PrimaryKey(autoGenerate = false)
    private int questionNumber;
    private LocalTime startedAt;
    private LocalTime answeredAt;
    
    public Question(int questionNumber, LocalTime startedAt, LocalTime answeredAt) {
        this.questionNumber = questionNumber;
        this.startedAt = startedAt;
        this.answeredAt = answeredAt;
    }
    
    public int getQuestionNumber() {
        return questionNumber;
    }
    
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
    
    public LocalTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalTime getAnsweredAt() {
        return answeredAt;
    }
    
    public void setAnsweredAt(LocalTime answeredAt) {
        this.answeredAt = answeredAt;
    }
}
