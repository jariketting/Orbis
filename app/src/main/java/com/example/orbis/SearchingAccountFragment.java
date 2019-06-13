package com.example.orbis;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchingAccountFragment extends Fragment {
    View view;
    MainActivity main;
    int id;
    API api;

    public ArrayList<DiaryItems> exampleList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searching_account, container, false);
        id = getArguments().getInt("id");
        main = (MainActivity)getActivity();
        api = new API(main);
        getSearchUser();
        getLatestMemSearch();

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarSearchAccount);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material); //set back arrow
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new SearchFragment()).commit();
            }
        });

        return view;
    }

    private void getSearchUser(){
        String url = "user/get/"+id;

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

            TextView username = view.findViewById(R.id.usernameSearch);
            username.setText(data.getString("username"));

            TextView name = view.findViewById(R.id.fullnameSearch);
            name.setText(data.getString("name"));

            TextView bio = view.findViewById(R.id.bioSearch);
            bio.setText(data.getString("bio"));

            JSONObject image = data.getJSONObject("image");

            ImageView profilepic = view.findViewById(R.id.profilepicSearch);

            Picasso.get()
                    .load(image.getString("uri"))
                    .into(profilepic);

        }

    }

    public void getLatestMemSearch() {
        String url = "dairy/"+id;

        JSONObject jsonBody = new JSONObject();

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onSearchAccountResponse(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onSearchAccountResponse(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");


        if(!error.getBoolean("error")) {

            TextView title = view.findViewById(R.id.titleSearch);
            title.setText(data.getString("title"));

            TextView description = view.findViewById(R.id.descriptionSearch);
            description.setText(data.getString("description"));

            TextView date = view.findViewById(R.id.dateSearch);
            date.setText(data.getString("date"));

            JSONObject image = data.getJSONObject("images");

            ImageView imageViewSearch = view.findViewById(R.id.imageViewSearch);

            Picasso.get()
                    .load(image.getString("uri"))
                    .into(imageViewSearch);

//            exampleList = new ArrayList<>();
//
//            JSONArray key = data.names();
//            if(key != null) {
//                for (int i = 0; i < key.length (); ++i) {
//                    String keys = key.getString(i);
//                    JSONObject memory = data.getJSONObject(keys);
//
//                    JSONObject image = new JSONObject();
//                    if(!memory.isNull("image")) {
//                        image = memory.getJSONObject("image");
//                    } else {
//                        image.put("uri", "");
//                    }
//
//                    exampleList.add(new DiaryItems(
//                            memory.getInt("id"),
//                            image.getString("uri"),
//                            memory.getString("title"),
//                            memory.getString("description"),
//                            memory.getString("datetime")));
//                }
            }
        }
    }
