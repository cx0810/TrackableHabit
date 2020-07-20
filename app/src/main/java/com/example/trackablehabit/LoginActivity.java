package com.example.trackablehabit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextUsername = findViewById(R.id.login_username);
        mTextPassword = findViewById(R.id.login_password);
        mButtonLogin = findViewById(R.id.login_button);
        mRegister = findViewById(R.id.login_register);

        mRegister.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(startIntent);
        });


    }
}
