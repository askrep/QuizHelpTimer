package com.kas.quizhelptimer.db;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class QuizDatabaseModule {
    
    @Singleton
    @Provides
    public static QuizDatabase provideQuizDatabase(@ApplicationContext Context application) {
        return Room.databaseBuilder(application, QuizDatabase.class, QuizDatabase.DATABASE_NAME)
                .build();
    }
    
    @Provides
    @Singleton
    public static QuizDao provideQuizDao(QuizDatabase database) {
        return database.quizDao();
    }
}
