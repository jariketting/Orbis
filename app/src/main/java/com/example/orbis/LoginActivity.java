package com.example.orbis;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private TextInputLayout email;
    private TextInputLayout password;

    API api;

    //    private int counter = 3;
//    private Button Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //toolbar title
        Toolbar toolbar = findViewById(R.id.toolbarLogin);
        toolbar.setTitle(R.string.login_screen_toolbar_title);

        api = new API(this);

        checkSession();

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
    }

    // code below checks if somebody has already logged in before
    // if so it goes straight to our home otherwise the person needs to login
    private void checkSession() {
        if(api.getSession() != null) {
            String url = "validate_session/";

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("session_id", api.getSession());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            api.request(url, jsonBody, new APICallback() {
                @Override
                public void onSuccessResponse(JSONObject response) {
                    try {
                        onSessionResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void onSessionResponse(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(!error.getBoolean("error")) {
            Boolean valid = data.getBoolean("valid");

            if(valid) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void login() {
        String url = "login/";

        EditText editTextEmail = email.getEditText();
        EditText editTextPassword = password.getEditText();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", editTextEmail.getText().toString());
            jsonBody.put("password", editTextPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onLoginResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onLoginResponse(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(error.getBoolean("error")) {
            Toast.makeText(this, "Wrong password or email.", Toast.LENGTH_SHORT).show();
        } else {
            api.setSession(data.getString("session_id"));

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
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

    // Validation
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

        login();
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

    @Override
    public void onBackPressed(){
    }
}