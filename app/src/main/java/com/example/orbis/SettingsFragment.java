package com.example.orbis;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsFragment extends Fragment {
    MainActivity main; //store main activity
    View view;
    API api;
    Switch privateSwitch;
    TextView textSignout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        main = ((MainActivity) getActivity());
        api = new API(main);

        textSignout = view.findViewById(R.id. TextSignout);

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarSettings);
        toolbar.setTitle(R.string.toolbarSettings);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material); //set back arrow
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AccountFragment()).commit();
            }
        });


        privateSwitch = view.findViewById(R.id.Privateaccountslide);

//        //invite a friend button
//        TextView TextInvitefriends = (TextView) view.findViewById(R.id.TextInvitefriends);
//        TextInvitefriends.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.container, new Invite_a_friendFragment()).addToBackStack(null).commit();
//            }
//        });

//        //help button
//        TextView TextHelp = (TextView) view.findViewById(R.id.TextHelp);
//        TextHelp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.container, new HelpFragment()).addToBackStack(null).commit();
//            }
//        });

        //about button
        TextView TextAbout = (TextView) view.findViewById(R.id.TextAbout);
        TextAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AboutFragment()).addToBackStack(null).commit();
            }
        });

        //change password button
        TextView TextChangepassword = (TextView) view.findViewById(R.id.TextChangepassword);
        TextChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new Change_passwordFragment()).addToBackStack(null).commit();
            }
        });

        //create listener
        textSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "logout/";

                JSONObject jsonBody = new JSONObject();
                api.request(url, jsonBody, new APICallback() {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        try {
                            onLogoutResult(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

        privateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String url = "user/update/";

                JSONObject jsonBody = new JSONObject();
                try {
                    if(isChecked)
                        jsonBody.put("private", 1);
                    else
                        jsonBody.put("private", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                api.request(url, jsonBody, new APICallback() {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        try {
                            onUsernameResult(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        getUser();

        return view;

    }

    private void getUser(){
        String url = "user/get/";

        JSONObject jsonBody = new JSONObject();

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onUsernameResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void onUsernameResult(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(!error.getBoolean("error")) {
            if(data.getBoolean("private")) {
                privateSwitch.setChecked(true);
            }
        }
    }

    private void onLogoutResult(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(!error.getBoolean("error")) {
            api.setSession("");
            Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
