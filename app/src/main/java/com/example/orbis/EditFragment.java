package com.example.orbis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;



public class EditFragment extends Fragment {
    View view;
    MainActivity main; //stores our main activity
    API api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        main = ((MainActivity) getActivity());
        api = new API(main);
        getUser();

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

            TextView username = view.findViewById(R.id.usernameText);
            username.setText(data.getString("username"));

            TextView name = view.findViewById(R.id.fullnameText);
            name.setText(data.getString("name"));

            TextView bio = view.findViewById(R.id.bioText);
            bio.setText(data.getString("bio"));

            JSONObject image = data.getJSONObject("image");

            ImageView profilepic = view.findViewById(R.id.profilepicView);

            Picasso.get()
                    .load(image.getString("uri"))
                    .into(profilepic);

        }

    }
}
