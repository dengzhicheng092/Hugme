package com.qboxus.hugme.InAppSubscription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.material.tabs.TabLayout;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.qboxus.hugme.All_Activities.MainActivity;
import com.qboxus.hugme.All_Activities.Splashscreen_A;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import com.qboxus.hugme.Volley_Package.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InApp_Subscription_A extends RootFragment implements BillingProcessor.IBillingHandler, View.OnClickListener {

    BillingProcessor bp;
    View view;
    Context context;
    Button purchase_btn;

    TextView Goback;

    LinearLayout sub_layout1,sub_layout2,sub_layout3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate
        // the layout for this fragment
        view= inflater.inflate(R.layout.activity_in_app_subscription, container, false);
        context=getContext();


        purchase_btn=view.findViewById(R.id.purchase_btn);
        purchase_btn.setOnClickListener(this);


        Goback=view.findViewById(R.id.Goback);
        Goback.setOnClickListener(this);

        Set_Slider();


        sub_layout1= view.findViewById(R.id.sub_layout1);
        sub_layout2= view.findViewById(R.id.sub_layout2);
        sub_layout3= view.findViewById(R.id.sub_layout3);


        Select_one(2);

        sub_layout1.setOnClickListener(this);
        sub_layout2.setOnClickListener(this);
        sub_layout3.setOnClickListener(this);

        return view;
    }


    int subscription_position;
    public void Select_one(int position){
        subscription_position=position;

        sub_layout1.setBackground(context.getResources().getDrawable(R.drawable.d_round_gray_border));
        sub_layout2.setBackground(context.getResources().getDrawable(R.drawable.d_round_gray_border));
        sub_layout3.setBackground(context.getResources().getDrawable(R.drawable.d_round_gray_border));

        if(position==1){
            sub_layout1.setBackground(context.getResources().getDrawable(R.drawable.d_round_blue_border_radius15));

        }
        else if(position==2){
            sub_layout2.setBackground(context.getResources().getDrawable(R.drawable.d_round_blue_border_radius15));

        }
        else if(position==3){
           sub_layout3.setBackground(context.getResources().getDrawable(R.drawable.d_round_blue_border_radius15));

        }


    }


    public void initlize_billing(){


        Functions.Show_loader(context,false,false);

        bp = new BillingProcessor(context, Variables.licencekey, this);
        bp.initialize();


    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

        // if the user is subcripbe successfully then we will store the data in local and also call the api
        SharedPrefrence.pref.edit().putBoolean(SharedPrefrence.is_puchase,true).commit();
        MainActivity.purduct_purchase=true;
        Call_Api_For_update_purchase("1");

    }


    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        // on billing intialize we will get the data from google
        Functions.cancel_loader();

        if(bp.loadOwnedPurchasesFromGoogle()){
            // check user is already subscribe or not
        if((bp.isSubscribed(Variables.product_ID) || bp.isSubscribed(Variables.product_ID2))
                || bp.isSubscribed(Variables.product_ID3)){
            // if already subscribe then we will change the static variable and goback
            MainActivity.purduct_purchase=true;
            Call_Api_For_update_purchase("1");
        }

        }
    }



    // when we click the continue btn this method will call
    public void Puchase_item() {
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(getActivity());
        if(isAvailable) {

            if(subscription_position==1){
                bp.subscribe(getActivity(), Variables.product_ID);
            }else if(subscription_position==2){
                bp.subscribe(getActivity(), Variables.product_ID2);
            }
            else if(subscription_position==3){
                bp.subscribe(getActivity(), Variables.product_ID3);
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("responce", "onActivity Result Code : " + resultCode);
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            Animation anim= MoveAnimation.create(MoveAnimation.UP, enter, 300);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    initlize_billing();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            return anim;

        } else {
            return MoveAnimation.create(MoveAnimation.DOWN, enter, 300);
        }
    }


    // on destory we will release the billing process
    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }


    public void Goback() {

        startActivity(new Intent(getActivity(), Splashscreen_A.class));
        getActivity().finish();

    }


    // when user subscribe the app then this method will call that will store the status of user
    // into the database
    private void Call_Api_For_update_purchase(String purchase_value) {

        JSONObject parameters = new JSONObject();
        try {

            String user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);

            parameters.put("fb_id", user_id);
            parameters.put("purchased",purchase_value);

        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.Show_loader(context,false,false);

        ApiRequest.Call_Api(context, Api_Links.update_purchase_Status, parameters, new CallBack() {
            @Override
            public void Get_Response(String requestType, String response) {
                Functions.cancel_loader();
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    SharedPrefrence.pref.edit().putBoolean(SharedPrefrence.is_puchase,true).commit();
                    MainActivity.purduct_purchase=true;

                    Goback();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } );


    }


    private ViewPager mPager;
    private ArrayList<Integer> ImagesArray;
    public void Set_Slider(){

        ImagesArray=new ArrayList<>();
        ImagesArray.add(0);
        ImagesArray.add(1);
        mPager = (ViewPager) view.findViewById(R.id.image_slider_pager);

        try {
            mPager.setAdapter(new SlidingImageAdapter(getContext(),ImagesArray));
        }
        catch (NullPointerException e){
            e.getCause();
        }

        mPager.setCurrentItem(0);

        TabLayout indicator = (TabLayout) view.findViewById(R.id.indicator);
        indicator.setupWithViewPager(mPager, true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.purchase_btn:
                Puchase_item();
                break;

            case R.id.Goback:
                getActivity().onBackPressed();
                break;

            case R.id.sub_layout1:
                Select_one(1);
                break;

            case R.id.sub_layout2:
                Select_one(2);
                break;

            case R.id.sub_layout3:
                Select_one(3);
                break;

        }
    }

}
