package com.qboxus.hugme.Code_Classes;

import android.app.Dialog;
import android.app.ProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Variables {

    public static int height;
    public static int width;

    public static String download_pref="download_pref";
     public static String facebook_banner_ad = "YOUR_PLACEMENT_ID";
    public static String res;

    public static int Var_num_click_to_show_Ads = 3;
    public static int Var_filled_val_new;
    public static int per_1;
    public static int Var_tab_change = 0;
    public static int is_searched = 0;
    public static int is_searched_at_ww = 0;

    public static int ad_display_after =9;


    public static int margin_left = 10;
    public static int margin_right = 10;
    public static int margin_top = 10;
    public static int margin_bottom = 10;


    public static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);



    public static int min_age = 15;

    public static String defalut_min_age="15";
    public static String defalut_max_age = "64";
    public static String defalut_max_distance = "10000";
    public static String defalut_gander="all";
    public static String defalut_search_by_status="all";

/*
    public static final int max_streming_time=60000000;
    public static final int max_video_calling_time=60000000;
    public static final int max_voice_calling_time=60000000;
*/

    public static final int max_streming_time=60000;
    public static final int max_video_calling_time=60000;
    public static final int max_voice_calling_time=60000;

    public static String Opened_Chat_id;
    public static String user_name;
    public static String user_pic;
    public static String user_id;
    public static Boolean check = true;
    public static final int MY_API_TIMEOUT_MS = 30000;

    // Edit Profile_F Variables

    public static String Var_living = "";
    public static String Var_children = "";
    public static String Var_smoking = "";
    public static String Var_drinking = "";
    public static String Var_relationship = "";
    public static String Var_sexuality = "";
    public static String Var_about_me = "";
    public static String user_gender = "";



    public static String gif_firstpart="https://media.giphy.com/media/";
    public static String gif_secondpart="/100w.gif";

    public static String gif_firstpart_chat="https://media.giphy.com/media/";
    public static String gif_secondpart_chat="/200w.gif";


    // Arrays
    public static final String[] arr_list_living = {"No answer","By myself","Student residence","With parents","With partner","With housemate(s)"};
    public static final String[] arr_list_relationship = {"No answer"," im in a complicated relationship", "Single"," Taken "};

    public static final String[] arr_list_children = {"No answer"," Grown up", "Already have"," No never ", "Someday"};
    public static final String[] arr_list_smoke = {"No answer","Im a heavy smoker","I hate smoking","i dont like it","im a social smoker","I smoke occasionally"};
    public static final String[] arr_list_drink = {"No answer"," I drink socially", "I dont drink"," Im against drinking ", "I drink a lot"};
    public static final String[] arr_list_sexuality = {"No answer"," Bisexual", "Gay"," Ask me ", "Straight"};
    public static final String[] arr_list_gender = {"Male","Female", "I dont want to disclose"," No Way ", "Ask Me"};


    // Fragments Names
    public static final String frag_about = "Describe_yourself_F";
    public static final String frag_Relation = "Relationship_A";
    public static final String frag_Living = "Living_F";
    public static final String frag_kids = "Kids_F";
    public static final String frag_Smoke = "Smoke_F";
    public static final String frag_Drink = "Drink_F";
    public static final String frag_gender = "Gender_F";


    public static String tag="hugme_";

    public static String verdana = "verdana.ttf";

    public static final int vedio_request_code = 9;

    public static final int permission_camera_code=786;
    public static final int permission_write_data=788;
    public static final int permission_Read_data=789;
    public static final int permission_Recording_audio=790;
    public static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 795;

    public static final String licencekey="play_store_licence_key";
    public static final String product_ID="android.hugme.subscription1";
    public static final String product_ID2="android.hugme.subscription2";
    public static final String product_ID3="android.hugme.subscription3";


    public static final int Boost_Time=1800000;
}
