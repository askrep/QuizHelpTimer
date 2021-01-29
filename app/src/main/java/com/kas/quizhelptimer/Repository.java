package com.kas.quizhelptimer;

import com.kas.quizhelptimer.data.LocalDataSource;
import com.kas.quizhelptimer.data.RemoteDataSource;

import javax.inject.Inject;


public class Repository {

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    @Inject
    public Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }
}
