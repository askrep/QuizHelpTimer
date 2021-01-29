package com.kas.quizhelptimer.ui.main;

import androidx.lifecycle.ViewModel;

import com.kas.quizhelptimer.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private Repository repository;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
    }
}