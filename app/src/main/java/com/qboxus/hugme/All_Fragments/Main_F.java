package com.qboxus.hugme.All_Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.appyvet.materialrangebar.RangeBar;

import org.json.JSONObject;

import com.qboxus.hugme.All_Adapters.VP_Adapter;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Chat_pkg.Inbox_F;
import com.qboxus.hugme.All_Activities.Edit_profile_A;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.R;
import com.qboxus.hugme.All_Activities.Setting_A;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Utils.Custom_ViewPager;

//import static com.com.hugme.All_Fragments.World_wide_F.get_Near_by_Users;

public class Main_F extends RootFragment {
    //RangeBar rangeBar;
    View v;
    public static Toolbar tb;
    public static TabLayout tl;
    public static RelativeLayout tl_rl;
    public static Custom_ViewPager vp;
    VP_Adapter adp;
    ImageView IV1,IV2,IV3;
    String search_gender, search_filter_by, search_age_min, search_age_max, search_distance;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main, null);
        context = getContext();
        tb = (Toolbar) v.findViewById(R.id.toolbar_id);
        tl = (TabLayout) v.findViewById(R.id.tablayout_id);
        vp = v.findViewById(R.id.main_f_vp_id);
        tl_rl = (RelativeLayout) v.findViewById(R.id.tablayout_rl_id);

        IV1 = (ImageView) v.findViewById(R.id.control_IV_id);
        IV2 = (ImageView) v.findViewById(R.id.main_f_setting_id);
        IV3 = (ImageView) v.findViewById(R.id.main_f_edit_id);


        tb.setVisibility(View.GONE);
        adp = new VP_Adapter(getChildFragmentManager());
        //adapter.add_fragment(new World_wide_F(), "");
        adp.add_fragment(new World_wide_native_ads_F(), "");
        adp.add_fragment(new Live_Users_F(), "");
        adp.add_fragment(new Swipe_F(), "");
        adp.add_fragment(new Inbox_F(), "");
        adp.add_fragment(new Profile_F(), "");

        vp.setAdapter(adp);
        vp.setOffscreenPageLimit(4);
        tl.setupWithViewPager(vp);
        vp.setPagingEnabled(false);

        final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.CTI_IV_id);
        imageView1.setImageResource(R.drawable.ic_world_gray);
        tl.getTabAt(0).setCustomView(view1);

        final View view5 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView5 = (ImageView) view5.findViewById(R.id.CTI_IV_id);
        imageView5.setImageResource(R.drawable.ic_live_black);
        tl.getTabAt(1).setCustomView(view5);


        final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView22 = (ImageView) view2.findViewById(R.id.CTI_IV_id);
        imageView22.setImageResource(R.drawable.ic_card_color);
        tl.getTabAt(2).setCustomView(view2);

        final View view3 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView3 = (ImageView) view3.findViewById(R.id.CTI_IV_id);
        imageView3.setImageResource(R.drawable.ic_chat_gray);
        tl.getTabAt(3).setCustomView(view3);

        final View view4 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_icons, null);
        ImageView imageView4 = (ImageView) view4.findViewById(R.id.CTI_IV_id);
        imageView4.setImageResource(R.drawable.ic_profile_gray);
        tl.getTabAt(4).setCustomView(view4);


        tl.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.CTI_IV_id);
                switch (tab.getPosition()){
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_world_color));
                        Main_F.tb.setVisibility(View.GONE);
                        IV1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {

                                final Dialog dialog = new Dialog(getContext());

                                final View dialogview =  LayoutInflater.from(getContext()).inflate(R.layout.item_filter_dialog_layout,null);

                                final TextView filter_text = dialogview.findViewById(R.id.filter_text);
                                String sourceString = "<b>Filter</b> ";
                                filter_text.setText(Html.fromHtml(sourceString));

                                final TextView tv = (TextView) dialogview.findViewById(R.id.guys_id);
                                final TextView tv1 = (TextView) dialogview.findViewById(R.id.girls_id);
                                final TextView tv2 = (TextView) dialogview.findViewById(R.id.both_id);

                                final TextView tv4 = (TextView) dialogview.findViewById(R.id.all_id);
                                final TextView tv5 = (TextView) dialogview.findViewById(R.id.online_id);
                                final TextView tv6 = (TextView) dialogview.findViewById(R.id.new_id);

                                final ImageView iv1 = (ImageView) dialogview.findViewById(R.id.dialog_cross_Id);
                                final ImageView iv2 = (ImageView) dialogview.findViewById(R.id.dialog_tick_id);
                                RangeBar rangeBar = (RangeBar) dialogview.findViewById(R.id.ww_RB_id);

                                final TextView text_age = dialogview.findViewById(R.id.text_age);
                                final TextView distance = dialogview.findViewById(R.id.distance);
                                SeekBar simpleSeekBar = dialogview.findViewById(R.id.simpleSeekBar);
                                simpleSeekBar.setProgress(100);
                                distance.setText("" + simpleSeekBar.getProgress() );
                                dialog.setContentView(dialogview);



                                simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                        int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();

                                        distance.setText("" + progress);
                                        search_distance = "" + progress + "";

                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });


                                try{
                                    rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                                        @Override
                                        public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                                            search_age_min = leftPinValue;
                                            search_age_max = rightPinValue;
                                            text_age.setText("" + leftPinValue + "-" + "" + rightPinValue);
                                           //Functions.toast_msg(context,"Left " + leftPinValue + " Right " + rightPinValue);
                                        }
                                    });

                                }catch (Exception b){

                                    Functions.toast_msg(context,"" + b.toString());
                                }

                                tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv1.setBackgroundResource(R.drawable.d_gray_border);
                                        tv2.setBackgroundResource(R.drawable.d_gray_border);
                                        add_gender(tv);  // TODO: get Value from gender

                                    }
                                });


                                tv1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv1.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv.setBackgroundResource(R.drawable.d_gray_border);
                                        tv2.setBackgroundResource(R.drawable.d_gray_border);
                                        add_gender(tv1);  // TODO: get Value from gender
                                    }
                                });


                                tv2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv2.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv.setBackgroundResource(R.drawable.d_gray_border);
                                        tv1.setBackgroundResource(R.drawable.d_gray_border);
                                        add_gender(tv2);  // TODO: get Value from gender
                                    }
                                });


                                tv4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv4.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv5.setBackgroundResource(R.drawable.d_gray_border);
                                        tv6.setBackgroundResource(R.drawable.d_gray_border);
                                        add_all_filter(tv4);  // TODO: get Value from All Filter
                                    }
                                });


                                tv5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv5.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv4.setBackgroundResource(R.drawable.d_gray_border);
                                        tv6.setBackgroundResource(R.drawable.d_gray_border);
                                        add_all_filter(tv5);  // TODO: get Value from All Filter
                                    }
                                });


                                tv6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv6.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv5.setBackgroundResource(R.drawable.d_gray_border);
                                        tv4.setBackgroundResource(R.drawable.d_gray_border);
                                        add_all_filter(tv6);  // TODO: get Value from All Filter
                                    }
                                });


                                iv1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        //Functions.toast_msg(getContext(),"" + search_filter_by + " " + search_gender);
                                    }
                                });


                                iv2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        handle_search_params();
                                        dialog.dismiss();
                                       // Functions.toast_msg(getContext(),"" + search_filter_by + " " + search_gender);
                                    }
                                });


                                dialog.show();

                            }
                        });

                        break;

                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_live_color));
                        IV1.setVisibility(View.GONE);
                        Main_F.tb.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_color));
                        Main_F.tb.setVisibility(View.GONE);
                        IV1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {

                                final Dialog dialog = new Dialog(getContext());

                                final View dialogview =  LayoutInflater.from(getContext()).inflate(R.layout.item_filter_dialog_layout,null);

                                final TextView filter_text = dialogview.findViewById(R.id.filter_text);
                                String sourceString = "<b>Filter</b> ";
                                filter_text.setText(Html.fromHtml(sourceString));

                                final TextView tv = (TextView) dialogview.findViewById(R.id.guys_id);
                                final TextView tv1 = (TextView) dialogview.findViewById(R.id.girls_id);
                                final TextView tv2 = (TextView) dialogview.findViewById(R.id.both_id);

                                final TextView tv4 = (TextView) dialogview.findViewById(R.id.all_id);
                                final TextView tv5 = (TextView) dialogview.findViewById(R.id.online_id);
                                final TextView tv6 = (TextView) dialogview.findViewById(R.id.new_id);

                                final ImageView iv1 = (ImageView) dialogview.findViewById(R.id.dialog_cross_Id);
                                final ImageView iv2 = (ImageView) dialogview.findViewById(R.id.dialog_tick_id);
                                RangeBar rangeBar = (RangeBar) dialogview.findViewById(R.id.ww_RB_id);

                                final TextView text_age = dialogview.findViewById(R.id.text_age);
                                final TextView distance = dialogview.findViewById(R.id.distance);
                                SeekBar simpleSeekBar = dialogview.findViewById(R.id.simpleSeekBar);
                                simpleSeekBar.setProgress(100);
                                distance.setText("" + simpleSeekBar.getProgress());
                                dialog.setContentView(dialogview);

                                rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                                    @Override
                                    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                                    }
                                });






                                simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                        int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();

                                        distance.setText("" + progress);
                                        search_distance = "" + progress + "";

                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });


                                try{
                                    rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                                        @Override
                                        public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                                            search_age_min = leftPinValue;
                                            search_age_max = rightPinValue;
                                            text_age.setText("" + leftPinValue + "-" + "" + rightPinValue);
                                            //Functions.toast_msg(context,"Left " + leftPinValue + " Right " + rightPinValue);
                                        }
                                    });

                                }catch (Exception b){

                                    Functions.toast_msg(context,"" + b.toString());
                                }


                                tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv1.setBackgroundResource(R.drawable.d_gray_border);
                                        tv2.setBackgroundResource(R.drawable.d_gray_border);
                                        add_gender(tv);  // TODO: get Value from gender

                                    }
                                });


                                tv1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv1.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv.setBackgroundResource(R.drawable.d_gray_border);
                                        tv2.setBackgroundResource(R.drawable.d_gray_border);
                                        add_gender(tv1);  // TODO: get Value from gender
                                    }
                                });


                                tv2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv2.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv.setBackgroundResource(R.drawable.d_gray_border);
                                        tv1.setBackgroundResource(R.drawable.d_gray_border);
                                        add_gender(tv2);  // TODO: get Value from gender
                                    }
                                });


                                tv4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv4.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv5.setBackgroundResource(R.drawable.d_gray_border);
                                        tv6.setBackgroundResource(R.drawable.d_gray_border);
                                        add_all_filter(tv4);  // TODO: get Value from All Filter
                                    }
                                });


                                tv5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv5.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv4.setBackgroundResource(R.drawable.d_gray_border);
                                        tv6.setBackgroundResource(R.drawable.d_gray_border);
                                        add_all_filter(tv5);  // TODO: get Value from All Filter
                                    }
                                });


                                tv6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tv6.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                                        tv5.setBackgroundResource(R.drawable.d_gray_border);
                                        tv4.setBackgroundResource(R.drawable.d_gray_border);
                                        add_all_filter(tv6);  // TODO: get Value from All Filter
                                    }
                                });


                                iv1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });


                                iv2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        handle_search_params();
                                        dialog.dismiss();
                                   }
                                });

                                dialog.show();

                            }
                        });

                        break;

                    case 3:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_color));
                        IV1.setVisibility(View.GONE);
                        Main_F.tb.setVisibility(View.VISIBLE);
                        break;

                    case 4:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_color));
                        IV1.setVisibility(View.GONE);
                        IV2.setVisibility(View.VISIBLE);
                        IV3.setVisibility(View.VISIBLE);
                        Main_F.tb.setVisibility(View.VISIBLE);
                        break;

                }
                tab.setCustomView(view);

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.CTI_IV_id);

                switch (tab.getPosition()){
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_world_gray));
                        break;

                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_live_black));
                        break;
                    case 2:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_card_gray));
                        break;

                    case 3:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_gray));
                        IV1.setVisibility(View.VISIBLE);
                        break;

                    case 4:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_gray));
                        IV1.setVisibility(View.VISIBLE);
                        IV2.setVisibility(View.GONE);
                        IV3.setVisibility(View.GONE);
                        break;

                }

                tab.setCustomView(view);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });






        tl.getTabAt(2).select();





        IV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Setting_A.class));
            }
        });




        IV3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Edit_profile_A.class));
            }
        });



        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().finish();
    }


    // Method to get text from gender Searching
    public void add_gender(TextView textView){
        search_gender = textView.getText().toString();
    }

    // Method to get text from gender Searching
    public void add_all_filter(TextView textView){
        search_filter_by = textView.getText().toString();
    }


    public void handle_search_params(){
        // Method to handle search Params.
        vp.getCurrentItem();


        try{
            JSONObject search_params = new JSONObject();
            search_params.put("gender","" + search_gender);
            search_params.put("filter_by","" + search_filter_by);
            search_params.put("age_min","" + search_age_min);
            search_params.put("age_max","" + search_age_max);
            search_params.put("search_distance","" + search_distance);

            // Save into Shared Pref
            SharedPrefrence.save_string(context,"" + search_params.toString(),
                    "" + SharedPrefrence.share_search_params);

        }catch (Exception b){

        }



    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            Main_F.tb.setVisibility(View.GONE);

        }
    }


}
