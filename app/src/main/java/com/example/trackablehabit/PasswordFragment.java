package com.example.trackablehabit;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PasswordFragment extends Fragment {
    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;
    private EditText change_old_pw, change_new_pw, change_confirm_new_pw;
    private Button change_confirm_pw;
    private int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_password, container, false);

        habitDBHelper = new HabitDBHelper(getActivity());
        habitDatabase = habitDBHelper.getReadableDatabase();
        change_old_pw = rootView.findViewById(R.id.change_old_pw);
        change_new_pw = rootView.findViewById(R.id.change_new_pw);
        change_confirm_new_pw = rootView.findViewById(R.id.change_confirm_new_pw);
        change_confirm_pw = rootView.findViewById(R.id.change_confirm_pw);

        change_confirm_pw.setOnClickListener(v -> {
            String username = habitDBHelper.getCurrentUsername();
            String old_password = change_old_pw.getText().toString().trim();
            String new_password = change_new_pw.getText().toString().trim();
            String confirm_password = change_confirm_new_pw.getText().toString().trim();

            int res = habitDBHelper.checkUser(username, old_password);

            if (res > 0) {
                userID = res;
                if (new_password.equals(confirm_password)) {
                    habitDBHelper.updateUser(String.valueOf(userID), username, new_password, 1);
                    Toast.makeText(getActivity(), R.string.sucessfully_updated, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.pw_does_not_match, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), R.string.wrong_pw, Toast.LENGTH_SHORT).show();
            }

        });

        return rootView;
    }
}
