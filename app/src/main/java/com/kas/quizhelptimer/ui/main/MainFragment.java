package com.kas.quizhelptimer.ui.main;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kas.quizhelptimer.R;
import com.kas.quizhelptimer.databinding.MainFragmentBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    
    private static final String TAG = "#_MainFragment";
    private MainFragmentBinding binding;
    public MainViewModel mainViewModel;
    
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
        // TODO: Use the ViewModel
        Button actionButton = binding.buttonMainAction;
        EditText mainQuestionsNumberValue = binding.mainQuestionsNumberValue;
        
        if (mainViewModel.getActionButtonState()) {
            actionButton.setText(R.string.main_action_button_answer);
            mainQuestionsNumberValue.setEnabled(false);
            binding.mainMaxTimeValue.setEnabled(false);
        }
        
        mainQuestionsNumberValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.d(TAG, "onActivityCreated: mainQuestionsNumberValue == " + charSequence);
                mainViewModel.setNumberQuestionsLiveData(charSequence.toString());
            }
            
            @Override
            public void afterTextChanged(Editable editable) {
            
            }
        });
        initActionButton(actionButton);
        
    }
    
    private void initActionButton(Button actionButton) {
        actionButton.setOnClickListener(btnView -> {
            if (!mainViewModel.getActionButtonState()) {
                mainViewModel.onStartPressed();
                
                actionButton.setText(R.string.main_action_button_answer);
                binding.mainQuestionsNumberValue.setEnabled(false);
                binding.mainMaxTimeValue.setEnabled(false);
            } else {
                mainViewModel.onAnsweredPressed();
            }
        });
    }
    
}