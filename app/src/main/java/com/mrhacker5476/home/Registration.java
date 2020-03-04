package com.mrhacker5476.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Set;

public class Registration extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
EditText firstname,lastname,email,password,mobile;
RadioGroup genderRadio;
Button register;
RadioButton male,female;
String gender;
RegisterBean rb;
Register_loginSource rls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firstname=(EditText)findViewById(R.id.RegisterFirstName);
        lastname=(EditText)findViewById(R.id.RegisterLastName);
        email=(EditText)findViewById(R.id.RegisterEmail);
        password=(EditText)findViewById(R.id.RegisterPassword);
        mobile=(EditText)findViewById(R.id.RegisterMobile);
        genderRadio=(RadioGroup)findViewById(R.id.RadioGroupGender);
        register=(Button)findViewById(R.id.buttonRegister);
        male=(RadioButton)findViewById(R.id.radioMale);
        female=(RadioButton)findViewById(R.id.radioFemale);
        this.rb=new RegisterBean();
        rls=new Register_loginSource(rb,Registration.this);
        register.setOnClickListener(Registration.this);
        genderRadio.setOnCheckedChangeListener(Registration.this);

    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==register.getId())
        {
            if(firstname.getText().toString().equals("")||lastname.getText().toString().equals("")|| email.getText().toString().equals("")||password.getText().toString().equals("")||mobile.getText().toString().equals("")){
                Toast.makeText(this,"Field Can't be empty",Toast.LENGTH_SHORT).show();
            }
            else{
            rb.setEmail(email.getText().toString());
            rb.setFirstName(firstname.getText().toString());
            rb.setLastName(lastname.getText().toString());
            rb.setPassword(password.getText().toString());
            rb.setMobile(mobile.getText().toString());
            rls=new Register_loginSource(rb,Registration.this);
            rls.insert();
            Intent intent = new Intent(Registration.this,Login.class);
            startActivity(intent);
            finish();
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if(checkedId==male.getId())
        {
            if(male.isChecked())
            {
                rb.setGender("male");
            }
            else {
                rb.setGender("female");
            }
        }
    }
}
