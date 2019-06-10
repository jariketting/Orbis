package com.example.orbis;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout email;
    private TextInputLayout password;

    //    private int counter = 3;
//    private Button Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //toolbar title
        Toolbar toolbar = findViewById(R.id.toolbarLogin);
        toolbar.setTitle(R.string.login_screen_toolbar_title);

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
//       Login = findViewById(R.id.loginButton);
    }

    // validation for email to make sure it contains @ and . and error messages when field empty or no @ and/or .
    // done with regular expression that already existed
    private boolean validateEmail() {
        String emailInput = email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Email not valid");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
//        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            password.setError("Enter valid password"); // find a way for it to explain why
//            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    // button for people to register
    public void LoginButton(View v) {
        if (!validateEmail()| !validatePassword()) {
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Button to go from login screen to the home screen
    // program cleans up code by turning == into .equals()
    //&& means and
//    public void loginButton(View view) {
//        if (email.getText().toString().equals("1") && password.getText().toString().equals("1")) {
//            //if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
//    }
//        }else{
//            //add the counter for login attempts
//            // add a indication for the user of how many tries they have left
////            counter = counter - 1;
////            info.setText("Attempts left: " + counter);
////            if (counter == 0) {
////                Login.setEnabled(false);
//           }
//        }


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
