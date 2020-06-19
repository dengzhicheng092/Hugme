package com.qboxus.hugme.All_Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.google.android.material.snackbar.Snackbar;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Utils.FontHelper;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;

public class Login_A extends AppCompatActivity {

    TextView male,female;

    private CallbackManager callbackManager;
    LoginButton loginButton;
    private AccessToken mAccessToken;
    private static final int RC_SIGN_IN = 007;
    private static final String EMAIL = "email";
    private GoogleApiClient mGoogleApiClient;
    String name, email, user_id, password, img_url;
    FrameLayout gmail_FrameLayout2, fb_FrameLayout1;
    Context context;

    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        context = Login_A.this;

        FontHelper.applyFont(getApplicationContext(),findViewById(R.id.Main_frame_Layout), Variables.verdana);

        male = (TextView) findViewById(R.id.male_TV_id);
        female = (TextView) findViewById(R.id.female_TV_id);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login_A.this, Segments_A.class));
            }
        });




        findViewById(R.id.phone_login_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login_A.this, Login_Phone_A.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

            }
        });


           Runtime_permission();



        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_A.this, Segments_A.class));
            }
        });


         fb_FrameLayout1 = findViewById(R.id.FrameLayout1);
        gmail_FrameLayout2 = findViewById(R.id.FrameLayout2);

        google_sign_in();

        fb_FrameLayout1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 loginButton.performClick();

            }
        });

        gmail_FrameLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });

        loginButton = findViewById(R.id.login_details_fb_iV_id);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mAccessToken = loginResult.getAccessToken();
                getUserProfile(mAccessToken);

            }
            @Override
            public void onCancel() {
                Functions.toast_msg(Login_A.this,"You cancel this.");
            }
            @Override
            public void onError(FacebookException error) {
                Functions.toast_msg(Login_A.this,"err " + error.toString());
                Log.d("error",""+error.toString());
            }
        });


        printKeyHash();


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




    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String fb_user_id = object.getString("id");
                            user_id=fb_user_id;
                            String imgurl = "https://graph.facebook.com/"+object.getString("id")+"/picture?width=500";
                            String fb_name = object.getString("name");
                            String fb_email = object.getString("email");

                            Log.d("hugme","pic link"+ imgurl);

                            try{
                                JSONObject FB_data_json = new JSONObject();
                                 FB_data_json.put("name", fb_name);
                                 FB_data_json.put("email", fb_email);
                                 FB_data_json.put("user_id", fb_user_id);
                                 FB_data_json.put("img_url",imgurl);

                                SharedPrefrence.save_string(context,FB_data_json.toString(),
                                        "" + SharedPrefrence.share_social_info);

                                getuser_info(user_id, FB_data_json);


                            }catch (Exception b){

                            }


                            LoginManager.getInstance().logOut();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Functions.toast_msg(Login_A.this,"Error "+e.toString());
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }


    public void google_sign_in()
    {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        Context context = Login_A.this;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(Login_A.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                     }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            try{
                    name = acct.getDisplayName();
                    user_id = acct.getId();
                    if(acct.getPhotoUrl() != null){
                        img_url = acct.getPhotoUrl().toString();
                    }else{
                        img_url = "";
                    }

                    email = acct.getEmail();
                    name = acct.getDisplayName();
                    password = acct.getId();

                try{
                    JSONObject Msgs_json = new JSONObject();
                    Msgs_json.put("name", name);
                    Msgs_json.put("email", email);
                    Msgs_json.put("user_id", user_id);
                    Msgs_json.put("img_url",img_url);


                    // TODO: Check if user ALready member or not
                    getuser_info(user_id,Msgs_json);



                }catch (Exception b){

                }


            }catch (Exception v){
                Functions.toast_msg(Login_A.this,""+v.toString());

            }
        } else {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode,  data);


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }


    }

  public void Runtime_permission (){

        ActivityCompat.requestPermissions(Login_A.this,
                new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA

                },
                1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    finish();
                }
                return;
            }

        }
    }



    public void getuser_info (final String user_id, final JSONObject Msgs_json){
          JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(
                context,
                Api_Links.getUserInfo,
                parameters,
                new CallBack() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void Get_Response(String requestType, String resp) {
                        try{
                            JSONObject response=new JSONObject(resp);


                            if(response.getString("code").equals("200")) {


                                JSONArray msg_obj = response.getJSONArray("msg");
                                JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                 SharedPrefrence.save_string(context,"" + user_info_obj.toString(),
                                        "" + SharedPrefrence.u_login_detail);


                                SharedPrefrence.save_string(context,"" + user_id, "" +
                                        SharedPrefrence.u_id
                                        );

                                    SharedPrefrence.remove_value_of_key(context,
                                        "" + SharedPrefrence.share_social_info);

                                Open_enable_location();


                            }else{

                                SharedPrefrence.save_string(context,Msgs_json.toString(),
                                        "" + SharedPrefrence.share_social_info);

                                startActivity(new Intent(Login_A.this, Segments_A.class));

                            }

                            }catch (Exception b){
                            Functions.toast_msg(context,"" + b.toString());
                        }

                    }
                }
        );






    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Functions.unRegisterConnectivity(this);
    }

    public void Open_enable_location(){
        context.startActivity(new Intent(context, Enable_location_A.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }


    public void printKeyHash()  {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName() , PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("keyhash" , Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
