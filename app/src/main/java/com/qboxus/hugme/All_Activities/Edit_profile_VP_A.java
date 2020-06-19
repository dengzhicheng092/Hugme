package com.qboxus.hugme.All_Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.qboxus.hugme.All_Fragments.Describe_yourself_F;
import com.qboxus.hugme.All_Fragments.Drink_F;
import com.qboxus.hugme.All_Fragments.Gender_F;
import com.qboxus.hugme.All_Fragments.Kids_F;
import com.qboxus.hugme.All_Fragments.Living_F;
import com.qboxus.hugme.All_Fragments.Relationship_F;
import com.qboxus.hugme.All_Fragments.Smoke_F;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Adapters.Segment_Adapter;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;

import static com.qboxus.hugme.Code_Classes.Variables.user_gender;
import static com.qboxus.hugme.All_Activities.Edit_profile_A.update_profile;

public class Edit_profile_VP_A extends AppCompatActivity implements View.OnClickListener {

    public static ViewPager vp;
    public static Segment_Adapter adp;

    public static RelativeLayout color_rl,vp_rl;

    public static LinearLayout back_ll,desc_ll,happy_ll,gender_ll,relation_ll,bodytype_ll,living_ll,kids_ll,smoke_ll,drink_ll,profqs_ll,missing_ll, update_profile_LL;

    public static ImageView next,previous;
    public static TextView frag_counter;


    Context context;
    List<String> list_user_img_from_API = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        }
        setContentView(R.layout.activity_edit_profile_vp);

        context = Edit_profile_VP_A.this;


        vp = (ViewPager) findViewById(R.id.vp_id);

        color_rl = (RelativeLayout) findViewById(R.id.color_rl_id);
        vp_rl = (RelativeLayout) findViewById(R.id.vp_rl_id);

        back_ll = (LinearLayout) findViewById(R.id.back_ll_id);
        desc_ll = (LinearLayout) findViewById(R.id.desc_ll_id);
        happy_ll = (LinearLayout) findViewById(R.id.happy_ll_id);
        gender_ll = (LinearLayout) findViewById(R.id.gender_ll_id);
        relation_ll = (LinearLayout) findViewById(R.id.relationship_ll_id);
        bodytype_ll = (LinearLayout) findViewById(R.id.bodytype_ll_id);
        living_ll = (LinearLayout) findViewById(R.id.house_ll_id);
        kids_ll = (LinearLayout) findViewById(R.id.kids_ll_id);
        smoke_ll = (LinearLayout) findViewById(R.id.smoking_ll_id);
        drink_ll = (LinearLayout) findViewById(R.id.drink_ll_id);
        profqs_ll = (LinearLayout) findViewById(R.id.profqs_ll_id);
        missing_ll = (LinearLayout) findViewById(R.id.missing_ll_id);

        update_profile_LL = (LinearLayout) findViewById(R.id.update_profile_LL);

        next = (ImageView) findViewById(R.id.next_id);
        previous = (ImageView) findViewById(R.id.previous_id);

        frag_counter = (TextView) findViewById(R.id.fragment_counter_id);



        adp = new Segment_Adapter(getSupportFragmentManager());

        if(check_values("about_me")){
            // If true , i.e present
         //   METHOD_hidelinearlayout(happy_ll,desc_ll,relation_ll,kids_ll,living_ll,drink_ll,smoke_ll,profqs_ll,gender_ll,bodytype_ll,missing_ll);



        }else{
            // Not present
   //         METHOD_hidelinearlayout(desc_ll,drink_ll,smoke_ll,kids_ll,living_ll,bodytype_ll,relation_ll,gender_ll,happy_ll,desc_ll,profqs_ll);

            adp.add_fragment(new Describe_yourself_F(),"");
        }
     //   adapter.add_fragment(new What_makes_you_happy_F(),"");

        if(check_values("gender")){
            // If true , i.e present
            //   METHOD_hidelinearlayout(happy_ll,desc_ll,relation_ll,kids_ll,living_ll,drink_ll,smoke_ll,profqs_ll,gender_ll,bodytype_ll,missing_ll);

        }else{
            // Not present
            //         METHOD_hidelinearlayout(desc_ll,drink_ll,smoke_ll,kids_ll,living_ll,bodytype_ll,relation_ll,gender_ll,happy_ll,desc_ll,profqs_ll);

            adp.add_fragment(new Gender_F(),"");
        }


        if (check_values("sexuality")) {
            // if true , i.e present

        }else{
//            adapter.add_fragment(new (),"");
        }



        if(check_values("relationship")){
            // If true , i.e present

        }else{
            // Not present
            adp.add_fragment(new Relationship_F(),"");
        }
       // adapter.add_fragment(new Bodytype_F(),"");
        // If value is not null then no need to add this into Adapter of Viwe pager
        if(check_values("living")){
            // If true , i.e present

        }else{
            // Not present
            adp.add_fragment(new Living_F(),"");
        }

        // If value is not null then no need to add this into Adapter of Viwe pager
        if(check_values("children")){
            // If true , i.e present

        }else{
            // Not present
            adp.add_fragment(new Kids_F(),"");
        }

        // If value is not null then no need to add this into Adapter of Viwe pager
        if(check_values("smoking")){
            // If true , i.e present

        }else{
            // Not present
            adp.add_fragment(new Smoke_F(),"");
        }

        // If value is not null then no need to add this into Adapter of Viwe pager
        if(check_values("drinking")){
            // If true , i.e present

        }else{
            // Not present
            adp.add_fragment(new Drink_F(),"");
        }



        if(check_values("drinking") && check_values("smoking") && check_values("children") && check_values("living")
                && check_values("relationship") && check_values("gender") && check_values("about_me")
        ){
            // If all things are present
            images_check();
        }


        // Update Record;


        // todo:
         // Get User Info
        String user_id = Functions.get_info(context,"fb_id");
        getuser_info(user_id);




        //adapter.add_fragment(new ProfileQs_F(),"");
        //adapter.add_fragment(new Missing_Profile(),"");

        vp.setAdapter(adp);
        frag_counter.setText((vp.getCurrentItem() + 1)+"/" + adp.getCount());

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) color_rl.getLayoutParams();
        lp.height = (int) (Variables.height/2);

        color_rl.setLayoutParams(lp);


        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) vp_rl.getLayoutParams();
        lp1.height = (int) (Variables.height/2);

        vp_rl.setLayoutParams(lp1);

        vp.setCurrentItem(0);
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vp.setCurrentItem(vp.getCurrentItem());
                return true;
            }
        });

        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        back_ll.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back_ll_id:
                finish();
                break;


            case R.id.next_id:

                int position = vp.getCurrentItem() + 1;

                vp.setCurrentItem(vp.getCurrentItem() + 1);

                frag_counter.setText((vp.getCurrentItem() + 1)+"/" + adp.getCount());

                if(position == adp.getCount()){
                    // If position equal to Size
                    next.setVisibility(View.GONE);
                }

                get_fragment_name(vp.getCurrentItem());
              //  Functions.toast_msg(context,"About " + Variables.Var_about_me);
                switch (vp.getCurrentItem()){
                    case 0:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.coloraccent));
                        get_fragment_name(vp.getCurrentItem());
                        next.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.red));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.lightblue));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 3:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.pink));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 4:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.orange));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 5:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.green));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 6:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.zink));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 7:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.light_purple));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 8:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.coloraccent));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 9:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.zink2));


                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 10:
                        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) vp_rl.getLayoutParams();
                        lp1.height = (int) (Variables.height/1.1);

                        vp_rl.setLayoutParams(lp1);

                        next.setVisibility(View.GONE);
                        METHOD_hidelinearlayout(missing_ll,drink_ll,smoke_ll,kids_ll,living_ll,bodytype_ll,relation_ll,gender_ll,happy_ll,desc_ll,profqs_ll);
                        break;
                }
                break;

            case R.id.previous_id:
                vp.setCurrentItem(vp.getCurrentItem() - 1);

                switch (vp.getCurrentItem()){
                    case 0:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.coloraccent));


                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.red));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.lightblue));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 3:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.pink));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 4:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.orange));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 5:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.green));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 6:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.zink));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 7:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.light_purple));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 8:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.coloraccent));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 9:
                        color_rl.setBackgroundColor(getResources().getColor(R.color.zink2));

                        get_fragment_name(vp.getCurrentItem());

                        next.setVisibility(View.VISIBLE);
                        break;

                    case 10:

                        get_fragment_name(vp.getCurrentItem());
                        next.setVisibility(View.VISIBLE);
                        break;
                }
                frag_counter.setText((vp.getCurrentItem() + 1)+"/" + adp.getCount());
                break;
        }
    }











    public static void METHOD_hidelinearlayout(LinearLayout ll1,LinearLayout ll2,LinearLayout ll3,LinearLayout ll4,
                                         LinearLayout ll5,LinearLayout ll6,LinearLayout ll7,LinearLayout ll8,
                                         LinearLayout ll9,LinearLayout ll10,LinearLayout ll11){

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

    // Method if value present or not
    public boolean check_values (String val_want){

        boolean is_present;
        String children = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.u_login_detail,
                "" + val_want
        );
       // Functions.toast_msg(context,"" + children);
        if(children.equals("") || children.equals("0")){
            // If val is not present

            is_present = false;
        }else{
            // if val present

            is_present = true;
        }


        return is_present;
    }// End method to check values in Profiles


    // Method to check if images are empty or not
    public void images_check(){


        if(check_values("image2") && check_values("image3") && check_values("image4") && check_values("image5") && check_values("image6")){

        }else if(check_values("living") || check_values("living")){
            // If not Present
           // Functions.toast_msg(context,"Please Add your imagaes.");
            startActivity(new Intent(context, Edit_profile_A.class));
            finish();

        }



    }

    public static String get_fragment_name(int position){
        int total_pages = adp.getCount();

        String full_fragment_name = adp.getItem(position).toString();
        int index = full_fragment_name.indexOf("{");

        String fragment_name = full_fragment_name.substring(0,index);
        position = position + 1;

        if(fragment_name.equals("" + Variables.frag_about)){
            METHOD_hidelinearlayout(desc_ll,happy_ll,relation_ll,kids_ll,living_ll,drink_ll,smoke_ll,profqs_ll,gender_ll,bodytype_ll,missing_ll);
        }else if(fragment_name.equals("" + Variables.frag_Drink)){
           METHOD_hidelinearlayout(drink_ll,desc_ll,relation_ll,kids_ll,living_ll,happy_ll,smoke_ll,profqs_ll,gender_ll,bodytype_ll,missing_ll);

        }else if(fragment_name.equals("" + Variables.frag_kids)){
            METHOD_hidelinearlayout(kids_ll,desc_ll,relation_ll,drink_ll,living_ll,happy_ll,smoke_ll,profqs_ll,gender_ll,bodytype_ll,missing_ll);

        }else if(fragment_name.equals("" + Variables.frag_Living)){
            METHOD_hidelinearlayout(living_ll,desc_ll,relation_ll,drink_ll,kids_ll,happy_ll,smoke_ll,profqs_ll,gender_ll,bodytype_ll,missing_ll);

        }else if(fragment_name.equals("" + Variables.frag_Relation)){

            METHOD_hidelinearlayout(relation_ll,desc_ll,living_ll,drink_ll,kids_ll,happy_ll,smoke_ll,profqs_ll,gender_ll,bodytype_ll,missing_ll);

        }else if(fragment_name.equals("" + Variables.frag_Smoke)){

            METHOD_hidelinearlayout(smoke_ll,desc_ll,living_ll,drink_ll,kids_ll,happy_ll,relation_ll,profqs_ll,gender_ll,bodytype_ll,missing_ll);

        }else if(fragment_name.equals("" + Variables.frag_gender)){
            METHOD_hidelinearlayout(gender_ll,desc_ll,living_ll,drink_ll,kids_ll,happy_ll,relation_ll,profqs_ll,smoke_ll,bodytype_ll,missing_ll);

        }

        if(total_pages == position){

           next.setVisibility(View.GONE);

        }


        return fragment_name;
    }

    public static void create_Json_for_API (Context context){
        final String user_id = Functions.get_info(context,"fb_id");

        Variables.Var_sexuality = Functions.get_info(context,"sexuality");
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", "" + user_id);
            parameters.put("about_me", "" + Variables.Var_about_me.replaceAll("[-+.^:,']",""));
            parameters.put("job_title", "job_title");
            parameters.put("company", "company");
            parameters.put("school", "school");
            parameters.put("living", "" + Variables.Var_living.replaceAll("[-+.^:,']",""));
            parameters.put("children", "" + Variables.Var_children.replaceAll("[-+.^:,']",""));
            parameters.put("smoking", "" + Variables.Var_smoking.replaceAll("[-+.^:,']",""));
            parameters.put("drinking", "" + Variables.Var_drinking.replaceAll("[-+.^:,']",""));
            parameters.put("relationship", "" + Variables.Var_relationship.replaceAll("[-+.^:,']",""));
            parameters.put("sexuality", "" + Variables.Var_sexuality.replaceAll("[-+.^:,']",""));

            parameters.put("image1", "" +  Functions.get_info(context,"image1"));
            parameters.put("image2", "" +  Functions.get_info(context,"image2"));
            parameters.put("image3", "" +  Functions.get_info(context,"image3"));
            parameters.put("image4", "" +  Functions.get_info(context,"image4"));
            parameters.put("image5", "" +  Functions.get_info(context,"image5"));
            parameters.put("image6", "" +  Functions.get_info(context,"image6"));

            parameters.put("gender", "" + user_gender.replaceAll("[-+.^:,']",""));
            parameters.put("birthday", "" + Functions.get_info(context,"birthday"));

         //   Functions.toast_msg(context,"Gender_F " + user_gender);

            update_profile(context,parameters);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }



    public void getuser_info (final String user_id){

        Functions.Show_loader(context,false,false);
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
                    @Override
                    public void Get_Response(String requestType, String resp) {
                        try{

                            Functions.cancel_loader();


                            JSONObject response=new JSONObject(resp);

                            //  Functions.toast_msg(context,"From Edi " + response);

                            if(response.getString("code").equals("200")) {

                                // Add User Profile_F info to Shared Prefrence

                                JSONArray msg_obj = response.getJSONArray("msg");
                                JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                /// Functions.toast_msg(context,"Whle " + user_info_obj.toString());
                                // Save
                                SharedPrefrence.save_string(context,"" + user_info_obj.toString(),
                                        "" + SharedPrefrence.u_login_detail);

                                // Set Texts

                                Variables.Var_sexuality = user_info_obj.getString("about_me");
                                Variables.Var_living = user_info_obj.getString("living");
                                Variables.Var_children = user_info_obj.getString("children");
                                Variables.Var_smoking = user_info_obj.getString("smoking");
                                Variables.Var_drinking = user_info_obj.getString("drinking");
                                Variables.Var_relationship = user_info_obj.getString("relationship");
                                Variables.Var_sexuality = user_info_obj.getString("sexuality");
                                Variables.Var_about_me = user_info_obj.getString("about_me");
                                user_gender = user_info_obj.getString("gender");
                                // Variables.Var_sexuality =

                                /*  todo: Get User Images */


                                if(user_info_obj.getString("image1").equals("") || user_info_obj.getString("image1").equals("0")){
                                    // If empty

                                }else{
                                    // Not Empty
                                    list_user_img_from_API.add(user_info_obj.getString("image1"));
                                }

                                if(user_info_obj.getString("image2").equals("") || user_info_obj.getString("image2").equals("0")){
                                    // If empty

                                }else{
                                    // Not Empty
                                    list_user_img_from_API.add(user_info_obj.getString("image2"));
                                }

                                if(user_info_obj.getString("image3").equals("") || user_info_obj.getString("image3").equals("0")){
                                    // If empty

                                }else{
                                    // Not Empty
                                    list_user_img_from_API.add(user_info_obj.getString("image3"));
                                }

                                if(user_info_obj.getString("image4").equals("") || user_info_obj.getString("image4").equals("0")){
                                    // If empty

                                }else{
                                    // Not Empty
                                    list_user_img_from_API.add(user_info_obj.getString("image4"));
                                }

                                if(user_info_obj.getString("image5").equals("") || user_info_obj.getString("image5").equals("0")){
                                    // If empty

                                }else{
                                    // Not Empty
                                    list_user_img_from_API.add(user_info_obj.getString("image5"));
                                }

                                if(user_info_obj.getString("image6").equals("") || user_info_obj.getString("image6").equals("0")){
                                    // If empty

                                }else{
                                    // Not Empty
                                    list_user_img_from_API.add(user_info_obj.getString("image6"));
                                }


                                // End Get User Images

                                String living = user_info_obj.getString("living");
                                String children = user_info_obj.getString("children");
                                String smoking = user_info_obj.getString("smoking");
                                String drinking = user_info_obj.getString("drinking");
                                String relationship = user_info_obj.getString("relationship");
                                String sexuality = user_info_obj.getString("sexuality");



                                if(living.equals("0") || living.equals("")){
                                    living = "";
                                }

                                if(children.equals("0") || children.equals("")){
                                    children = "";
                                }

                                if(smoking.equals("0") || smoking.equals("")){
                                    smoking = "";
                                }

                                if(drinking.equals("0") || drinking.equals("")){
                                    drinking = "";
                                }

                                if(relationship.equals("0") || relationship.equals("")){
                                    relationship = "";
                                }

                                if(sexuality.equals("0") || sexuality.equals("")){
                                    sexuality = "";
                                }
                            }

                        }catch (Exception b){
                            Functions.cancel_loader();
                            Functions.toast_msg(context,"" + b.toString());
                        } // End Catch Body

                    }  // End Get_Response Bodyt
                }  // ENd Call Back Body
        );

    } // End method to check if user already member or not.

}
