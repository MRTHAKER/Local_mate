package com.mrhacker5476.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,AsyncResponse {
EditText username,password;
CheckBox keeplogin;
Button loginB,register;
LoginBean lb;
String file="login";
Register_loginSource rls;
    SharedPreferences sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sf = getSharedPreferences("log",MODE_PRIVATE);
        if(sf.getBoolean("log",Boolean.FALSE)){
            startActivity(new Intent(Login.this,Welcome.class));
            finish();
        }
        else {
            username = (EditText) findViewById(R.id.usernameLogin);
            password = (EditText) findViewById(R.id.PasswordLogin);
            keeplogin = (CheckBox) findViewById(R.id.KeepLoginCheck);
            loginB = (Button) findViewById(R.id.LoginButton);
            register = (Button) findViewById(R.id.LoginButtonrEG);
            loginB.setOnClickListener(Login.this);
            register.setOnClickListener(Login.this);
            keeplogin.setOnCheckedChangeListener(Login.this);
            lb = new LoginBean();
        }
    }

    @Override
    public void onClick(View v) {
        if(loginB.getId()==v.getId())
         {
             if(isNetworkStatusAvialable(Login.this)) {
                 lb.setEmail(username.getText().toString());
                 lb.setPassword(password.getText().toString());
                 new SqlCall(Login.this, file, lb, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
             }
             else {
                 Toast.makeText(Login.this,"Please enable Internet Connection",Toast.LENGTH_LONG).show();
             }
        }
        if(v.getId()==register.getId())
        {
            Intent intent=new Intent(Login.this,Registration.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView==keeplogin){
            SharedPreferences.Editor se=sf.edit();
            if(keeplogin.isChecked()){
                se.putBoolean("log",Boolean.TRUE).apply();
            }
            else{
                se.putBoolean("log",Boolean.FALSE).apply();
            }
        }
    }
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }

    @Override
    public void processFinish(JSONObject jsonObject) throws JSONException {
        if(jsonObject.get("done").equals(true)){
            Toast.makeText(Login.this,"Login Success.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Login.this,Welcome.class);
            intent.putExtra("mail",lb.Email);
            startActivity(intent);
            finish();
        }
        else Toast.makeText(Login.this,"Email/Password is wrong",Toast.LENGTH_SHORT).show();
    }
}
