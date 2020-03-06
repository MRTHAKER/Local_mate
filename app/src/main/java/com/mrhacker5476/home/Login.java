package com.mrhacker5476.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentBreadCrumbs;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
EditText username,password;
CheckBox keeplogin;
Button loginB,register;
RegisterBean rb;
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
            rb = new RegisterBean();
            rls = new Register_loginSource(rb, Login.this);
        }
    }

    @Override
    public void onClick(View v) {
        if(loginB.getId()==v.getId())
         {
             rb.setEmail(username.getText().toString());
             rb.setPassword(password.getText().toString());

             if(rls.CheckLogin(rb)){startActivity(new Intent(Login.this,Welcome.class));finish();}
             else Toast.makeText(Login.this,"Username/Password is incorrect",Toast.LENGTH_SHORT).show();
        }
        if(v.getId()==register.getId())
        {
            Intent intent=new Intent(Login.this,Registration.class);
            startActivity(intent);
            finish();
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
}
