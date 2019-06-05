package com.example.orbis;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

class API {
    private static final String TAG = "API";
    private RequestQueue requestQueue;
    private Context appContext;

    API(Context context) {
        appContext = context;

        requestQueue = Volley.newRequestQueue(context);
    }

    void request(String requestUrl, @Nullable JSONObject jsonBody, final APICallback callback) {
        String baseUrl = appContext.getString(R.string.API_base_url);
        String url = baseUrl + requestUrl;

        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccessResponse(response);
                Log.i(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });

        requestQueue.add(jsonObject);
    }
}
