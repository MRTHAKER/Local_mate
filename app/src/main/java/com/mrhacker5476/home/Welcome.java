package com.mrhacker5476.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressLint("NewApi")
public class Welcome extends AppCompatActivity implements View.OnClickListener, LocationListener, ActivityCompat.OnRequestPermissionsResultCallback, DialogInterface.OnClickListener,AsyncResponse {
    Button logout;
    SharedPreferences sf;
    SharedPreferences.Editor se;
    TextView lock;
    LocationManager lm;
    Geocoder gc;
    String mail;
    MaterialToolbar mt;
    AlertDialog ab;
    ProgressDialog pd;
    LocationBean lb;
    String file="location";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logout = (Button) findViewById(R.id.logout);
        lock = (TextView) findViewById(R.id.lock);
        mt = findViewById(R.id.mt);
        lb=new LocationBean();
        logout.setOnClickListener(Welcome.this);
        sf = getSharedPreferences("log", MODE_PRIVATE);
        gc = new Geocoder(this, Locale.getDefault());
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Intent intent = getIntent();
        mail = sf.getString("mail",null);
        pd=new ProgressDialog(this);
        pd.setTitle("Processing, please wait.");
        pd.setCancelable(false);
        lb.setEmail(mail);
        makeCall();


    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        if (CheckPermission()) {
            if(lm.isLocationEnabled()) {
                pd.show();
                    lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.getMainLooper());
            } else{
                Toast.makeText(this,"Enable GPS",Toast.LENGTH_LONG).show();

            }

        }
        else{
            RequestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            makeCall();
        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                RequestPermission();
            }
            else{
                ab=new AlertDialog.Builder(this).setMessage("You have permanently denied permission of app, You need to manually grant permission to application from setting, App will close now").setNegativeButton("OK",this).show(); }
        }

    }

    @Override
    public void onClick(View v) {
        if (v == logout) {
            se = sf.edit();
            se.remove("log");
            se.putBoolean("log", Boolean.FALSE).apply();
            startActivity(new Intent(Welcome.this, Login.class));
            finish();
        }
    }

    public Boolean CheckPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void RequestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
    }
    @Override
    public void onLocationChanged(Location location) {
        try {
            List<Address> ad = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String loc=ad.get(0).getAdminArea() + ", " + ad.get(0).getLocality() + ", " + ad.get(0).getSubLocality()+", "+ad.get(0).getPostalCode();
            mt.setTitle(loc);
            lb.setLocation(loc);
            new SqlCall(file,lb,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(dialog==ab){
            finish();
        }
    }

    @Override
    public void processFinish(JSONObject jsonObject) throws JSONException {
        pd.dismiss();
        if(jsonObject.get("done").equals(true)){
            Toast.makeText(Welcome.this,"Success",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(Welcome.this,"Error",Toast.LENGTH_SHORT).show();
    }
}

