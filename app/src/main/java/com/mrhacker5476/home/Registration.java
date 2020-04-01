package com.mrhacker5476.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
public class Registration extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,AsyncResponse {
EditText firstname,lastname,email,password,mobile;
RadioGroup genderRadio;
Button register;
RadioButton male,female;
String gender;
RegisterBean rb;
Register_loginSource rls;
ProgressDialog progressDialog;
String file="register";

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
        progressDialog = new ProgressDialog(Registration.this);
        progressDialog.setTitle("Registering you please wait..");
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
                Toast.makeText(this,"Field Can't be empty",Toast.LENGTH_LONG).show();
            }
            else {
                if (isNetworkStatusAvialable(Registration.this)) {
                    rb.setEmail(email.getText().toString());
                    rb.setFirstName(firstname.getText().toString());
                    rb.setLastName(lastname.getText().toString());
                    rb.setPassword(password.getText().toString());
                    rb.setMobile(mobile.getText().toString());
                    progressDialog.setTitle("Processing, please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new SqlCall( file, rb, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(Registration.this,"Please Enable Internet Connection.",Toast.LENGTH_LONG).show();
                }
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

    @Override
    public void processFinish(JSONObject jsonObject) throws JSONException {
        progressDialog.dismiss();
        if(jsonObject.get("done").equals(true)){
            Toast.makeText(Registration.this,"Success.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Registration.this,Login.class);
            startActivity(intent);
            finish();
        }
        else Toast.makeText(Registration.this,"Email, Already registered.",Toast.LENGTH_SHORT).show();
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
}
