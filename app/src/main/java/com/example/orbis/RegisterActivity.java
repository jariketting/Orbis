package com.example.orbis;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // regular expression for password validation
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[!@#$%^&+=])" +   //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");                   //end of the string
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputName;
    private TextInputLayout textConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //toolbar title
        Toolbar toolbar = findViewById(R.id.toolbarRegister);
        toolbar.setTitle(R.string.register_screen_toolbar_title);

        textInputName = findViewById(R.id.nameRegister);
        textInputEmail = findViewById(R.id.emailRegister);
        textInputUsername = findViewById(R.id.usernameRegister);
        textInputPassword = findViewById(R.id.passwordRegister);
        textConfirmPassword = findViewById(R.id.confirmPasswordRegister);

    }
// validation for the name that the field is not empty
    private boolean validateName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else {
            textInputName.setError(null);
            return true;
        }
    }
// validation for email to make sure it contains @ and . and error messages when field empty or no @ and/or .
// done with regular expression that already existed
    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Email not valid");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }
// validation for username that the field can't be empty
    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }
// validation for the password, with errors for being empty or not a strong enough password otherwise no error
// done with self made regular expression at the top
    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("You need at least 4 characters, 1 upper and lower case letter, 1 digit and 1 special character "); // find a way for it to explain why
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        String passwordInput = textConfirmPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textConfirmPassword.setError("Field can't be empty");
            return false;
        } else if (!textConfirmPassword.getEditText().toString().equals(textInputPassword.getEditText().toString())){
        //(!textConfirmPassword .equals(textInputPassword)){
            textConfirmPassword.setError("The password doesn't match"); // find a way for it to explain why
            return false;
        } else {
            textConfirmPassword.setError(null);
            return true;
        }
    }

// button for people to register
    public void Register_Button(View v) {
        if (!validateName() | !validateEmail() | !validateUsername() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

// Button back to the login screen
    public void Already_Registered_Button(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}