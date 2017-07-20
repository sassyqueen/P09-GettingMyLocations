package demofilereadwriting.android.myapplicationdev.com.p09_gettingmylocations;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class MainActivity extends AppCompatActivity {

    TextView longitude, latitude, currentlongitude, currentlatitude;
    Button startdetector, stopdetector, checkrecords;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)
                fm.findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                int locationcheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

                if (locationcheck == PermissionChecker.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                } else {
                    Log.e("GMap - Permission", "GPS access has not been granted");
                }



                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String lat = prefs.getString("latitude", "");
                String longt = prefs.getString("longitude", "");
                String currlat = prefs.getString("currentlatitude", "");
                String currlongt = prefs.getString("currentlongitude", "");

                if (lat == "" && longt == ""){
                    LatLng singapore = new LatLng(1.352083, 103.819836);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,
                            11));
                }
                else if (currlat == "" && currlongt == ""){
                    LatLng current = new LatLng(Double.parseDouble(lat), Double.parseDouble(longt));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(current,
                            15));

                    UiSettings ui = map.getUiSettings();
                    ui.setZoomControlsEnabled(true);

                    final Marker n = map.addMarker(new
                            MarkerOptions()
                            .position(current)
                            .title("Current Coordinates")
                            .snippet("Latitude: " + lat + ", Longitude: " + longt)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


                }
                else if (currlat != "" && currlongt != ""){
                    LatLng current = new LatLng(Double.parseDouble(currlat), Double.parseDouble(currlongt));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(current,
                            15));

                    UiSettings ui = map.getUiSettings();
                    ui.setZoomControlsEnabled(true);

                    final Marker n = map.addMarker(new
                            MarkerOptions()
                            .position(current)
                            .title("Current Coordinates")
                            .snippet("Latitude: " + currlat + ", Longitude: " + currlongt)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    currentlatitude.setText("Latitude: " + currlat);
                    currentlongitude.setText("Longitude: " + currlongt);
                }


            }




        });


        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        startdetector = (Button) findViewById(R.id.startdetector);
        stopdetector = (Button) findViewById(R.id.stopdetector);
        checkrecords = (Button) findViewById(R.id.checkrecords);
        currentlatitude = (TextView)findViewById(R.id.latitudecurrent);
        currentlongitude = (TextView)findViewById(R.id.longitudecurrent);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String lat = prefs.getString("latitude", "");
        String longt = prefs.getString("longitude", "");
        if (lat!="" && longt != ""){
            latitude.setText("Latitude: " + lat);
            longitude.setText("Longitude: " + longt);
        }

        int finelocation = PermissionChecker.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        int coarselocation = PermissionChecker.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        int permissionCheck = PermissionChecker.checkSelfPermission
                (MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (coarselocation != PermissionChecker.PERMISSION_GRANTED && finelocation != PermissionChecker.PERMISSION_GRANTED && permissionCheck != PermissionChecker.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            // stops the action from proceeding further as permission not
            //  granted yet
            return;
        }



        startdetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startIntent = new Intent(MainActivity.this, MyService.class);
                startService(startIntent);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String lat = prefs.getString("latitude", "");
                String longt = prefs.getString("longitude", "");
                latitude.setText("Latitude: " + lat);
                longitude.setText("Longitude: " + longt);
            }
        });

        stopdetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, MyService.class);
                stopService(stopIntent);
            }
        });

        checkrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckRecordActivity.class);
                startActivity(intent);
            }
        });
    }


}
