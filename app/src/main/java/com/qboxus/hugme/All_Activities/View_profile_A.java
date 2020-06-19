package com.qboxus.hugme.All_Activities;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import com.qboxus.hugme.All_Adapters.ViewPager_Adp_new;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.Code_Classes.Vertical_ViewPager;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import me.relex.circleindicator.CircleIndicator;

public class View_profile_A extends AppCompatActivity {

    Vertical_ViewPager vp;
    ViewPager_Adp_new adp;
    CircleIndicator ci;
    CoordinatorLayout rl;
    BottomSheetBehavior behavior ;
    ImageView ic_cancel;
    Context context;
    TextView name_and_age, about_me_text;

    List<String> list_profile_img;
    Button logout;
    FlowLayout item;
    ArrayList<String> about_me = new ArrayList<String>();
    ArrayList<Integer> about_me_pics = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        context = View_profile_A.this;
        rl = (CoordinatorLayout) findViewById(R.id.view_prof_rl_id);
        list_profile_img = new ArrayList<>();
        about_me_text = findViewById(R.id.about_me);

        String first_name = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "first_name"
        );

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefrence.logout_user(context);
            }
        });

        String age = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "age"
        );

        String image1 = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "image1"
        );

        String image2 = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "image2"
        );

        String image3 = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "image3"
        );

        String image4 = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "image4"
        );

        String image5 = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "image5"
        );

        String image6 = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "image6"
        );


        if(image1.equals("") || image1.equals("0")){
            // If Empty
        }else{
            // If not Empty
            list_profile_img.add(image1);

        }


        if(image2.equals("") || image2.equals("0")){
            // If Empty
        }else{
            // If not Empty
            list_profile_img.add(image2);
        }

        if(image3.equals("") || image3.equals("0")){
            // If Empty
        }else{
            // If not Empty
            list_profile_img.add(image3);
        }

        if(image4.equals("") || image4.equals("0")){
            // If Empty
        }else{
            // If not Empty
            list_profile_img.add(image4);
        }

        if(image5.equals("") || image5.equals("0")){
            // If Empty
        }else{
            // If not Empty
            list_profile_img.add(image5);
        }

        if(image6.equals("") || image6.equals("0")){
            // If Empty
        }else{
            // If not Empty
            list_profile_img.add(image6);
        }

        name_and_age = findViewById(R.id.name_and_age);
        name_and_age.setText("" + first_name + ", " + age);
        about_me_text.setText("" + SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "about_me"
        ));

        // Basic Profile_F add into ListArray





        if( SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "living"
        ).equals("")){
             // If EMpty

        }else{
            // Not Empty
            about_me_pics.add(R.drawable.ic_living);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "living"
            ));
        }

        if(SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "children"
        ).equals("")){
            // If EMpty

        }else{
            // If not Empty
            about_me_pics.add(R.drawable.ic_childrens);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "children"
            ));

        }


        if(SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "smoking"
        ).equals("")){
            /// If EMpty

        }else{
            // If nOt EMpty
            about_me_pics.add(R.drawable.ic_smoking);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "smoking"
            ));

        }


        if(SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "drinking"
        ).equals("")){
             // If EMpty

        }else{
            // If Not EMpty
            about_me_pics.add(R.drawable.ic_drinking);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "drinking"
            ));

        }

        if(SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "relationship"
        ).equals("")){
             // If EMpty

        }else{
            // If Not Empty
            about_me_pics.add(R.drawable.ic_relationship);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "relationship"
            ));
        }


        if(SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "sexuality"
        ).equals("")){
            // If Empty

        }else{
            // If Not EMpty
            about_me_pics.add(R.drawable.ic_sexuality);
            about_me.add("" + SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "sexuality"
            ));
        }


        inflate_layout();



        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (320 * scale + 0.5f);

        ic_cancel = findViewById(R.id.ic_cancel);

        ic_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        CoordinatorLayout.LayoutParams rel_btn = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels);
        rl.setLayoutParams(rel_btn);

        vp = (Vertical_ViewPager) findViewById(R.id.view_prof_vp_id);
        adp = new ViewPager_Adp_new(getApplicationContext(), list_profile_img);
        ci = (CircleIndicator) findViewById(R.id.view_prof_ci_id);

        vp.setAdapter(adp);
        ci.setViewPager(vp);

        final View bottomsheet = findViewById(R.id.bottom_sheet);

        behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

    }

    // End Method to inflate Layout
    public void inflate_layout(){
        // Method to inflate Layout

        item = findViewById(R.id.inflate_layout);//where you want to add/inflate a view as a child

        for(int i=0; i< about_me.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml
            ImageView image = child.findViewById(R.id.ic_image);
            TextView intrest_name = child.findViewById(R.id.intrest_name);
            LinearLayout main_linear_layout = child.findViewById(R.id.main_linear_layout);
            try{
                about_me_pics.get(i);
                image.setImageResource(about_me_pics.get(i));
            }catch (Exception b){

            }


            if(about_me.get(i).equals("0") || about_me.get(i).equals(" ")){
             }else{

                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(Variables.margin_left, Variables.margin_top, Variables.margin_right, Variables.margin_bottom);
                main_linear_layout.setLayoutParams(buttonLayoutParams);

                intrest_name.setText("" + about_me.get(i));
                item.addView(child);

            }

        }

        about_me.clear();

    }// End Method to inflate Layout

}
