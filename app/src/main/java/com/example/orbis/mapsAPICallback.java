package com.example.orbis;

import org.json.JSONObject;

public class mapsAPICallback implements APICallback {
    public JSONObject test;
    @Override
    public void onSuccessResponse(JSONObject response) {
        test = response;

        System.out.println(response.toString());
    }
}
