package com.example.orbis;

import org.json.JSONObject;

public interface APICallback {
    void onSuccessResponse(JSONObject response);
}
