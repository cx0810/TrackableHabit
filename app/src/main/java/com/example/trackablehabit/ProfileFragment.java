package com.example.trackablehabit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private TextView profileName, noOfHabitsTracked, noOfRewards, highestStreak;
    private HabitDBHelper habitDBHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        habitDBHelper = new HabitDBHelper(getActivity());

        profileName = rootView.findViewById(R.id.profile_name);
        noOfHabitsTracked = rootView.findViewById(R.id.number_of_habits_tracked);
        noOfRewards = rootView.findViewById(R.id.number_of_rewards);
        highestStreak = rootView.findViewById(R.id.highest_streak);


        profileName.setText(habitDBHelper.getCurrentUsername());

        String rewardsText = habitDBHelper.queryNoOfRewards() + "" ;
        noOfRewards.setText(rewardsText);

        String habitsText = habitDBHelper.queryNoOfHabits() + "";
        noOfHabitsTracked.setText(habitsText);

        String streakText = habitDBHelper.queryHighestStreak() + "" ;
        highestStreak.setText(streakText);


        return rootView;
    }
}
