package com.example.orbis;

import android.content.Intent;
import android.provider.ContactsContract;
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
    private EditText email;
    private EditText password;
    private int counter = 3;
    private TextView info;
    private Button Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       email = findViewById(R.id.usernameEmailLogin);
       password = findViewById(R.id.passwordLogin);
       info = findViewById(R.id.attemptsText);
       Login = findViewById(R.id.loginButton);
//       toolbar title
//       View view = inflater.inflate(R.layout.activity_login, container, false);
//       Toolbar toolbar = view.findViewById(R.id.toolbar);
//       toolbar.setTitle(getContext().getResources().getString(R.string.login_screen_toolbar_title));
    }

    //Button to go from login screen to the home screen
    // program cleans up code by turning == into .equals()
    //&& means and
    public void loginButton(View view) {
        if(email.getText().toString().equals("admin@hr.nl")&& password.getText().toString().equals("1234")) {
//            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){}
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            //add the counter for login attempts
//            // add a indication for the user of how many tries they have left
            counter = counter - 1;
            info.setText("Attempts left: " + counter);
            if (counter == 0) {
                Login.setEnabled(false);
            }
        }
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
