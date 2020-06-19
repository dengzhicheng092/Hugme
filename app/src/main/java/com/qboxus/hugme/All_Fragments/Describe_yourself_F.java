package com.qboxus.hugme.All_Fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Activities.Edit_profile_VP_A;
import com.qboxus.hugme.R;

import static com.qboxus.hugme.All_Activities.Edit_profile_VP_A.color_rl;

/**
 * A simple {@link Fragment} subclass.
 */
public class Describe_yourself_F extends RootFragment implements View.OnClickListener {

    View view;

    TextView text_counter;
    EditText et;

    public Describe_yourself_F() {
        // Required empty public constructor
    }


    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_describe_yourself, container, false);
        context = getContext();
        text_counter = (TextView) view.findViewById(R.id.text_counter_id);
        et = (EditText) view.findViewById(R.id.et_id);

        final String about_me = Functions.get_info(context,"about_me");

        et.setText("" + about_me);

        if(about_me.equals("")){
            // If about me is Empty
        }else{

        } // End else part


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Variables.Var_about_me = s.toString();
                int len = et.length();
                text_counter.setText(len+"/250");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) Edit_profile_VP_A.vp_rl.getLayoutParams();
        lp1.height = (int) (Variables.height/2);

        Edit_profile_VP_A.vp_rl.setLayoutParams(lp1);

        Edit_profile_VP_A.next.setVisibility(View.VISIBLE);

        Edit_profile_VP_A.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int adaptor_position_total = Edit_profile_VP_A.adp.getCount();
                int adp_current_item_position = Edit_profile_VP_A.vp.getCurrentItem()+1;

                if(adaptor_position_total == adp_current_item_position){

                    Edit_profile_VP_A.create_Json_for_API(context);
                    getActivity().finish();
                }else{
                        // Not last Fragment
                    Edit_profile_VP_A.vp.setCurrentItem(Edit_profile_VP_A.vp.getCurrentItem()+1);
                    Edit_profile_VP_A.frag_counter.setText((Edit_profile_VP_A.vp.getCurrentItem() + 1)+"/ " + Edit_profile_VP_A.adp.getCount());
                    color_rl.setBackgroundColor(getResources().getColor(R.color.zink2));

                    Edit_profile_VP_A.get_fragment_name(adp_current_item_position);

                }
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    private void METHOD_hidelinearlayout(LinearLayout ll1, LinearLayout ll2, LinearLayout ll3, LinearLayout ll4,
                                         LinearLayout ll5, LinearLayout ll6, LinearLayout ll7, LinearLayout ll8,
                                         LinearLayout ll9, LinearLayout ll10, LinearLayout ll11){

        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        ll4.setVisibility(View.GONE);
        ll5.setVisibility(View.GONE);
        ll6.setVisibility(View.GONE);
        ll7.setVisibility(View.GONE);
        ll8.setVisibility(View.GONE);
        ll9.setVisibility(View.GONE);
        ll10.setVisibility(View.GONE);
        ll11.setVisibility(View.GONE);

    }
}
