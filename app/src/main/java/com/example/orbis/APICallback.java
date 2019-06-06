package com.example.orbis;

import org.json.JSONObject;

/**
 * API Callback function
 *
 * To be overwritten with own implementation
 */
public interface APICallback {
    /**
     * @param response
     */
    void onSuccessResponse(JSONObject response);
}
