package com.kas.quizhelptimer.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
            binding.mainQuizDurationValue.setEnabled(false);
        }

        /** init fields and buttons*/
    /*    initQuestionsNumber(mainQuestionsNumberValue);
        initMaxTime();*/
        initAscendingDescendingButtons();
        initActionButton(actionButton);
        initTextFieldsObservers();
        initResetButtonAction();
        configWhenQuizStateChanged();

        initIsQuizFinishedObserver();


        mainViewModel.getTimerFinishedLiveData().observe(getViewLifecycleOwner(), isFinished -> {
            Toast.makeText(getContext(), "CountDown Finished", Toast.LENGTH_SHORT).show();
        });

    }

    private void initIsQuizFinishedObserver() {
        mainViewModel.getIsQuizFinishedLiveData().observe(getViewLifecycleOwner(), isFinished -> {
            Toast.makeText(getContext(), "Quiz Finished! Duration time: " + mainViewModel.getQuizFinishedDurationTime(), Toast.LENGTH_LONG).show();
        });
    }

    private void initAscendingDescendingButtons() {

        buttonRemoveQuestions.setOnClickListener(view -> {
            mainViewModel.stepDecrementQuestionsNumber();
        });

        buttonRemoveQuestions.setOnLongClickListener(view -> {
            return mainViewModel.fastDecrementQuestionsNumber();
        });
///////////
        buttonAddQuestions.setOnClickListener(view -> {
            mainViewModel.stepIncrementQuestionsNumber();
        });
        buttonAddQuestions.setOnLongClickListener(view -> {
            return mainViewModel.fastIncrementQuestionsNumber();
        });
///////////
        buttonRemoveMaxTime.setOnClickListener(view -> {
            mainViewModel.stepDecrementMaxTime();
        });
        buttonRemoveMaxTime.setOnLongClickListener(view -> {
            return mainViewModel.fastDecrementMaxTime();
        });
///////////
        buttonAddMaxTime.setOnClickListener(view -> {
            mainViewModel.stepIncrementMaxTime();
        });
        buttonAddMaxTime.setOnLongClickListener(view -> {
            return mainViewModel.fastIncrementMaxTime();
        });

    }

    private void initTextFieldsObservers() {
        mainViewModel.getQuestionsNumberLiveData().observe(getViewLifecycleOwner(), value -> {

            binding.mainQuestionsNumberValue.setText(value);
        });
        mainViewModel.getQuizDurationLiveData().observe(getViewLifecycleOwner(), value -> {

            binding.mainQuizDurationValue.setText(value);
        });
        /** TextView "Left Questions" ViewModel Observer*/
        mainViewModel.getLeftQuestionsLiveData().observe(getViewLifecycleOwner(), value -> {
            binding.mainQuestionsLeftValue.setText(value);
        });
        /** TextView "Answer Time Left" ViewModel Observer*/
        mainViewModel.getLeftTimeToAnswerLiveData().observe(getViewLifecycleOwner(), value -> {
            binding.mainLeftTimeToAnswerValue.setText(value);
        });
        /** TextView "Answer Average Time" ViewModel Observer*/
        mainViewModel.getAverageTimeToAnswerLiveData().observe(getViewLifecycleOwner(), string -> {
            binding.mainAvgTimeValue.setText(string);
        });

        mainViewModel.getCountDownTimerMsLiveData().observe(getViewLifecycleOwner(), mills -> {
            binding.mainDurationTimeValue.setText(String.valueOf(mainViewModel.getAnswerTimeAverageMs((int) (mills / 1000))));

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
                binding.mainQuizDurationValue.setEnabled(false);
            } else {
                actionButton.setText(R.string.main_action_button_start);
                binding.mainQuestionsNumberValue.setEnabled(true);
                binding.mainQuizDurationValue.setEnabled(true);
            }
        });
    }

    private void initResetButtonAction() {
        binding.mainButtonReset.setOnClickListener(view -> {
            mainViewModel.onResetClicked();
            Toast.makeText(getContext(), "Quiz was sReset", Toast.LENGTH_SHORT).show();
            binding.mainQuestionsNumberValue.setText("0");
            binding.mainQuizDurationValue.setText("0");
        });
    }
}