package com.mrhacker5476.home;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SqlCall extends AsyncTask {
    String res;
    public AsyncResponse delegate;
    JSONObject jsonObject;
    Object object;
    String file;
    SqlCall(String file,Object object,AsyncResponse delegate){
        this.file=file;
        this.object=object;
        this.delegate=delegate;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        URL url= null;
        try {
            url = new URL("http://192.168.43.2:9090/local/" + file + ".jsp?" + object.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder a = new StringBuilder();
            while ((res = bufferedReader.readLine()) != null)
                a.append(res);
            res=a.toString();
            assert res!=null;
            jsonObject = new JSONObject(res);
        }
        catch(Exception e){
            Log.d("Exception Occurred",e.toString());}
        finally {
            urlConnection.disconnect();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try {
            delegate.processFinish(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}