package demofilereadwriting.android.myapplicationdev.com.p09_gettingmylocations;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileWriter;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    boolean started;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    String folderLocation;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Service", "Created");


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (started == false){
            started = true;

            mGoogleApiClient.connect();

            Log.d("Service", "Started");
        } else {
            Log.d("Service", "Still running");
            mGoogleApiClient.connect();
            Toast.makeText(this, "Service still running",
                    Toast.LENGTH_SHORT).show();



            String data = mLocation.getLatitude() +  ", " + mLocation.getLongitude();
            folderLocation = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/Location";
            File targetFile = new File(folderLocation, "location.txt");

            try {
                FileWriter writer = new FileWriter(targetFile, true);
                writer.write(data +"\n");
                writer.flush();
                writer.close();
            } catch (Exception e) {
                Toast.makeText(MyService.this, "Failed to write!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyService.this);
            SharedPreferences.Editor prefEdit = prefs.edit();
            prefEdit.putString("latitude", String.valueOf(mLocation.getLatitude()));
            prefEdit.putString("longitude", String.valueOf(mLocation.getLongitude()));
            prefEdit.commit();

        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Log.d("Service", "Exited");
        super.onDestroy();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MyService.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MyService.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setSmallestDisplacement(100);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

            folderLocation = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/Location";

            File folder = new File(folderLocation);
            if (folder.exists() == false){
                boolean result = folder.mkdir();
                if (result == true){
                    Log.d("File Read/Write", "Folder created");
                }
            }

        } else {
            mLocation = null;
            Toast.makeText(MyService.this,
                    "Permission not granted to retrieve location info",
                    Toast.LENGTH_SHORT).show();
        }

        if (mLocation != null) {
            Toast.makeText(this, "Lat : " + mLocation.getLatitude() +
                            " Lng : " + mLocation.getLongitude(),
                    Toast.LENGTH_SHORT).show();


            String data = mLocation.getLatitude() +  ", " + mLocation.getLongitude();
            folderLocation = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/Location";
            File targetFile = new File(folderLocation, "location.txt");

            try {
                FileWriter writer = new FileWriter(targetFile, true);
                writer.write(data +"\n");
                writer.flush();
                writer.close();
            } catch (Exception e) {
                Toast.makeText(MyService.this, "Failed to write!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyService.this);
            SharedPreferences.Editor prefEdit = prefs.edit();
            prefEdit.putString("latitude", String.valueOf(mLocation.getLatitude()));
            prefEdit.putString("longitude", String.valueOf(mLocation.getLongitude()));
            prefEdit.commit();



        } else {
            Toast.makeText(this, "Location not Detected",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        String data = location.getLatitude() +  ", " + location.getLongitude();
        folderLocation = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Location";
        File targetFile = new File(folderLocation, "location.txt");

        try {
            FileWriter writer = new FileWriter(targetFile, true);
            writer.write(data +"\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Toast.makeText(MyService.this, "Failed to write!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyService.this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("currentlatitude", String.valueOf(mLocation.getLatitude()));
        prefEdit.putString("currentlongitude", String.valueOf(mLocation.getLongitude()));
        prefEdit.commit();



    }
}
