package com.mrhacker5476.home;

import org.json.JSONException;
import org.json.JSONObject;

public interface AsyncResponse {
    void processFinish(JSONObject jsonObject) throws JSONException;
}
