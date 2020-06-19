package com.qboxus.hugme.Shared_pref;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONObject;

import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Activities.Splashscreen_A;

import static com.qboxus.hugme.Code_Classes.Variables.per_1;

public class SharedPrefrence {

    public static SharedPreferences.Editor editor;
    public static SharedPreferences pref;
    public static String pref_name="hugme";

    public static String u_id = "fb_id_social";
    public static String u_fname = "u_fname";
    public static String u_lname = "u_lname";
    public static String u_image = "u_image";
    public static String u_gender = "u_gender";
    public static String u_age = "u_age";
    public static String u_lat_lng = "user_lat_lng";

    public static String u_click = "click";
    public static String u_Device_token = "device_token";
    public static String u_login_detail = "user_info";

    public static String share_search_params = "search_params";

    public static String share_user_search_place_name = "search_place_name";
    public static String share_user_search_place_lat_lng = "search_place_lat_lng";

    public static String share_filter_inbox_key = "inbox_filter";
    public static String share_profile_hide_or_not = "profile_hide";
    public static String share_social_info = "social_info";

    public static String is_puchase="is_puchase";

    public static String Boost_On_Time="Boost_On_Time";



    public static String streaming_used_time="streaming_used_time";
    public static String video_calling_used_time="video_calling_used_time";
    public static String voice_calling_used_time="voice_calling_used_time";



    public static void init_share(Context context){
        pref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static void save_string(Context context, String value, String data_key)
    {
        if(pref==null || editor==null)
        init_share(context);


        editor.putString(data_key, value);
        editor.commit();
    }

    public static String get_string(Context context, String key){
        init_share(context);
        return pref.getString(key, null);

    }

    public static void save_int(Context context, int value, String data_key)
    {
        if(pref==null || editor==null)
            init_share(context);


        editor.putInt(data_key, value);
        editor.commit();
    }

    public static int get_int(Context context, String key){
        init_share(context);
        return pref.getInt(key, 0);

    }


    public static void save_boolean(Context context, boolean value, String data_key)
    {
        if(pref==null || editor==null)
            init_share(context);


        editor.putBoolean(data_key, value);
        editor.commit();
    }

    public static boolean get_boolean(Context context, String key){
        init_share(context);
        return pref.getBoolean(key, false);
    }


    // Clear Key
    public static void remove_value_of_key (Context context, String key){
        init_share(context);
        pref.edit().remove(key).commit();
    }


    public static void logout_user(Context context)
    {
        if(pref==null || editor==null)
            init_share(context);


        pref.edit().remove(u_login_detail).commit();
        pref.edit().remove(share_social_info).commit();
        pref.edit().remove(u_lat_lng).commit();
        pref.edit().remove(share_search_params).commit();
        pref.edit().remove(share_profile_hide_or_not).commit();
        pref.edit().remove(share_filter_inbox_key).commit();

        Variables.Var_filled_val_new = 0;
        per_1 = 0;
        Intent intent = new Intent(context, Splashscreen_A.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }




    public static String get_social_info (Context context, String key,String what_val_want){

        String info = SharedPrefrence.get_string(context,key);
        String val = "";

        try{
            JSONObject info_obj = new JSONObject(info);
            val = info_obj.getString("" + what_val_want);
          }catch (Exception b){

        }


        return val;
    }

    public static int calculate_complete_profile(Context context){

        String info = SharedPrefrence.get_string(context,SharedPrefrence.u_login_detail);

        Variables.Var_filled_val_new = 0;
        per_1 = 0;
        int total_val = 19;


        try{
            JSONObject info_obj = new JSONObject(info);

            info_obj.getString("about_me");
            info_obj.getString("job_title");
            info_obj.getString("gender");
            info_obj.getString("birthday");
            info_obj.getString("age");
            info_obj.getString("company");
            info_obj.getString("school");
            info_obj.getString("living");
            info_obj.getString("children");
            info_obj.getString("smoking");
            info_obj.getString("drinking");
            info_obj.getString("relationship");
            info_obj.getString("sexuality");
            info_obj.getString("image1");
            info_obj.getString("image2");
            info_obj.getString("image3");
            info_obj.getString("image4");
            info_obj.getString("image5");
            info_obj.getString("image6");


            if(info_obj.getString("job_title").equals("") || info_obj.getString("job_title").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }

            if(info_obj.getString("age").equals("") || info_obj.getString("age").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }




            if(info_obj.getString("about_me").equals("") || info_obj.getString("about_me").equals("0")){

            }else{

                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
             }


            if(info_obj.getString("gender").equals("") || info_obj.getString("gender").equals("0")){

            } else{
               Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }

            if(info_obj.getString("birthday").equals("") || info_obj.getString("birthday").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }

            if(info_obj.getString("company").equals("") || info_obj.getString("company").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }

            if(info_obj.getString("school").equals("") || info_obj.getString("school").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }

            if(info_obj.getString("living").equals("") || info_obj.getString("living").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }
            if(info_obj.getString("children").equals("") || info_obj.getString("children").equals("0") ){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }


            if(info_obj.getString("smoking").equals("") || info_obj.getString("smoking").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }

            if(info_obj.getString("drinking").equals("") || info_obj.getString("drinking").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }

            if(info_obj.getString("relationship").equals("") || info_obj.getString("relationship").equals("0")){

            }else{

                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;

            }

            if(info_obj.getString("sexuality").equals("") || info_obj.getString("sexuality").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
            }
            if(info_obj.getString("image1").equals("") || info_obj.getString("image1").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
               }

            if(info_obj.getString("image2").equals("") || info_obj.getString("image2").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
               }
            if(info_obj.getString("image3").equals("") || info_obj.getString("image3").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
              }


            if(info_obj.getString("image4").equals("") || info_obj.getString("image4").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
               }

            if(info_obj.getString("image5").equals("") || info_obj.getString("image5").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
               }
            if(info_obj.getString("image6").equals("") || info_obj.getString("image6").equals("0")){

            }else{
                Variables.Var_filled_val_new = Variables.Var_filled_val_new + 1;
              }

            float percantage_1 = ((float)Variables.Var_filled_val_new/(float)total_val)*100;
            per_1 = Integer.parseInt("" + (int) percantage_1);


        }catch (Exception b){

        }


        return per_1;
    }



}
