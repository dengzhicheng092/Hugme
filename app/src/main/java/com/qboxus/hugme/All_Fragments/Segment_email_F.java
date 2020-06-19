package com.qboxus.hugme.All_Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

import static com.qboxus.hugme.All_Activities.Segments_A.spb;
import static com.qboxus.hugme.All_Activities.Segments_A.vp;

public class Segment_email_F extends RootFragment {

    View v;
    Button btn;
    LinearLayout LL, phone_LL_id;
    EditText ET, Number_verify_ET_id, password_ET_id;
    TextView email_TV,phone_TV,country_TV, email_title_id, dob_title_id;
    public static String signup_email, string_phone_num;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_email,null);

        context = getContext();
        email_TV = (TextView) v.findViewById(R.id.email_TV_id);
        phone_TV = (TextView) v.findViewById(R.id.phone_TV_id);
        country_TV = (TextView) v.findViewById(R.id.phone_number_id);
        phone_LL_id = v.findViewById(R.id.phone_LL_id);
        ET = (EditText) v.findViewById(R.id.email_ET_id);
        LL = (LinearLayout) v.findViewById(R.id.inner_LL1_id);
        Number_verify_ET_id = v.findViewById(R.id.Number_verify_ET_id);
        email_title_id= v.findViewById(R.id.email_title_id);
        password_ET_id = v.findViewById(R.id.password_ET_id);



        String email = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.share_social_info,
                "email"
        );
        ET.setText("" + email);



        email_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_TV.setVisibility(View.VISIBLE);
                email_TV.setVisibility(View.GONE);
                ET.setVisibility(View.GONE);
                LL.setVisibility(View.VISIBLE);
                phone_LL_id.setVisibility(View.VISIBLE);
                email_title_id.setText("Whats your Phone Number?");

            }
        });

        phone_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_TV.setVisibility(View.VISIBLE);
                phone_TV.setVisibility(View.GONE);
                ET.setVisibility(View.VISIBLE);
                LL.setVisibility(View.GONE);
                phone_LL_id.setVisibility(View.GONE);

                email_title_id.setText("Whats your Email?");
            }
        });


        country_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                country_picker();
            }
        });

        btn = (Button) v.findViewById(R.id.email_btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_email = ET.getText().toString();
                string_phone_num = country_TV.getText().toString() + Number_verify_ET_id.getText().toString();

                if (phone_LL_id.getVisibility() == View.VISIBLE) {

                    if(string_phone_num.isEmpty()){

                        Number_verify_ET_id.setError("Please fill this ");
                    }else{
                        vp.setCurrentItem(5);
                        spb.setCompletedSegments(5);
                    }


                } else {


                    if(signup_email.isEmpty()){
                        ET.setError("Please fill this ");
                    }else if(!Functions.isEmailValid(signup_email)){
                        ET.setError("Please enter a valid Email");
                    }else {
                        vp.setCurrentItem(5);
                        spb.setCompletedSegments(6);
                    }
                }

            }
        });

        return v;
    }

    private void country_picker() {

        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                country_TV.setText(dialCode);
                picker.dismiss();
            }
        });

        picker.setStyle(R.style.countrypicker_style,R.style.countrypicker_style);
        picker.show(getChildFragmentManager(), "Select Country");

    }
}
