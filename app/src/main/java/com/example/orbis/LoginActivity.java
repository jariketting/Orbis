package com.example.orbis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //Button to go from login screen to the home screen
    //Add a checker
    public void loginButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
//        Info = findViewById(R.id.attemptsTextView);
//        validate(Email.getText().toString(), Password.getText().toString());
        startActivity(intent);
    }

    //Button to go from login screen to the register screen
    public void registerButton(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //Button to go from login screen to the forgot password screen
    public void forgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
