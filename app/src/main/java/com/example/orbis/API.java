package com.example.orbis;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * API class, connect to the Orbis API.
 */
class API {
    private static final String TAG = "API"; //stores tag for logging
    private RequestQueue requestQueue; //stores request que
    private Context appContext; //stores app context
    private SharedPreferences prefs; //stores our session id

    /**
     *
     * Initialize API
     *
     * @param context App context
     */
    API(Context context) {
        appContext = context;

        //create queue
        requestQueue = Volley.newRequestQueue(context);

        //get prefs from preference manager
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set session id in storage
     *
     * @param sessionId
     */
    void setSession(String sessionId) {
        prefs.edit().putString("session_id", sessionId).apply();
    }

    /**
     * Get session id from storage
     *
     * @return String
     */
    String getSession() {
        //TODO remove test from api session id
        return prefs.getString("session_id", "");
    }

    /**
     * Make a request to the API
     *
     * @param requestUrl url without base and leading slash
     * @param jsonBody json body data, can be null
     * @param callback callback function
     */
    void request(String requestUrl, JSONObject jsonBody, final APICallback callback) {
        String baseUrl = appContext.getString(R.string.API_base_url); //get base url
        String url = baseUrl + requestUrl; //combine base url and request url

        if(!getSession().equals("")) {
            try {
                jsonBody.put("session_id", getSession());
            } catch (JSONException e) {
                Log.i(TAG, e.getMessage()); //log error to the logger
            }
        }


        //Make request
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccessResponse(response); //callback function to be overwritten
                Log.i(TAG, response.toString()); //log response to the logger
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage()); //log error message to logger
            }
        });

        //add to request que
        requestQueue.add(jsonObject);
    }
}