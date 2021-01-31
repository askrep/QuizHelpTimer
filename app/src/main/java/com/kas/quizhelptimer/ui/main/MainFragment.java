package com.kas.quizhelptimer.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kas.quizhelptimer.R;
import com.kas.quizhelptimer.databinding.MainFragmentBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    
    private static final String TAG = "#_MainFragment";
    private MainFragmentBinding binding;
    public MainViewModel mainViewModel;
    Boolean isQuizStarted;
    private Button actionButton;
    private ImageButton buttonRemoveQuestions;
    private ImageButton buttonAddQuestions;
    private ImageButton buttonRemoveMaxTime;
    private ImageButton buttonAddMaxTime;
    
    @Inject
    public MainFragment() {
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        
        actionButton = binding.buttonMainAction;
        buttonRemoveQuestions = binding.buttonRemoveQuestions;
        buttonAddQuestions = binding.buttonAddQuestions;
        buttonRemoveMaxTime = binding.buttonRemoveMaxTime;
        buttonAddMaxTime = binding.buttonAddMaxTime;
        
        EditText mainQuestionsNumberValue = binding.mainQuestionsNumberValue;
        
        /** init if quiz was started */
        if (mainViewModel.getActionButtonState()) {
            actionButton.setText(R.string.main_action_button_answer);
            mainQuestionsNumberValue.setEnabled(false);
            binding.mainMaxTimeValue.setEnabled(false);
        }
        
        /** init fields and buttons*/
    /*    initQuestionsNumber(mainQuestionsNumberValue);
        initMaxTime();*/
        initAscendingDescendingButtons();
        initActionButton(actionButton);
        initTextFieldsObservers();
        initResetButtonAction();
        configWhenQuizStateChanged();
        
    }
    
    private void initAscendingDescendingButtons() {
        
        buttonRemoveQuestions.setOnClickListener(view -> {
            mainViewModel.decrementQuestionsNumber();
        });
        
        buttonAddQuestions.setOnClickListener(view -> {
            mainViewModel.incrementQuestionsNumber();
        });
        
        buttonRemoveMaxTime.setOnClickListener(view -> {
            mainViewModel.decrementMaxTime();
        });
        buttonAddMaxTime.setOnClickListener(view -> {
            mainViewModel.incrementMaxTime();
        });
        
    }
    
    private void initTextFieldsObservers() {
        mainViewModel.getQuestionsNumberLiveData().observe(getViewLifecycleOwner(), value -> {
            
            binding.mainQuestionsNumberValue.setText(value);
        });
        mainViewModel.getMaxTimeLiveData().observe(getViewLifecycleOwner(), value -> {
            
            binding.mainMaxTimeValue.setText(value);
        });
        /** TextView "Left Questions" ViewModel Observer*/
        mainViewModel.getLeftQuestionsLiveData().observe(getViewLifecycleOwner(), value -> {
            binding.mainLeftTimeValue.setText(value);
        });
        /** TextView "Time Left" ViewModel Observer*/
        mainViewModel.getLeftTimeToAnswerLiveData().observe(getViewLifecycleOwner(), value -> {
            binding.mainLeftTimeValue.setText(value);
        });
        /** TextView "Average Time" ViewModel Observer*/
        mainViewModel.getAverageTimeToAnswerLiveData().observe(getViewLifecycleOwner(), string -> {
            binding.mainAvgTimeValue.setText(string);
        });
    }
    
/*    private void initQuestionsNumber(EditText mainQuestionsNumberValue) {
        mainQuestionsNumberValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                String validString = mainViewModel.getValidStringFormat(charSequence);
       
                mainViewModel.onQuestionsNumberChanged(validString);
                
                Log.d(TAG, "onActivityCreated: mainQuestionsNumberValue == " + charSequence);
            }
            
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            public void afterTextChanged(Editable editable) {
            
            }
        });
    }
    
    private void initMaxTime() {
        binding.mainMaxTimeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
                mainViewModel.onMaxTimeChanged(mainViewModel.getValidStringFormat(charSequence));
                
                Log.d(TAG, "onActivityCreated: mainMaxTimeValue == " + charSequence);
            }
            
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            public void afterTextChanged(Editable editable) {
            
            }
        });
    }*/
    
    private void initActionButton(Button actionButton) {
        
        actionButton.setOnClickListener(btnView -> {
            if (isQuizStarted) {
                mainViewModel.onAnsweredPressed();
            } else {
                mainViewModel.onStartPressed();
            }
        });
    }
    
    /**
     * Quiz is started LiveData Observer
     */
    private void configWhenQuizStateChanged() {
        mainViewModel.getIsQuizStartedLiveData().observe(getViewLifecycleOwner(), state -> {
            isQuizStarted = state;
            Log.d(TAG, "configWhenQuizStateChanged: " + state);
            if (state) {
                actionButton.setText(R.string.main_action_button_answer);
                binding.mainQuestionsNumberValue.setEnabled(false);
                binding.mainMaxTimeValue.setEnabled(false);
            } else {
                actionButton.setText(R.string.main_action_button_start);
                binding.mainQuestionsNumberValue.setEnabled(true);
                binding.mainMaxTimeValue.setEnabled(true);
            }
            
        });
    }
    
    private void initResetButtonAction() {
        binding.mainButtonReset.setOnClickListener(view -> {
            mainViewModel.onResetClicked();
            Toast.makeText(getContext(), "Quiz was sReset", Toast.LENGTH_SHORT).show();
            binding.mainQuestionsNumberValue.setText("");
            binding.mainMaxTimeValue.setText("");
        });
    }
}