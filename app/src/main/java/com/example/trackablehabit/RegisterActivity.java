package com.example.trackablehabit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextConfirmPassword;
    Button mButtonRegister;
    TextView mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mTextUsername = findViewById(R.id.register_username);
        mTextPassword = findViewById(R.id.register_password);
        mTextConfirmPassword = findViewById(R.id.register_confirm_password);
        mButtonRegister = findViewById(R.id.register_button);
        mLogin = findViewById(R.id.register_login);

        mLogin.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(startIntent);
        });
    }
}
