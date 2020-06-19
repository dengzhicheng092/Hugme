package com.qboxus.hugme.All_Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qboxus.hugme.All_Activities.MainActivity;
import com.qboxus.hugme.All_Adapters.World_wide_ads_Adapter;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.Bottom_Sheet.View_Profile_Bottom_WorldWide;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.All_Adapters.Images_Adp;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Images;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.R;
import com.qboxus.hugme.All_Activities.SearchPlaces_A;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Utils.SpacesItemDecoration;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class World_wide_native_ads_F extends RootFragment {

    View v;
    public static RecyclerView recyclerView;
    public static World_wide_ads_Adapter adapter;

    FrameLayout fl;
    BottomSheetBehavior behavior;

    NestedScrollView scrollView;
    Context context;

    public static StaggeredGridLayoutManager manager;
    public static List<Object> nearby_list = new ArrayList<>();
    RelativeLayout find_nearby_User;
    PulsatorLayout pulsator;

    public static int NUMBER_OF_ADS;

    private AdLoader adLoader;

    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    ProgressBar progress_bar;
    RecyclerView RV_images_list;
    Images_Adp adp_img;
    ArrayList<String> about_me = new ArrayList<String>();
    FlowLayout item;
    String user_id_of_swipe ;
    TextView report_user;
    List<Get_Set_Images> list_profile_img;
    SimpleDraweeView iv;
    public static int swipe_position, view_type;

    ImageView like;
    ImageView IV1,IV2,IV3;
    ImageView dislike, move_left, move_right;
    String search_gender, search_filter_by, search_age_min, search_age_max, search_distance;

    TextView distance, about_me_tv_id, simple_about;
    Toolbar tb;

    ImageView right_overlay, left_overlay;
    SimpleDraweeView profileimage;
    Button change_setting_btn;
    public static TextView search_place_ww;
    String location_string;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_world_wide, null);

        context = getContext();
        fl = (FrameLayout) v.findViewById(R.id.fl_id);
        scrollView = (NestedScrollView) v.findViewById(R.id.nestedscrollview_id);
        iv = v.findViewById(R.id.profile_iv_id);
        behavior = BottomSheetBehavior.from(fl);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        progress_bar = v.findViewById(R.id.progress_bar);
        RV_images_list = v.findViewById(R.id.RV_images_list);

        right_overlay = v.findViewById(R.id.right_overlay);
        left_overlay = v.findViewById(R.id.left_overlay);
        pulsator = (PulsatorLayout) v.findViewById(R.id.pulsator);
        find_nearby_User = v.findViewById(R.id.find_nearby_User);
        profileimage = v.findViewById(R.id.profileimage);

        change_setting_btn = v.findViewById(R.id.change_setting_btn);
        change_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_filter_dialogue();
            }
        });


        String search_place = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_user_search_place_name);

        if(search_place != null){

            if(search_place.equals("People nearby")) {

                location_string = SharedPrefrence.get_string(context,"" + SharedPrefrence.u_lat_lng);

            }else{
                location_string = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_user_search_place_lat_lng);
            }

        }else{
            location_string = SharedPrefrence.get_string(context,"" + SharedPrefrence.u_lat_lng);

        }


        try{
            String img = Functions.get_info(context,"image1");
            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img))
                            .setProgressiveRenderingEnabled(false)
                            .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(false)
                    .build();

            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);

            profileimage.getHierarchy().setPlaceholderImage(R.drawable.image_placeholder);
            profileimage.getHierarchy().setFailureImage(R.drawable.image_placeholder);
            profileimage.getHierarchy().setRoundingParams(roundingParams);
            profileimage.setController(controller);
        }catch (Exception v){
            Functions.Log_d_msg(context,"" + v.toString());
        }

        MobileAds.initialize(context,
                getString(R.string.admob_app_id));


        adLoader = new AdLoader.Builder(context, context.getResources().getString(R.string.admob_app_id))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()

                        .build())
                .build();

        dislike = (ImageView) v.findViewById(R.id.left_overlay);
        like = (ImageView) v.findViewById(R.id.right_overlay);

        tb = (Toolbar) v.findViewById(R.id.toolbar_id);
        IV1 = (ImageView) v.findViewById(R.id.control_IV_id);
        IV2 = (ImageView) v.findViewById(R.id.main_f_setting_id);
        IV3 = (ImageView) v.findViewById(R.id.main_f_edit_id);

        IV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_filter_dialogue();
            }
        });
        move_left = v.findViewById(R.id.move_left);
        move_right = v.findViewById(R.id.move_right);

        move_left.setVisibility(View.VISIBLE);
        move_right.setVisibility(View.VISIBLE);
        distance = v.findViewById(R.id.distance);
        about_me_tv_id = v.findViewById(R.id.about_me_tv_id);
        simple_about = v.findViewById(R.id.simple_about);


        report_user = v.findViewById(R.id.report_user);
        report_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Functions.report_user(context,"" + user_id_of_swipe);

            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.world_wide_RV_id);

        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });



        get_Near_by_Users();



            return v;
    }

    @Override
    public void onResume() {
        super.onResume();


    }



    public void inflate_layout(){

        item = v.findViewById(R.id.inflate_layout);
        for(int i=0; i< about_me.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml

            TextView intrest_name = child.findViewById(R.id.intrest_name);
            if(about_me.get(i).equals("0") || about_me.get(i).equals(" ")){

                item.setVisibility(View.GONE);
            }else{

                intrest_name.setText("" + about_me.get(i));
                item.addView(child);

            }
        }

        about_me.clear();


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            ArrayList<Get_Set_Nearby> temp_list=new ArrayList<>();
            for (int i=0;i<nearby_list.size();i++){

                if(nearby_list.get(i) instanceof Get_Set_Nearby){
                    Get_Set_Nearby item=(Get_Set_Nearby) nearby_list.get(i);
                    if(Swipe_F.removed_user_ids_index.contains(item.getFb_id())){
                        temp_list.add(item);

                    }
                }
            }

            for(int i=0;i<temp_list.size();i++){
                nearby_list.remove(temp_list.get(i));
            }

            if(adapter!=null)
            adapter.notifyDataSetChanged();


            if(Variables.is_searched==1){

                nearby_list.clear();
                String handle_search = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_search_params);

                if(handle_search != null){

                    get_Near_by_Users();

                    Variables.is_searched = 0;

                }else{
                }
            }
        }
    }



    public void get_Near_by_Users( ) {

        String user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);

        String search = SharedPrefrence.get_string(context,SharedPrefrence.share_search_params);
        String search_gender = "", search_age_min= "", search_age_max = "", search_distance= "";
            try{

            JSONObject search_obj = new JSONObject(search);
            search_gender = search_obj.optString("gender",Variables.defalut_gander);
            search_age_min = search_obj.optString("age_min",Variables.defalut_min_age);
            search_age_max = search_obj.optString("age_max",Variables.defalut_max_age);
            search_distance = search_obj.optString("search_distance",Variables.defalut_max_distance);
        }catch (Exception n){
                search_gender = Variables.defalut_gander;
                search_age_min = Variables.defalut_min_age;
                search_age_max = Variables.defalut_max_age;
                search_distance = Variables.defalut_max_distance;

            }


        Functions.cancel_loader();
        final JSONObject parameters = new JSONObject();


        try {
            parameters.put("fb_id",  user_id);
            parameters.put("lat_long",  location_string);
            parameters.put("gender",  search_gender);
            parameters.put("distance",  search_distance);
            parameters.put("device_token", MainActivity.token);
            parameters.put("device", context.getResources().getString(R.string.device));
            parameters.put("age_min",  search_age_min);
            parameters.put("age_max",  search_age_max);
            parameters.put("version",context.getResources().getString(R.string.version));
            if(MainActivity.purduct_purchase)
                parameters.put("purchased","1");
            else
                parameters.put("purchased","0");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(
                context,
                 Api_Links.userNearByMe,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String resp) {
                        Functions.cancel_loader();

                        try {
                            JSONObject response = new JSONObject(resp);

                            if(progress_bar != null){
                               progress_bar.setVisibility(View.GONE);
                            }

                            handle_response(response);


                        } catch (Exception b) {
                            Functions.toast_msg(context, "Err " + b.toString());
                            Functions.cancel_loader();
                       }


                    }
                }

        );


    }

    public void handle_response(JSONObject response){
        try{
            JSONArray msg_arr = response.getJSONArray("msg");
            if(msg_arr.length()==0){
                find_nearby_User.setVisibility(View.VISIBLE);
                pulsator.start();


            }else{
                find_nearby_User.setVisibility(View.GONE);

                for(int i=0;i< msg_arr.length();i++){
                    JSONObject user_obj = msg_arr.getJSONObject(i);
                    user_obj.getString("fb_id");
                    user_obj.getString("first_name");
                    user_obj.getString("last_name");
                    user_obj.getString("birthday");
                    user_obj.getString("distance");
                    user_obj.getString("image1");

                    Get_Set_Nearby nearby = new Get_Set_Nearby(
                            "" +  user_obj.getString("fb_id"),
                            "" + user_obj.getString("first_name"),
                            "" + user_obj.getString("last_name"),
                            "" + user_obj.getString("birthday"),
                            "" + user_obj.getString("about_me"),
                            "" + user_obj.getString("distance"),
                            "" +  user_obj.getString("image1"),
                            "" + user_obj.getString("swipe"),
                            "" ,
                            "" + user_obj.getString("company"),
                            "" + user_obj.getString("school"),
                            "" + user_obj.getString("living"),
                            "" + user_obj.getString("children"),
                            "" + user_obj.getString("smoking"),
                            "" + user_obj.getString("drinking"),
                            "" + user_obj.getString("relationship"),
                            "" + user_obj.getString("sexuality"),
                            "" + user_obj.getString("block"),
                            "" +  user_obj.getString("image2"),
                            "" +  user_obj.getString("image3"),
                            "" +  user_obj.getString("image4"),
                            "" +  user_obj.getString("image5"),
                            "" +  user_obj.getString("image6")

                    );

                    nearby_list.add(nearby);

                }


                loadNativeAds();

            }

        }catch (Exception b){
            Functions.toast_msg(context,"Error " + b.toString());
        }

    }



    private void loadNativeAds() {

        NUMBER_OF_ADS = nearby_list.size()/Variables.ad_display_after;

        AdLoader.Builder builder = new AdLoader.Builder(context, getString(R.string.ad_unit_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {

                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        int index = Variables.ad_display_after;
        while (index < nearby_list.size()) {

            for (UnifiedNativeAd ad : mNativeAds) {
                nearby_list.add(index, ad);
                index = index + Variables.ad_display_after+1;
                if (index > nearby_list.size()) {
                    Set_Adapter();
                    return;
                }

            }

            Set_Adapter();
        }

    }


    public void Set_Adapter(){
        adapter = new World_wide_ads_Adapter(new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {
                view_type = adapter.getItemViewType(postion);

                Get_Set_Nearby get_nearby = (Get_Set_Nearby) nearby_list.get(postion);
                get_nearby.getImage1();

                Bundle bundle_user_profile = new Bundle();
                bundle_user_profile.putString("user_id", "" + user_id_of_swipe);
                bundle_user_profile.putString("current_position", "" + swipe_position);
                bundle_user_profile.putString("view_type", "" + view_type);

                bundle_user_profile.putSerializable("user_near_by", get_nearby);

                View_Profile_Bottom_WorldWide view_profile = new View_Profile_Bottom_WorldWide();
                view_profile.setArguments(bundle_user_profile);

                view_profile.show(getActivity().getSupportFragmentManager(), view_profile.getTag());

                swipe_position = postion;


                user_id_of_swipe = get_nearby.getFb_id();

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

                about_me.add(get_nearby.getChildren());
                about_me.add(get_nearby.getLiving());
                about_me.add(get_nearby.getRelationship());
                about_me.add(get_nearby.getSchool());
                about_me.add(get_nearby.getChildren());
                about_me.add(get_nearby.getSexuality());
                about_me.add(get_nearby.getSmoking());

                list_profile_img = new ArrayList<>();

                if(get_nearby.getImage2().equals("") || get_nearby.getImage2().equals("0")){


                }else{
                    Get_Set_Images add_2 = new Get_Set_Images("" + get_nearby.getImage2());
                    list_profile_img.add(add_2);
                }

                if(get_nearby.getImage3().equals("") || get_nearby.getImage3().equals("0")){

                }else{
                    Get_Set_Images add_3 = new Get_Set_Images("" + get_nearby.getImage3());
                    list_profile_img.add(add_3);
                }


                if(get_nearby.getImage4().equals("") || get_nearby.getImage4().equals("0")){


                }else{
                    Get_Set_Images add_4 = new Get_Set_Images("" + get_nearby.getImage4());
                    list_profile_img.add(add_4);
                }

                if(get_nearby.getImage5().equals("") || get_nearby.getImage5().equals("0")){

                }else{
                    Get_Set_Images add_5 = new Get_Set_Images("" + get_nearby.getImage5());
                    list_profile_img.add(add_5);
                }

                if(get_nearby.getImage6().equals("") || get_nearby.getImage6().equals("0")){

                }else{
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

                final GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
                RV_images_list.setLayoutManager(layoutManager);

                RV_images_list.setAdapter(adp_img);


                inflate_layout();


                fl.setBackgroundColor(getResources().getColor(R.color.normal_transparent));

                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int i) {
                        if (i == BottomSheetBehavior.STATE_DRAGGING) {
                            fl.setBackgroundColor(getResources().getColor(R.color.full_transparent));
                        }

                        if (i == BottomSheetBehavior.STATE_EXPANDED) {
                            fl.setBackgroundColor(getResources().getColor(R.color.normal_transparent));
                            // fl
                            Main_F.tb.setVisibility(View.GONE);
                            Main_F.tl_rl.setVisibility(View.GONE);
                        }

                        if (i == BottomSheetBehavior.STATE_COLLAPSED) {
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            fl.setBackgroundColor(getResources().getColor(R.color.full_transparent));
                            //  Main_F.tb.setVisibility(View.VISIBLE);
                            Main_F.tl_rl.setVisibility(View.VISIBLE);

                            // todo: Smoth Scrol to position
                            recyclerView.smoothScrollToPosition(swipe_position);


                            about_me.clear();
                            item.removeAllViews();
                            list_profile_img.clear();

                        }

                        if (i == BottomSheetBehavior.STATE_HIDDEN) {
                            fl.setBackgroundColor(getResources().getColor(R.color.full_transparent));
                            Main_F.tl_rl.setVisibility(View.VISIBLE);
                            recyclerView.smoothScrollToPosition(swipe_position);

                        }

                    }

                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                });

            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        }, nearby_list, context);


        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.base_16), getResources().getDimensionPixelOffset(R.dimen.base_90)));

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

    }


    public void display_filter_dialogue(){

        final Dialog dialog = new Dialog(getContext());

        final View dialogview =  LayoutInflater.from(getContext()).inflate(R.layout.item_filter_dialog_layout,null);
        String handle_search = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_search_params);

        String handle_search_gender = "", handle_search_filter_by ="", handle_search_age_min= "", handle_search_age_max = "",
                handle_search_distance= "";
        String user_search_place = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_user_search_place_name);


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
        simpleSeekBar.setProgress(Integer.parseInt(Variables.defalut_max_distance));
        distance.setText("" + simpleSeekBar.getProgress() );
        dialog.setContentView(dialogview);

        RelativeLayout near_by_RL_id = dialogview.findViewById(R.id.near_by_RL_id);
        search_place_ww = dialogview.findViewById(R.id.search_place);
        if(user_search_place != null){
            search_place_ww.setText("" + user_search_place);
        }else{
            search_place_ww.setText("People nearby");
        }



        near_by_RL_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchPlaces_A.class));
            }
        });




        if(handle_search != null) {

            try {

                JSONObject search_obj = new JSONObject(handle_search);
                handle_search_gender = search_obj.optString("gender");
                handle_search_filter_by = search_obj.optString("filter_by");
                handle_search_age_min = search_obj.optString("age_min");
                handle_search_age_max = search_obj.optString("age_max");
                handle_search_distance = search_obj.optString("search_distance");
                text_age.setText( handle_search_age_min + " - " + handle_search_age_max);

                rangeBar.setRangePinsByValue(Float.parseFloat(handle_search_age_min), Float.parseFloat(handle_search_age_max));

            } catch (Exception n) {
            }

            search_distance = handle_search_distance;
            search_age_min = handle_search_age_min;
            search_age_max = handle_search_age_max;
            search_gender = handle_search_gender;
            try {
                if (handle_search_distance != null) {
                    simpleSeekBar.setProgress(Integer.parseInt(handle_search_distance));
                    distance.setText("" + handle_search_distance);
                } else {
                    distance.setText(Variables.defalut_max_distance);
                    simpleSeekBar.setProgress(Integer.parseInt(Variables.defalut_max_distance));
                }

            } catch (Exception b) {

            }

            if (handle_search_gender.equals("female")) {
                tv1.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv.setBackgroundResource(R.drawable.d_gray_border);
                tv2.setBackgroundResource(R.drawable.d_gray_border);
            } else if (handle_search_gender.equals("male")) {
                tv1.setBackgroundResource(R.drawable.d_gray_border);
                tv.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv2.setBackgroundResource(R.drawable.d_gray_border);
            } else {
                tv1.setBackgroundResource(R.drawable.d_gray_border);
                tv.setBackgroundResource(R.drawable.d_gray_border);
                tv2.setBackgroundResource(R.drawable.d_round_blue_border_radius8);


            }

            if (handle_search_filter_by.equals("All")) {
                tv4.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv5.setBackgroundResource(R.drawable.d_gray_border);
                tv6.setBackgroundResource(R.drawable.d_gray_border);

            } else if (handle_search_filter_by.equals("Online")) {
                tv4.setBackgroundResource(R.drawable.d_gray_border);
                tv5.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv6.setBackgroundResource(R.drawable.d_gray_border);

            } else {
                tv4.setBackgroundResource(R.drawable.d_gray_border);
                tv5.setBackgroundResource(R.drawable.d_gray_border);
                tv6.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            }


        }
        else{

            search_gender = Variables.defalut_gander;
            search_filter_by = Variables.defalut_search_by_status;
            search_age_min = Variables.defalut_min_age;
            search_age_max = Variables.defalut_max_age;
            search_distance = Variables.defalut_max_distance;
        }


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
                    text_age.setText("" + leftPinValue + " - " + "" + rightPinValue);
                    Functions.Log_d_msg(context,"Left " + leftPinValue + " Right " + rightPinValue);
                }
            });

        }catch (Exception b){

            Functions.Log_d_msg(context,"" + b.toString());
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
                add_all_filter(tv6);
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

                progress_bar.setVisibility(View.VISIBLE);
                mNativeAds.clear();
                nearby_list.clear();
                try{
                    adapter.notifyDataSetChanged();
                }catch (Exception b){

                }

                String search_place = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_user_search_place_name);
                if(search_place != null){
                    if(search_place.equals("People nearby")) {
                        location_string = SharedPrefrence.get_string(context,"" + SharedPrefrence.u_lat_lng);

                    }else{
                        location_string = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_user_search_place_lat_lng);
                    }

                }else{
                    location_string = SharedPrefrence.get_string(context,"" + SharedPrefrence.u_lat_lng);
                }

                try{
                    JSONObject search_params = new JSONObject();
                    search_params.put("gender","" + search_gender);
                    search_params.put("filter_by","" + search_filter_by);
                    search_params.put("age_min","" + search_age_min);
                    search_params.put("age_max","" + search_age_max);
                    search_params.put("search_distance","" + search_distance);

                    SharedPrefrence.save_string(context,"" + search_params.toString(),
                            "" + SharedPrefrence.share_search_params);


                }catch (Exception b){

                }

                Variables.is_searched_at_ww=1;
                handle_search_params();

                dialog.dismiss();
            }
        });


        dialog.show();


    }


    public void add_gender(TextView textView){
        search_gender = textView.getText().toString();

        if(search_gender.equals("Male")){
            search_gender = "male";
        }else if(search_gender.equals("Female")){
            search_gender = "female";
        }else if(search_gender.equals("Both")){
            search_gender = "all";
        }

    }


    public void add_all_filter(TextView textView){
        search_filter_by = textView.getText().toString();
    }

    public void handle_search_params(){

        String handle_search = SharedPrefrence.get_string(context,"" + SharedPrefrence.share_search_params);
        String handle_search_gender = "", handle_search_age_min= "", handle_search_age_max = "",
                handle_search_distance= "";

        if(handle_search != null){

            try{

                JSONObject search_obj = new JSONObject(handle_search);
                handle_search_gender = search_obj.getString("gender");
                handle_search_age_min = search_obj.getString("age_min");
                handle_search_age_max = search_obj.getString("age_max");
                handle_search_distance = search_obj.getString("search_distance");
            }catch (Exception n){
                Functions.Log_d_msg(context,"Obj Error " + n.toString());
            }

            search_distance = handle_search_distance;
            search_age_min = handle_search_age_min;
            search_age_max = handle_search_age_max;
            search_gender = handle_search_gender;


        }

        try{
            get_Near_by_Users();

        }catch (Exception b){
            Functions.Log_d_msg(context,"" + b.toString());
        }



    }

    // when we swipe left , right or reverse then this method is call and update the value in firebase database
    public static void updatedata_onLeftSwipe (Context context ,final Get_Set_Nearby item){
        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();;

        Functions.send_push_notification(context, item.getFb_id(), "Messege","dislike");

        final String user_id = Functions.get_info(context,"fb_id");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh");
        final String formattedDate = df.format(c);

        rootref.child("Match").child(item.getFb_id()).child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map mymap=new HashMap<>();
                    mymap.put("fragment_match","false");
                    mymap.put("type","dislike");
                    mymap.put("status","0");
                    mymap.put("time",formattedDate);
                    mymap.put("name",item.getFirst_name());
                    mymap.put("effect","true");


                    Map othermap=new HashMap<>();
                    othermap.put("fragment_match","false");
                    othermap.put("type","dislike");
                    othermap.put("status","0");
                    othermap.put("time",formattedDate);
                    othermap.put("name",item.getFirst_name());
                    othermap.put("effect","false");

                    rootref.child("Match").child(user_id+"/"+item.getFb_id()).updateChildren(mymap);
                    rootref.child("Match").child(item.getFb_id()+"/"+user_id).updateChildren(othermap);

                }else {
                    Map mymap=new HashMap<>();
                    mymap.put("fragment_match","false");
                    mymap.put("type","dislike");
                    mymap.put("status","0");
                    mymap.put("time",formattedDate);
                    mymap.put("name",item.getFirst_name());
                    mymap.put("effect","true");

                    Map othermap=new HashMap<>();
                    othermap.put("fragment_match","false");
                    othermap.put("type","dislike");
                    othermap.put("status","0");
                    othermap.put("time",formattedDate);
                    othermap.put("name",item.getFirst_name());
                    othermap.put("effect","false");

                    rootref.child("Match").child(user_id+"/"+item.getFb_id()).setValue(mymap);
                    rootref.child("Match").child(item.getFb_id()+"/"+user_id).setValue(othermap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public static void updatedata_onrightSwipe (Context context ,final Get_Set_Nearby item){

        // todo: Push Notification.

        Functions.send_push_notification(context, item.getFb_id(), "Messege","like");

        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();;
        // todo: Get User ID from Shared Prefrence:
        final String user_id = Functions.get_info(context,"fb_id");

        final String user_name = Functions.get_info(context,"first_name");


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh");
        final String formattedDate = df.format(c);

        Query query=rootref.child("Match").child(item.getFb_id()).child(user_id);
        query.keepSynced(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() || item.getSwipe().equals("like")){
                    Map mymap=new HashMap<>();
                    mymap.put("fragment_match","true");
                    mymap.put("type","like");
                    mymap.put("status","0");
                    mymap.put("time",formattedDate);
                    mymap.put("name",item.getFirst_name());
                    mymap.put("effect","true");

                    Map othermap=new HashMap<>();
                    othermap.put("fragment_match","true");
                    othermap.put("type","like");
                    othermap.put("status","0");
                    othermap.put("time",formattedDate);
                    othermap.put("name",user_name);
                    othermap.put("effect","false");

                    rootref.child("Match").child(user_id+"/"+item.getFb_id()).updateChildren(mymap);
                    rootref.child("Match").child(item.getFb_id()+"/"+user_id).updateChildren(othermap);

                }else {
                    Map mymap=new HashMap<>();
                    mymap.put("fragment_match","false");
                    mymap.put("type","like");
                    mymap.put("status","0");
                    mymap.put("time",formattedDate);
                    mymap.put("name",item.getFirst_name());
                    mymap.put("effect","true");

                    Map othermap=new HashMap<>();
                    othermap.put("fragment_match","false");
                    othermap.put("type","like");
                    othermap.put("status","0");
                    othermap.put("time",formattedDate);
                    othermap.put("name",user_name);
                    othermap.put("effect","false");

                    rootref.child("Match").child(user_id+"/"+item.getFb_id()).setValue(mymap);
                    rootref.child("Match").child(item.getFb_id()+"/"+user_id).setValue(othermap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
