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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.usernameLogin);
        password=(EditText)findViewById(R.id.PasswordLogin);
        keeplogin=(CheckBox)findViewById(R.id.KeepLoginCheck);
        loginB=(Button)findViewById(R.id.LoginButton);
        register=(Button)findViewById(R.id.LoginButtonrEG);
        loginB.setOnClickListener(Login.this);
        register.setOnClickListener(Login.this);
    }

    @Override
    public void onClick(View v) {
        if(loginB.getId()==v.getId())
         {
            Helper h = new Helper(getApplicationContext());
            /*if(h.CheckLog(username.getText().toString(),password.getText().toString())){
                Toast.makeText(Login.this,"Ok",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Login.this,"NO",Toast.LENGTH_SHORT).show();
            }*/
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

    }
}
