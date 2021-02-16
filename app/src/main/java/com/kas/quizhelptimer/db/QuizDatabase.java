package com.kas.quizhelptimer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Question.class}, version = 3, exportSchema = false)
public abstract class QuizDatabase extends RoomDatabase {
    private static final String LOG_TAG = "#_APP_DATABASE";
    public static final String DATABASE_NAME = "units_db";
    
    public abstract QuizDao quizDao();
}
