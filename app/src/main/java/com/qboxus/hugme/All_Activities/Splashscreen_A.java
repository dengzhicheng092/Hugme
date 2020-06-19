package com.qboxus.hugme.All_Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.material.snackbar.Snackbar;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.CallBack;

import io.fabric.sdk.android.Fabric;


public class Splashscreen_A extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ImageView iv;
    String user_info;
    Context context;
    LocationManager locationManager;
    Handler handler;
    Runnable runnable;

    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splashscreen);
        context = Splashscreen_A.this;
        iv = (ImageView) findViewById(R.id.splashscreen_id);
        Variables.Var_tab_change = 0;


        Variables.height = getResources().getDisplayMetrics().heightPixels;
        Variables.width = getResources().getDisplayMetrics().widthPixels;

        user_info = SharedPrefrence.get_string(context,SharedPrefrence.u_login_detail);


        handler = new Handler();
        runnable=new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                if(user_info != null){

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(context, Enable_location_A.class));

                    }else {


                        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER )  ){
                            Functions.toast_msg(context,"GPS is disabled! ");
                            startActivity(new Intent(context, Enable_location_A.class));
                            finish();
                        }else{

                            GPSStatus();

                        }
                    }
                }else{
                    startActivity(new Intent(context, Login_A.class));
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, 3000);


        if (getIntent().getExtras() != null) {

            final Handler handler_1 = new Handler();
            handler_1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getIntent().getExtras() != null) {
                        String icon = "", Rec_id = "";
                        for (String key : getIntent().getExtras().keySet()) {
                            String value = getIntent().getExtras().getString(key);

                            if(key.equals("receiverid")){
                                Rec_id = getIntent().getExtras().getString("senderid");
                            }else if(key.equals("icon")){
                                icon = getIntent().getExtras().getString("icon");
                           }

                            if(value != null){

                                if(value.equals("messege")){

                                    Intent myIntent = new Intent(context, Chat_A.class);
                                    myIntent.putExtra("receiver_id", "" + Rec_id);
                                    myIntent.putExtra("receiver_name", "");
                                    myIntent.putExtra("receiver_pic", "" + icon);
                                    myIntent.putExtra("match_api_run", "0");
                                    startActivity(myIntent);


                                }

                            }


                        }
                    }
                }
            }, 3000);

        }


        Functions.RegisterConnectivity(this, new CallBack() {
            @Override
            public void Get_Response(String requestType, String response) {
                if(response.equalsIgnoreCase("disconnected")) {
                    snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet. Connect to wifi or cellular network.", Snackbar.LENGTH_INDEFINITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                }else {

                    if(snackbar!=null)
                        snackbar.dismiss();
                }
            }
        });

    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermission() {
        // todo;
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
                    Toast.makeText(context, "Granted ", Toast.LENGTH_SHORT).show();
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
            String location_string = location.getLatitude() + ", " + location.getLongitude();
            String search_place = SharedPrefrence.get_string(context, "" + SharedPrefrence.share_user_search_place_name);
            if (search_place != null) {

                if (search_place.equals("People nearby")) {

                    SharedPrefrence.save_string(context, location_string,
                            SharedPrefrence.u_lat_lng);

                }

            } else {
                SharedPrefrence.save_string(context, location_string,
                         SharedPrefrence.u_lat_lng);

            }

        }
    }



    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 1000;
    private static int FATEST_INTERVAL = 1000;
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
                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        Go_Next(location);
                        stopLocationUpdates();

                    }
                }
                if(user_info != null){

                    Intent intent_1 = new Intent(context, MainActivity.class);
                    startActivity(intent_1);
                    finish();

                }

            }
        };


        mFusedLocationClient.requestLocationUpdates(mLocationRequest,locationCallback
                , Looper.myLooper());

    }


    protected void stopLocationUpdates() {

        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            mGoogleApiClient.disconnect();
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }


    @Override
    public void onDestroy() {
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();

        }

        if(handler!=null && runnable!=null)
            handler.removeCallbacks(runnable);

        Functions.unRegisterConnectivity(this);

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
