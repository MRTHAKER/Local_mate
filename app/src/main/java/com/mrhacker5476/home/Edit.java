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
import android.widget.EditText;
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
import java.sql.ResultSet;


public class Edit extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    Button upload,update;
    String email;
    SharedPreferences sf;
    Bitmap bitmap;
    RegisterBean rb;
    Intent getData;
    String file="";
    JSONObject jsonObject;
    HttpURLConnection urlConnection;
    ProgressDialog progressDialog;
    EditText Email,Phone,FirstName,LastName;
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
        Email=findViewById(R.id.EmailEdit);
        Phone=findViewById(R.id.PhoneEdit);
        FirstName=findViewById(R.id.FirstNameEdit);
        LastName=findViewById(R.id.LastNameEdit);
        progressDialog=new ProgressDialog(Edit.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing, Please wait...");
        new getImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new GetRegister().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v==upload){
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,5);
        }
        if(v==update){
            rb.setMobile(Phone.getText().toString());
            rb.setFirstName(FirstName.getText().toString());
            rb.setLastName(LastName.getText().toString());
            rb.setEmail(Email.getText().toString());
            new Inner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
           new UpdateReg().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
    public String BitmaptoString(Bitmap b){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.WEBP,80, stream);
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
    public class GetRegister extends AsyncTask{
        JSONObject j;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            URL url= null;
            try {
                url = new URL("http://192.168.43.2:9090/local/register.jsp?display="+email);

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
                j = new JSONObject(res);
            }
            catch(Exception e){
                Log.d("Exception Occurred",e.toString());}
            finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Email.setText(email);
            try {
                FirstName.setText(j.getString("FirstName"));
                LastName.setText(j.getString("LastName"));
                Phone.setText(j.getString("Mobile"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class UpdateReg extends AsyncTask{
        JSONObject jj;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            URL url= null;
            try {
                url = new URL("http://192.168.43.2:9090/local/register.jsp?update=ok&Email="+rb.getEmail()+"&FirstName="+rb.getFirstName()+"&LastName="+rb.getLastName()+"&Mobile="+rb.getMobile());

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
                jj = new JSONObject(res);
            }
            catch(Exception e){
                Log.d("Exception Occurred",e.toString());}
            finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (jj.getBoolean("done")==true){
                    Toast.makeText(Edit.this,"Perfect",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Edit.this,"Error",Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class Inner extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
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
            progressDialog.show();
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
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            try {
                imageView.setImageBitmap(DecodeBase64(jsonObject.getString("string")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
