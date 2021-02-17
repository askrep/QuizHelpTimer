package com.kas.quizhelptimer.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

@Entity(tableName = "quiz")
public class Question {
    
    @PrimaryKey(autoGenerate = false)
    private int questionNumber;
    private String startedAt;
    private String answeredAt;
    
    public Question(int questionNumber, String startedAt, String answeredAt) {
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
    
    public String getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }
    
    public String getAnsweredAt() {
        return answeredAt;
    }
    
    public void setAnsweredAt(String answeredAt) {
        this.answeredAt = answeredAt;
    }
}
