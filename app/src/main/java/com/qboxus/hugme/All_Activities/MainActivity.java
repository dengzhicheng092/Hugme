package com.qboxus.hugme.All_Activities;

import android.annotation.SuppressLint;
import android.content.Context;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.All_Fragments.Main_F;
import com.qboxus.hugme.All_Fragments.Swipe_F;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    Main_F fragment;
    long mBackPressed;
    Context context;

    BillingProcessor billingProcessor;


    public static String user_id;
    public static String token;
    DatabaseReference rootref;

    public static boolean purduct_purchase=false;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        SharedPrefrence.init_share(this);

        MobileAds.initialize(this);

        Functions.display_fb_ad(context);

        rootref= FirebaseDatabase.getInstance().getReference();

        user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);

        token=FirebaseInstanceId.getInstance().getToken();
        if(token==null || (token.equals("")||token.equals("null")))
            token=SharedPrefrence.get_string(this,SharedPrefrence.u_Device_token);

        billingProcessor = new BillingProcessor(this, Variables.licencekey, this);
        billingProcessor.initialize();


        fragment = new Main_F();
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        FT.addToBackStack(null);
        FT.replace(R.id.RL_id, fragment).commit();



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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Functions.unRegisterConnectivity(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {

        if(Swipe_F.behavior.getState() == 3){

            Swipe_F.behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        }else{

            if (!fragment.onBackPressed()) {
                int count = this.getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    if (mBackPressed + 2000 > System.currentTimeMillis()) {
                        super.onBackPressed();
                        return;
                    } else {
                       Toast.makeText(getBaseContext(), "Tap Again To Exit", Toast.LENGTH_SHORT).show();
                        mBackPressed = System.currentTimeMillis();

                    }
                } else {
                    super.onBackPressed();

                }
            }

            }

        }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            rootref.child("Users").child(user_id).child("token").setValue(token);
        }catch (Exception e){

        }

    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        SharedPrefrence.pref.edit().putBoolean(SharedPrefrence.is_puchase,true).commit();
        purduct_purchase=true;
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

        if(billingProcessor.loadOwnedPurchasesFromGoogle()){
            if((billingProcessor.isSubscribed(Variables.product_ID) ||
                    billingProcessor.isSubscribed(Variables.product_ID2)) || billingProcessor.isSubscribed(Variables.product_ID3)){

                SharedPrefrence.save_boolean(this,true,SharedPrefrence.is_puchase);
                purduct_purchase=true;
                billingProcessor.release();
                Call_Api_For_update_purchase("1");
            }else {
                SharedPrefrence.save_boolean(this,false,SharedPrefrence.is_puchase);
                purduct_purchase=false;
                Call_Api_For_update_purchase("0");
            }



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

            for(Fragment fragment1 :fragment.getChildFragmentManager().getFragments()){
                fragment1.onActivityResult(requestCode, resultCode, data);
            }
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }



    private void Call_Api_For_update_purchase(String purchase_value) {
        String user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("fb_id", user_id);
            parameters.put("purchased",purchase_value);

        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(this, Api_Links.update_purchase_Status, parameters, null);


    }


}
