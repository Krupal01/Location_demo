package com.example.locationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static String LATITUDE = "latitude";
    public static String LONGITUDE ="longitude";
    private RecyclerView rcLocation;
    private Button btnCopyTxt;
    private LocationManager locationManager;
    private Boolean isGps, isNetwork;
    private Location locationByGps , locationByNetwork;
    private ArrayList<HashMap<String,String>> locationArrayList = new ArrayList<>();
    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcLocation = findViewById(R.id.rcLocation);
        btnCopyTxt = findViewById(R.id.btnCopyText);

        locationAdapter = new LocationAdapter(locationArrayList);
        rcLocation.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rcLocation.setAdapter(locationAdapter);

        
        btnCopyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsArray = new JSONArray(locationArrayList);
                Log.i("jsonArray",jsArray.toString());
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_TEXT,jsArray.toString() );
                startActivity(i);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isLocationPermission()) {
            getLocation();
        }

    }

    public boolean isLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    202
            );
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 202 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

    public void getLocation() {
//        if (isNetwork) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            locationManager.requestLocationUpdates(
//                    LocationManager.NETWORK_PROVIDER,
//                    1000,
//                    0F,
//                    new LocationListener() {
//                        @Override
//                        public void onLocationChanged(@NonNull Location location) {
//                            locationByNetwork = location;
//                        }
//                    }
//            );
//        }
        if (isGps){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    0F,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            locationByGps = location;
                            HashMap<String,String> locationMap = new HashMap<>();
                            locationMap.put(LATITUDE,String.valueOf(locationByGps.getLatitude()));
                            locationMap.put(LONGITUDE,String.valueOf(locationByGps.getLongitude()));
                            locationArrayList.add(locationMap);
                            locationAdapter.notifyDataSetChanged();
                        }
                    }
            );
        }
    }

}