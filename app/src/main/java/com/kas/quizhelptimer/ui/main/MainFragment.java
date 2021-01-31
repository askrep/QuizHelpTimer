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
        EditText mainQuestionsNumberValue = binding.mainQuestionsNumberValue;
        
        /** init if quiz was started */
        if (mainViewModel.getActionButtonState()) {
            actionButton.setText(R.string.main_action_button_answer);
            mainQuestionsNumberValue.setEnabled(false);
            binding.mainMaxTimeValue.setEnabled(false);
        }
        
        /** init fields and buttons*/
        initQuestionsNumber(mainQuestionsNumberValue);
        initMaxTime();
        initActionButton(actionButton);
        initTextFieldsObservers();
        initResetButtonAction();
        configWhenQuizStateChanged();
        
    }
    
    private void initTextFieldsObservers() {
 /*       mainViewModel.getNumberQuestionsLiveData().observe(getViewLifecycleOwner(), value -> {
            if (binding.mainQuestionsNumberValue.getText().equals(value)) return;
            binding.mainQuestionsNumberValue.setText(value);
        });
        mainViewModel.getMaxTimeLiveData().observe(getViewLifecycleOwner(), value -> {
            if (binding.mainMaxTimeValue.getText().equals(value)) return;
            binding.mainMaxTimeValue.setText(value);
        });*/
        mainViewModel.getLeftQuestionsLiveData().observe(getViewLifecycleOwner(), value -> {
            binding.mainLeftTimeValue.setText(value);
        });
        mainViewModel.getLeftTimeToAnswerLiveData().observe(getViewLifecycleOwner(), value -> {
            binding.mainLeftTimeValue.setText(value);
        });
        mainViewModel.getAverageTimeToAnswerLiveData().observe(getViewLifecycleOwner(), string -> {
            binding.mainAvgTimeValue.setText(string);
        });
    }
    
    private void initMaxTime() {
        binding.mainMaxTimeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onActivityCreated: mainMaxTimeValue == " + charSequence);
                if (charSequence.toString().equals("") || charSequence.toString().equals("0")) {
                    return;
                } else {
                    mainViewModel.onMaxTimeChanged(charSequence.toString());
                }
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
    }
    
    private void initQuestionsNumber(EditText mainQuestionsNumberValue) {
        mainQuestionsNumberValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                
                if (charSequence.toString().equals("") || charSequence.toString().equals("0")) {
                    return;
                } else {
                    mainViewModel.onQuestionsNumberChanged(charSequence.toString());
                }
                Log.d(TAG, "onActivityCreated: mainQuestionsNumberValue == " + charSequence);
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
    }
    
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