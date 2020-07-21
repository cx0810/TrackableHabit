package com.example.trackablehabit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    HabitDBHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextConfirmPassword;
    Button mButtonRegister;
    TextView mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new HabitDBHelper(this);

        mTextUsername = findViewById(R.id.register_username);
        mTextPassword = findViewById(R.id.register_password);
        mTextConfirmPassword = findViewById(R.id.register_confirm_password);
        mButtonRegister = findViewById(R.id.register_button);
        mLogin = findViewById(R.id.register_login);

        mLogin.setOnClickListener(v -> {
            Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(startIntent);
        });

        mButtonRegister.setOnClickListener(v -> {
            String user = mTextUsername.getText().toString().trim();
            String password = mTextPassword.getText().toString().trim();
            String confirmPassword = mTextConfirmPassword.getText().toString().trim();

            if (password.equals(confirmPassword)) {
                long val = db.addUser(user, password);
                if (val > 0) {
                    Toast.makeText(RegisterActivity.this, "You have registered successfully", Toast.LENGTH_SHORT).show();
                    Intent movetoLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(movetoLogin);
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
