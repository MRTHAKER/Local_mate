package com.mrhacker5476.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity implements View.OnClickListener {
Button logout;
SharedPreferences sf;
SharedPreferences.Editor se;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logout=(Button)findViewById(R.id.logout);
        logout.setOnClickListener(Welcome.this);
        sf=getSharedPreferences("log",MODE_PRIVATE);
        se=sf.edit();
    }

    @Override
    public void onClick(View v) {
        if(v==logout){
            se.remove("log");
            se.putBoolean("log",Boolean.FALSE).apply();
            startActivity(new Intent(Welcome.this,Login.class));
            finish();
        }
    }
}
