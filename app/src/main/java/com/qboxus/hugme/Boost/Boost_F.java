package com.qboxus.hugme.Boost;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.mikhaellopez.circularprogressbar.CircularProgressBar;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Boost_F extends RootFragment implements View.OnClickListener {

    View view;
    Context context;
    CircularProgressBar circularProgressBar;



    public Boost_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context=getContext();

       if(!Check_IS_Boost_On()) {
            view = inflater.inflate(R.layout.fragment_boost, container, false);

            view.findViewById(R.id.boost_btn).setOnClickListener(this);

       }

       else {

           view = inflater.inflate(R.layout.fragment_boost_on, container, false);
           circularProgressBar = view.findViewById(R.id.circularProgressBar);
           view.findViewById(R.id.okay_btn).setOnClickListener(this);
           Set_Progress();
       }


        view.findViewById(R.id.transparent_layout).setOnClickListener(this);

        return view;
    }



    long time_gone;
    public boolean Check_IS_Boost_On(){

        long requesttime= Long.parseLong(SharedPrefrence.pref.getString(SharedPrefrence.Boost_On_Time,"0"));
        long currenttime= System.currentTimeMillis();

        time_gone=(currenttime-requesttime);

        if(requesttime==0){

            return false;
        }
        else if(time_gone<Variables.Boost_Time){

          return true;

        }
        else {

            return false;

        }


    }



    public void Set_Progress(){
        long requesttime= Long.parseLong(SharedPrefrence.pref.getString(SharedPrefrence.Boost_On_Time,"0"));
        long currenttime= System.currentTimeMillis();

        time_gone=(currenttime-requesttime);


        Start_Timer();
    }


    CountDownTimer timer;
    public void Start_Timer(){
        long time_left=Variables.Boost_Time-time_gone;
        timer=new CountDownTimer(time_left,1000) {
            @Override
            public void onTick(long l) {
                long millis = l;

                String time_string= Functions.convertSeconds((int) (millis/1000));
                TextView textView=view.findViewById(R.id.remaining_txt);
                textView.setText(time_string+" Remaining");

                float progress=  ((l*100)/Variables.Boost_Time);
                circularProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {

                Stop_timer();
            }
        };

        timer.start();
    }

    public void Stop_timer(){

        if(timer!=null)
            timer.cancel();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.boost_btn:
                Call_Api_For_BoostProfile();
                break;

            case R.id.transparent_layout:
                getActivity().onBackPressed();
                break;

            case R.id.okay_btn:
                getActivity().onBackPressed();
                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Stop_timer();
    }

    private void Call_Api_For_BoostProfile() {

        String user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);
            parameters.put("mins", "30");
            parameters.put("promoted", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.Show_loader(context,false,false);
        ApiRequest.Call_Api(context, Api_Links.boostProfile, parameters, new CallBack() {
            @Override
            public void Get_Response(String requestType, String response) {
                Functions.cancel_loader();

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    long min = System.currentTimeMillis();

                    SharedPrefrence.pref.edit().putString(SharedPrefrence.Boost_On_Time,""+min).commit();

                    getActivity().onBackPressed();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


}
