package com.kas.quizhelptimer.db;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class QuizDatabaseModule {
    
    @Provides
    @Singleton
    public static QuizDatabase provideQuizDatabase(Application application) {
        return Room.databaseBuilder(application, QuizDatabase.class, QuizDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build(); //TODO FATAL EXCEPTION: main RuntimeException: ... QuizDatabase_Impl does not exist
    }
    
    @Provides
    @Singleton
    public static QuizDao provideQuizDao(@NotNull QuizDatabase database) {
        return database.quizDao();
    }
}
