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
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressLint("NewApi")
public class Welcome extends AppCompatActivity implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback, DialogInterface.OnClickListener, AsyncResponse, Toolbar.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sf;
    SharedPreferences.Editor se;
    LocationManager lm;
    Geocoder gc;
    String mail;
    MaterialToolbar mt;
    AlertDialog ab;
    ProgressDialog pd;
    LocationBean lb;
    FrameLayout frame;
    String file="location";
    Main_dashboard dashboard;
    Fragment_recent recent;
    Fragment_Settings settings;
    BottomNavigationView navigationMenu;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mt = findViewById(R.id.mt);
        mt.setOnMenuItemClickListener(this);
        lb=new LocationBean();
        sf = getSharedPreferences("log", MODE_PRIVATE);
        navigationMenu=findViewById(R.id.BottomMenu);
        navigationMenu.setOnNavigationItemSelectedListener(this);
        gc = new Geocoder(this, Locale.getDefault());
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mail = sf.getString("mail",null);
        frame=findViewById(R.id.Mainframe);
        dashboard=new Main_dashboard();
        recent=new Fragment_recent();
        settings=new Fragment_Settings();
        pd=new ProgressDialog(this);
        pd.setTitle("Processing, please wait.");
        pd.setCancelable(false);
        lb.setEmail(mail);
        makeCall();


    }

    private void getFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.Mainframe,dashboard).add(R.id.Mainframe,recent).add(R.id.Mainframe,settings).hide(recent).hide(settings).show(dashboard).commit();
    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        if (CheckPermission()) {
            if(lm.isLocationEnabled()) {
                pd.show();
                    lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.getMainLooper());
            } else{
                Toast.makeText(this,"Enable GPS in high accuracy mode.",Toast.LENGTH_LONG).show();
                Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
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
            dashboard.lb=this.lb;
            new SqlCall(file,lb,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        makeCall();
    }

    @Override
    public void onProviderEnabled(String provider) {
        makeCall();
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
            getFragment();
        }
        else Toast.makeText(Welcome.this,"Error",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.LogoutMenu:
                    se = sf.edit();
                    se.remove("log");
                    se.remove("mail");
                    se.putBoolean("log", Boolean.FALSE).apply();
                    startActivity(new Intent(Welcome.this, Login.class));
                    finish();
                    return true;
            }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                getSupportFragmentManager().beginTransaction().show(dashboard).hide(recent).hide(settings).commit();
                return true;
            case R.id.menu_recent:
                getSupportFragmentManager().beginTransaction().show(recent).hide(dashboard).hide(settings).commit();
                return true;
            case R.id.menu_settings:
                getSupportFragmentManager().beginTransaction().show(settings).hide(dashboard).hide(recent).commit();
                return true;
            default:
                return false;
        }
    }
}

