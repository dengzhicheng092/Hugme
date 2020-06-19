package com.qboxus.hugme.Bottom_Sheet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import com.qboxus.hugme.All_Activities.Edit_profile_A;
import com.qboxus.hugme.All_Activities.Share_Profile_A;
import com.qboxus.hugme.BuildConfig;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Adapters.Images_Adp;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Images;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

public class View_Profile_Bottom_My extends BottomSheetDialogFragment {
    View view;
    TextView distance, about_me_tv_id, simple_about;
    ArrayList<Integer> about_me_pics = new ArrayList<Integer>();
   List<Get_Set_Images> list_profile_img;
    ArrayList<String> about_me = new ArrayList<String>();
    public View_Profile_Bottom_My() {
    }
    String user_id;
    RecyclerView RV_images_list;
    Images_Adp adp_img;
    Context context;
    ImageView edit_profile;
    ImageView dialogue_cancel, move_left, move_right;
    SimpleDraweeView iv;
    FlowLayout item;

    FrameLayout fl;
    BottomSheetBehavior behavior;
    Get_Set_Nearby get_nearby;
    TextView report_user;
    FrameLayout bottomSheet;

    String swipe_pos_string;
    String view_type;
    TextView share_profile, user_name;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_view_my_profile, container, false);
        //  view.findViewById(R.id.fl_id).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        user_id = getArguments().getString("user_id");
        swipe_pos_string = getArguments().getString("current_position");
        view_type = getArguments().getString("view_type");
        context = getContext();
        distance = view.findViewById(R.id.distance);
        about_me_tv_id = view.findViewById(R.id.about_me_tv_id);
        simple_about = view.findViewById(R.id.simple_about);
        dialogue_cancel = (ImageView) view.findViewById(R.id.left_overlay);
        edit_profile = (ImageView) view.findViewById(R.id.right_overlay);
        list_profile_img = new ArrayList<>();
        RV_images_list = view.findViewById(R.id.RV_images_list);
        iv = view.findViewById(R.id.profile_iv_id);
        share_profile = view.findViewById(R.id.share_profile);
        user_name = view.findViewById(R.id.user_name);


        fl = (FrameLayout) view.findViewById(R.id.fl_id);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Edit Profile_F Screen
                startActivity(new Intent(getActivity(), Edit_profile_A.class));

            }
        });


        dialogue_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        report_user = view.findViewById(R.id.report_user);

        move_left = view.findViewById(R.id.move_left);
        move_right = view.findViewById(R.id.move_right);

        get_nearby = (Get_Set_Nearby) getArguments().getSerializable("user_near_by");

        report_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.report_user(context,"" + get_nearby.getFb_id());
            }
        });


        share_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  user_image = Functions.get_info(context,"image1");

                String user_name = Functions.get_info(context,"first_name")
                        +" "+Functions.get_info(context,"last_name");

                Intent intent=new Intent(getActivity(), Share_Profile_A.class);
                intent.putExtra("name",user_name);
                intent.putExtra("image",user_image);

                startActivity(intent);



            }
        });

        user_name.setText("" + get_nearby.getFirst_name() + ", " + get_nearby.getBirthday());

        NestedScrollView scroller = (NestedScrollView) view.findViewById(R.id.nestedscrollview_id);

        if (scroller != null) {

            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("", "Scroll DOWN");
                        user_name.setVisibility(View.GONE);


                    }
                    if (scrollY < oldScrollY) {
                        Log.i("", "Scroll UP");
                    }


                    if (scrollY == 0) {
                        user_name.setVisibility(View.VISIBLE);
                        Log.i("", "TOP SCROLL");
                    }

                    if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                        Log.i("", "BOTTOM SCROLL");
                    }
                }
            });
        }
        get_nearby.getAbout_me();
        about_me_tv_id.setText("" + get_nearby.getAbout_me());
        distance.setText("" + get_nearby.getDistance());

        if(get_nearby.getAbout_me().equals("")){
            simple_about.setVisibility(View.GONE);
            about_me_tv_id.setVisibility(View.GONE);
        }else{
            simple_about.setVisibility(View.VISIBLE);
            about_me_tv_id.setVisibility(View.VISIBLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            about_me_tv_id.setText(Html.fromHtml( get_nearby.getAbout_me(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            about_me_tv_id.setText(Html.fromHtml( get_nearby.getAbout_me()));
        }

        list_profile_img = new ArrayList<>();

        if(!get_nearby.getImage2().equals("") && !get_nearby.getImage2().equals("0")){
            Get_Set_Images add_2 = new Get_Set_Images("" + get_nearby.getImage2());
            list_profile_img.add(add_2);
        }

        if(!get_nearby.getImage3().equals("") && !get_nearby.getImage3().equals("0")){
            Get_Set_Images add_3 = new Get_Set_Images("" + get_nearby.getImage3());
            list_profile_img.add(add_3);
        }

        if(!get_nearby.getImage4().equals("") && !get_nearby.getImage4().equals("0")){
            Get_Set_Images add_4 = new Get_Set_Images("" + get_nearby.getImage4());
            list_profile_img.add(add_4);
        }

        if(!get_nearby.getImage5().equals("") && !get_nearby.getImage5().equals("0")){
            Get_Set_Images add_5 = new Get_Set_Images("" + get_nearby.getImage5());
            list_profile_img.add(add_5);
        }

        if(!get_nearby.getImage6().equals("") && !get_nearby.getImage6().equals("0")){
            Get_Set_Images add_6 = new Get_Set_Images("" + get_nearby.getImage6());
            list_profile_img.add(add_6);
        }


        adp_img = new Images_Adp(context, list_profile_img, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {

            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

        RV_images_list.setLayoutManager(new GridLayoutManager(getContext(), 1));
        RV_images_list.setHasFixedSize(false);
        RV_images_list.setAdapter(adp_img);



        if(get_nearby.getImage2().equals("") && get_nearby.getImage3().equals("") &&
                get_nearby.getImage4().equals("") &&
                get_nearby.getImage5().equals("") &&
                get_nearby.getImage6().equals("") ){
            // If all images empty
            RV_images_list.setVisibility(View.GONE);
        }else{
            RV_images_list.setVisibility(View.VISIBLE);
        }


        try{
            Uri uri = Uri.parse(get_nearby.getImage1());
            iv.setImageURI(uri);  // Attachment
            iv.getHierarchy().setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.profile_image_placeholder));

        }catch (Exception v){

        }

        if(!SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "living"
        ).equals("")){
            about_me_pics.add(R.drawable.ic_living);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "living"
            ));
        }

        if(!SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "children"
        ).equals("")){
            about_me_pics.add(R.drawable.ic_childrens);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "children"
            ));


        }


        if(!SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "smoking"
        ).equals("")){
            about_me_pics.add(R.drawable.ic_smoking);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "smoking"
            ));

        }


        if(!SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "drinking"
        ).equals("")){
            about_me_pics.add(R.drawable.ic_drinking);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "drinking"
            ));


        }

        if(!SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "relationship"
        ).equals("")){
            about_me_pics.add(R.drawable.ic_relationship);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "relationship"
            ));

        }


        if(!SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "sexuality"
        ).equals("")){
            about_me_pics.add(R.drawable.ic_sexuality);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "sexuality"
            ));

        }

        fl = (FrameLayout) view.findViewById(R.id.fl_id);


        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                bottomSheet = (FrameLayout)
                        dialog.findViewById(R.id.design_bottom_sheet);
                dialog
                        .getWindow()
                        .findViewById(R.id.design_bottom_sheet)
                        .setBackgroundResource(android.R.color.transparent);

                behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int i) {
                        if (i == BottomSheetBehavior.STATE_DRAGGING){
                            // fl.setBackgroundColor(getResources().getColor(R.color.full_transparent));

                        }

                        if (i == BottomSheetBehavior.STATE_EXPANDED){

                        }

                        if (i == BottomSheetBehavior.STATE_COLLAPSED){
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            behavior.setHideable(true);
                            dismiss();
                       }

                        if (i == BottomSheetBehavior.STATE_HIDDEN){

                        }

                    }

                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                });



                        final GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
                        RV_images_list.setLayoutManager(layoutManager);


                        // RV_images_list.setHasFixedSize(false);
                        RV_images_list.setAdapter(adp_img);
                inflate_layout();

                        try {
                            Uri uri = Uri.parse(get_nearby.getImage1());
                            iv.setImageURI(uri);  // Attachment
                            iv.getHierarchy().setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.image_placeholder));

                        } catch (Exception v9) {

                        }

                    }
                });


                return view;
    }



    public void inflate_layout(){

        item = view.findViewById(R.id.inflate_layout);

        for(int i=0; i< about_me.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml
            ImageView image = child.findViewById(R.id.ic_image);
            TextView intrest_name = child.findViewById(R.id.intrest_name);
            LinearLayout main_linear_layout = child.findViewById(R.id.main_linear_layout);

            if(about_me.get(i).equals("0") || about_me.get(i).equals(" ")){
            }else {
                intrest_name.setText("" + about_me.get(i));
                image.setImageResource(about_me_pics.get(i));

                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(Variables.margin_left, Variables.margin_top, Variables.margin_right, Variables.margin_bottom);

                main_linear_layout.setLayoutParams(buttonLayoutParams);

                item.addView(child);

            }
        }

        about_me.clear();
    }

}
