package com.mrhacker5476.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Edit extends AppCompatActivity implements View.OnClickListener,AsyncResponse {
    ImageView imageView;
    Button upload,update;
    String email;
    SharedPreferences sf;
    Bitmap bitmap;
    RegisterBean rb;
    Intent getData;
    String file="register";
    String toEn;
    JSONObject jsonObject;
    HttpURLConnection urlConnection;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        sf = getSharedPreferences("log", MODE_PRIVATE);
        email=sf.getString("mail","");
        rb=new RegisterBean();
        imageView=findViewById(R.id.profilePicture);
        rb.setEmail(email);
        upload=findViewById(R.id.UploadImageButtonEdit);
        update=findViewById(R.id.ConfirmEdit);
        update.setOnClickListener(Edit.this);
        upload.setOnClickListener(Edit.this);
        new getImage().execute();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v==upload){
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,5);
            rb.setPassword("111");
            rb.setMobile("111");
            rb.setGender("gg");
            rb.setFirstName("55");
            rb.setLastName("lol");
        }
        if(v==update){
            new Inner().execute();
        }
    }
    public String BitmaptoString(Bitmap b){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100, stream);
        byte[] bb=stream.toByteArray();
        Log.d("BBBBBBBBBBBBBBBBBBB",Base64.encodeToString(bb,Base64.URL_SAFE));
        return Base64.encodeToString(bb,Base64.URL_SAFE);
    }
    public void updateButton(){
        if(imageView.getDrawable()!=null){
            upload.setText("Update");
        }
        else {
            upload.setText("upload");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==5 && data!=null){
            getData=data;
            imageView.setImageURI(data.getData());
            bitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();
            updateButton();
        }else{
            bitmap=null;
        }
    }

    @Override
    public void processFinish(JSONObject jsonObject) throws JSONException {
        if(jsonObject.get("done").equals(true)){
            Toast.makeText(Edit.this,"Success",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(Edit.this,"Error",Toast.LENGTH_SHORT).show();
    }
    public class Inner extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Edit.this,"Started",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(Edit.this,"Success",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {

                URL urlToRequest = new URL("http://192.168.43.2:9090/local/image.jsp");
                urlConnection = (HttpURLConnection)urlToRequest.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                String q="e="+email+"&i="+BitmaptoString(bitmap);
                Log.d("FFFFFFFFFFF",q);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(q);
                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();
                Log.d("Response",urlConnection.getResponseMessage());
            } catch (Exception e) {
                Log.d("Exception:", e.toString());
            }
            finally {
                urlConnection.disconnect();
            }
            return null ;
        }
    }
    public Bitmap DecodeBase64(String string){
        byte[] decodedString = Base64.decode(string, Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    public class getImage extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Edit.this,"Started",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(Edit.this,"Success",Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitmapp=DecodeBase64(jsonObject.getString("string"));
                imageView.setImageBitmap(bitmapp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            URL url= null;
            try {
                url = new URL("http://192.168.43.2:9090/local/image.jsp?gg="+email);

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
                String res;
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
    }
}
