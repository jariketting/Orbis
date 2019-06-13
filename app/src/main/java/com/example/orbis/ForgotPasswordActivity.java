package com.example.orbis;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    API api;
    private TextInputLayout email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.emailForgotPw);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbarForgotPW);
        toolbar.setTitle(R.string.forgot_pw_screen_toolbar_title);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
    }
    private void newPassword() {
        String url = "reset_password/";

        EditText editTextEmail = email.getEditText();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", editTextEmail.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onSendResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onSendResponse(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(error.getBoolean("error")) {
            Toast.makeText(this, "This email is not connected to Orbis", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity
        }

        return super.onOptionsItemSelected(item);
    }
    public void sendPassword(View v) {
        if (!validateEmail()) {
            return;
        }
        newPassword();
    }
}
