package com.example.trackablehabit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText mTextUsername, mTextPassword;
    Button mButtonLogin;
    TextView mRegister;
    HabitDBHelper habitDBHelper;

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        habitDBHelper = new HabitDBHelper(this);

        mTextUsername = findViewById(R.id.login_username);
        mTextPassword = findViewById(R.id.login_password);
        mButtonLogin = findViewById(R.id.login_button);
        mRegister = findViewById(R.id.login_register);

        mRegister.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(startIntent);
        });

        mButtonLogin.setOnClickListener(v -> {
            String user = mTextUsername.getText().toString().trim();
            String password = mTextPassword.getText().toString().trim();
            int res = habitDBHelper.checkUser(user, password);
            if (res > 0) {
                userID = res;

                // set loggedIn to true/1
                habitDBHelper.updateUser(String.valueOf(userID), user, password, 1);

                Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                Intent homepage = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(homepage);
            } else {
                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
