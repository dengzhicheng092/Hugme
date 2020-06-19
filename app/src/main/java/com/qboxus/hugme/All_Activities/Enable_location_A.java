package com.qboxus.hugme.All_Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

public class Enable_location_A extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Context context;
    Button enable_location_btn;
    SharedPreferences sharedPreferences;
    IOSDialog iosDialog;
    String user_info;
    LocationManager locationManager;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_location);

        context = Enable_location_A.this;
        user_info = SharedPrefrence.get_string(context,"" + SharedPrefrence.u_login_detail);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        iosDialog = new IOSDialog.Builder(context)
                .setCancelable(false)
                .setSpinnerClockwise(false)
                .setMessageContentGravity(Gravity.END)
                .build();
        enable_location_btn= findViewById(R.id.enable_location_btn);
        enable_location_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
               GPSStatus();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermission() {

        try{

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                   123);
        }catch (Exception b){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case  123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission Granted ", Toast.LENGTH_SHORT).show();
                    GPSStatus();
                } else {
                    Toast.makeText(context, "Please Grant permission", Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void GPSStatus(){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GpsStatus) {
            Toast.makeText(context, "On Location in High Accuracy", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),2);
        }
        else {
            GetCurrentlocation();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            GPSStatus();
        }
    }


    private FusedLocationProviderClient mFusedLocationClient;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void GetCurrentlocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            iosDialog.cancel();
            getLocationPermission();
            return;
        }

        buildGoogleApiClient();
        createLocationRequest();

    }


    public void Go_Next(Location location){

        if (location != null) {

            location.getLatitude();
            location.getLongitude();
            String location_string = "" + location.getLatitude() + ", " +  location.getLongitude();

            SharedPrefrence.save_string(context,"" + location_string,
                    "" +  SharedPrefrence.u_lat_lng);


            if(user_info != null){

                startActivity(new Intent(context, MainActivity.class));
                finish();

            }else{
               startActivity(new Intent(context, Login_A.class));
                finish();
            }

            stopLocationUpdates();
            mGoogleApiClient.disconnect();
            mFusedLocationClient.removeLocationUpdates(locationCallback);

        } else {

        }
    }



    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 3000;
    private static int FATEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 0;
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }


    LocationCallback locationCallback;
    protected void startLocationUpdates() {
        mGoogleApiClient.connect();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        Go_Next(location);
                        stopLocationUpdates();

                    }
                }


            }
        };


        mFusedLocationClient.requestLocationUpdates(mLocationRequest,locationCallback
                , Looper.myLooper());

    }


    protected void stopLocationUpdates() {
        mGoogleApiClient.disconnect();
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }


    @Override
    public void onDestroy() {
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();

        }
       super.onDestroy();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onConnected(Bundle arg0) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }





}
