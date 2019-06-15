package com.example.orbis;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;


public class Change_passwordFragment extends Fragment {
    MainActivity main; //store main activity
    View view;
    API api;

    Button buttonChange;
    private TextInputLayout Password;
    private TextInputLayout PasswordAgain;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +    //any letter
                    "(?=.*[!@#$%^&+=])" +   //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");                   //end of the string

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        main = ((MainActivity) getActivity());
        api = new API(main);

        buttonChange = view.findViewById(R.id.buttonChange);
        Password = view.findViewById(R.id.Password);
        PasswordAgain = view.findViewById(R.id.PasswordAgain);

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarChange);
        toolbar.setTitle(R.string.settings_changepassword);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material); //set back arrow
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToLastFragment();
            }
        });

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePassword() | !validateConfirmPassword()) {
                    return;
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }







    // validation for the password, with errors for being empty or not a strong enough password otherwise no error
// done with self made regular expression at the top
    private boolean validatePassword() {
        String passwordInput = Password.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            Password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            Password.setError("You need at least 4 characters, 1 upper and lower case letter, 1 digit and 1 special character "); // find a way for it to explain why
            return false;
        } else {
            Password.setError(null);
            return true;
        }
    }


    private boolean validateConfirmPassword() {
        String passwordInput = PasswordAgain.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            PasswordAgain.setError("Field can't be empty");
            return false;
        } else if (!PasswordAgain.getEditText().getText().toString().equals(PasswordAgain.getEditText().getText().toString())){
            PasswordAgain.setError("The password doesn't match");
            return false;
        } else {
            PasswordAgain.setError(null);
            return true;
        }
    }




    private void updatePassword(String newPassword){
        String url = "user/update/";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void onResult(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(!error.getBoolean("error")) {
            getFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).addToBackStack(null).commit();
        }
    }
}
