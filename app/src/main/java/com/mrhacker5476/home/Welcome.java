package com.mrhacker5476.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressLint("NewApi")
public class Welcome extends AppCompatActivity implements View.OnClickListener,LocationListener {
    Button logout;
    SharedPreferences sf;
    SharedPreferences.Editor se;
    TextView lock;
    LocationManager lm;
    Geocoder gc;
    MaterialToolbar mt;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logout = (Button) findViewById(R.id.logout);
        lock = (TextView) findViewById(R.id.lock);
        mt= findViewById(R.id.mt);
        logout.setOnClickListener(Welcome.this);
        gc = new Geocoder(this, Locale.getDefault());
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        makeCall();
    }

@SuppressLint({"MissingPermission", "NewApi"})
public void makeCall(){
    if (CheckPermission()) {
        if(lm.isLocationEnabled()) {
            if (isNetworkStatusAvialable(this)) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                Toast.makeText(Welcome.this, "Getting you location this may take a while", Toast.LENGTH_LONG).show();
            }else  Toast.makeText(this,"enable internet",Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this,"Enable GPS",Toast.LENGTH_SHORT).show();

    }
    else{
        RequestPermission();
    }
}
    private void RequestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        if (v == logout) {
            sf = getSharedPreferences("log", MODE_PRIVATE);
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

    @Override
    public void onLocationChanged(Location location) {
        try {
            List<Address> ad = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            mt.setTitle(ad.get(0).getAdminArea() + ", " + ad.get(0).getLocality() + ", " + ad.get(0).getSubLocality()+", "+ad.get(0).getPostalCode());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
        builder.setTitle("Please");
        builder.setMessage("Please enable gps and internet otherwise application won't work properly").setNegativeButton("OK", null).show();
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

