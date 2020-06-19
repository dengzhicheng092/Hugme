package com.qboxus.hugme.All_Fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.flowlayout.FlowLayout;
import com.qboxus.hugme.All_Activities.MainActivity;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.Code_Classes.Functions;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;


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

import com.qboxus.hugme.All_Activities.Chat_A;
import com.qboxus.hugme.All_Adapters.Card_Adapter;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Images;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.Bottom_Sheet.View_Profile_Bottom_Swipe;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.All_Adapters.Images_Adp;
import com.qboxus.hugme.R;
import com.qboxus.hugme.All_Activities.SearchPlaces_A;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class Swipe_F extends RootFragment {

    View v;
    public static CardStackView cardStackView;
    public static Card_Adapter adapter;


    public static String token_other_user;
    String user_id_of_swipe, user_name_of_swipe;
    public static int list_index_int;
    public static BottomSheetBehavior behavior;
    List<Get_Set_Images> list_profile_img;
    public static FrameLayout fl;
    SimpleDraweeView iv;
    Context context;
    String search_gender, search_filter_by, search_age_min, search_age_max, search_distance;

    TextView distance, about_me_tv_id, simple_about;
    List<Get_Set_Nearby> nearby_list = new ArrayList<>();
    ProgressBar progress_bar;
    ImageView favorite_id;
    TextView report_user;
    DatabaseReference rootref;
    ArrayList<String> about_me = new ArrayList<String>();

    public static ArrayList<Object> removed_user_ids_index = new ArrayList<Object>();

    FlowLayout item;
    RecyclerView RV_images_list;
    Images_Adp adp_img;

     ImageView like;
     ImageView IV1,IV2,IV3;
     ImageView dislike, move_left, move_right;
     Toolbar tb;
     RelativeLayout find_nearby_User;
     PulsatorLayout pulsator;
     SimpleDraweeView profileimage;
     Button change_setting_btn;
     String location_string;
    public static  TextView search_place;

    ArrayList<Integer> about_me_pics = new ArrayList<Integer>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_swipe, null);
        context = getContext();

        iv = v.findViewById(R.id.profile_iv_id);
        nearby_list.clear();
        progress_bar = v.findViewById(R.id.progress_bar);
        fl = (FrameLayout) v.findViewById(R.id.fl_id);
        behavior = BottomSheetBehavior.from(fl);
        favorite_id = v.findViewById(R.id.favorite_id);
        favorite_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_chat(context,"12","Name","sdkj");
            }
        });
        pulsator = (PulsatorLayout) v.findViewById(R.id.pulsator);
        Main_F.tb.setVisibility(View.GONE);
        find_nearby_User = v.findViewById(R.id.find_nearby_User);
        profileimage = v.findViewById(R.id.profileimage);
        change_setting_btn = v.findViewById(R.id.change_setting_btn);

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


        Main_F.tb.setVisibility(View.GONE);
        distance = v.findViewById(R.id.distance);
        about_me_tv_id = v.findViewById(R.id.about_me_tv_id);
        simple_about = v.findViewById(R.id.simple_about);
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

        String user_id = SharedPrefrence.get_string(context,"" + SharedPrefrence.u_id);


        get_user_info(user_id);

        change_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_filter_dialogue();
            }
        });


        rootref = FirebaseDatabase.getInstance().getReference();
        report_user = v.findViewById(R.id.report_user);

        move_left = v.findViewById(R.id.move_left);
        move_right = v.findViewById(R.id.move_right);

        report_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.report_user(context,"" + user_id_of_swipe);

            }
        });
        dislike = (ImageView) v.findViewById(R.id.left_overlay);
        like = (ImageView) v.findViewById(R.id.right_overlay);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        RV_images_list = v.findViewById(R.id.RV_images_list);

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

        cardStackView = (CardStackView) v.findViewById(R.id.swipe_csv_id);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {

                int org_index = cardStackView.getTopIndex()-1;

                if(org_index < adapter.getCount()) {

                     if (direction.equals(SwipeDirection.Left)) {
                         Swipe_F.updatedata_onLeftSwipe(getContext(), adapter.getItem(org_index));
                     }

                     else if(direction.equals(SwipeDirection.Right)){
                         Swipe_F.updatedata_onrightSwipe(getContext(), adapter.getItem(org_index));

                     }

                    if (cardStackView.getTopIndex() == adapter.getCount()) {
                        tb.setVisibility(View.VISIBLE);
                        find_nearby_User.setVisibility(View.VISIBLE);
                        pulsator.start();
                     }


                    }


                }


            @Override
            public void onCardReversed() {
            }

            @Override
            public void onCardMovedToOrigin() {

            }

            @Override
            public void onCardClicked(int index) {

                Get_Set_Nearby get_nearby = nearby_list.get(index);
                get_nearby.getImage1();

                user_id_of_swipe = get_nearby.getFb_id();
                user_name_of_swipe = get_nearby.getFirst_name();
                list_index_int = index;
                distance.setText("" + get_nearby.getDistance());



                Bundle bundle_user_profile = new Bundle();
                bundle_user_profile.putString("user_id", "" + user_id_of_swipe);

                bundle_user_profile.putSerializable("user_near_by", get_nearby);

                View_Profile_Bottom_Swipe view_profile = new View_Profile_Bottom_Swipe();
                view_profile.setArguments(bundle_user_profile);
                view_profile.setCancelable(true);
                view_profile.show(getActivity().getSupportFragmentManager(), view_profile.getTag());


                Functions.Log_d_msg(context,"V " + get_nearby.getAbout_me());

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

                if(get_nearby.getChildren().equals("")){


                }else{

                    about_me.add(get_nearby.getChildren());
                    about_me_pics.add(R.drawable.ic_childrens);
                }

                if(get_nearby.getLiving().equals("")){


                }else{

                    about_me.add(get_nearby.getLiving());
                    about_me_pics.add(R.drawable.ic_living);
                }

                if(get_nearby.getRelationship().equals("")){


                }else{

                    about_me.add(get_nearby.getRelationship());
                    about_me_pics.add(R.drawable.ic_relationship);
                }


                if(get_nearby.getSchool().equals("")){


                }else{

                    about_me.add(get_nearby.getSchool());
                    about_me_pics.add(R.drawable.ic_living);
                }

                if(get_nearby.getChildren().equals("")){


                }else{
                    // If not Empty
                    about_me.add(get_nearby.getChildren());
                    about_me_pics.add(R.drawable.ic_childrens);
                }

                if(get_nearby.getSexuality().equals("")){


                }else{
                    about_me.add(get_nearby.getSexuality());
                    about_me_pics.add(R.drawable.ic_sexuality);
                }

                if(get_nearby.getSmoking().equals("")){


                }else{
                    about_me.add(get_nearby.getSmoking());
                    about_me_pics.add(R.drawable.ic_smoking);
                }


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

                RV_images_list.setLayoutManager(new GridLayoutManager(getContext(), 1));
                RV_images_list.setHasFixedSize(false);
                RV_images_list.setAdapter(adp_img);

                if(get_nearby.getImage2().equals("") && get_nearby.getImage3().equals("") &&
                        get_nearby.getImage4().equals("") &&
                        get_nearby.getImage5().equals("") &&
                        get_nearby.getImage6().equals("") ){

                    RV_images_list.setVisibility(View.GONE);
                }else{
                    RV_images_list.setVisibility(View.VISIBLE);
                }



                try{
                    Uri uri = Uri.parse(get_nearby.getImage1());
                    iv.setImageURI(uri);
                    iv.getHierarchy().setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.image_placeholder));

                }catch (Exception v){

                }
                inflate_layout();


                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int i) {
                        if (i == BottomSheetBehavior.STATE_DRAGGING){
                            fl.setBackgroundColor(getResources().getColor(R.color.full_transparent));
                        }

                        if (i == BottomSheetBehavior.STATE_EXPANDED) {
                            fl.setBackgroundColor(getResources().getColor(R.color.normal_transparent));
                        }

                        if (i == BottomSheetBehavior.STATE_COLLAPSED){
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            fl.setBackgroundColor(getResources().getColor(R.color.full_transparent));

                            about_me.clear();
                            item.removeAllViews();
                            list_profile_img.clear();
                        }

                        if (i == BottomSheetBehavior.STATE_HIDDEN){
                            fl.setBackgroundColor(getResources().getColor(R.color.full_transparent));
                        }

                    }

                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                });
            }
        });

        intimages();


        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
        lp.height = (int) (Variables.height/1.4);

        iv.setLayoutParams(lp);

        get_Near_by_Users();


        return v;
    }

    private void intimages() {

        init_adp();

    }




    public static void open_chat (Context context,String receiverid, String name, String receiver_pic){

        Intent myIntent = new Intent(context, Chat_A.class);
        myIntent.putExtra("receiver_id", receiverid);
        myIntent.putExtra("receiver_name", name);
        myIntent.putExtra("receiver_pic", receiver_pic);
        context.startActivity(myIntent);

    }

    public static void updatedata_onLeftSwipe (Context context ,final Get_Set_Nearby item){


        try{

            Functions.Count_num_click(context);


            Functions.display_fb_ad(context);
        }catch (Exception b){


        }



        Functions.send_push_notification(context, item.getFb_id(), "dislike you","dislike");

        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();;

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
        try{

            Functions.Count_num_click(context);


            Functions.display_fb_ad(context);
        }catch (Exception b){


        }


        Functions.send_push_notification(context, item.getFb_id(), "Like you","like");

        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();;
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
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

     }

    public void inflate_layout(){

        item = v.findViewById(R.id.inflate_layout);

        for(int i=0; i< about_me.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml

            TextView intrest_name = child.findViewById(R.id.intrest_name);
            ImageView image = child.findViewById(R.id.ic_image);
            if(about_me.get(i).equals("0") || about_me.get(i).equals(" ")){

                item.setVisibility(View.GONE);
            }else{
                intrest_name.setText("" + about_me.get(i));
                image.setImageResource(about_me_pics.get(i));
                item.addView(child);
            }

        }

        about_me.clear();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Main_F.tb.setVisibility(View.GONE);

            if( Variables.is_searched_at_ww==1){
                nearby_list.clear();
                adapter.notifyDataSetChanged();

                init_adp();
                get_Near_by_Users();
                cardStackView.setAdapter(adapter);
                Variables.is_searched_at_ww=0;
            }

        }
    }



    public void get_Near_by_Users() {

        progress_bar.setVisibility(View.VISIBLE);

        String search_place = SharedPrefrence.get_string(context, SharedPrefrence.share_user_search_place_name);

        if(search_place != null){

            if(search_place.equals("People nearby")) {
                location_string = SharedPrefrence.get_string(context,SharedPrefrence.u_lat_lng);

            }else{
                location_string = SharedPrefrence.get_string(context, SharedPrefrence.share_user_search_place_lat_lng);
            }

        }else{
            location_string = SharedPrefrence.get_string(context,SharedPrefrence.u_lat_lng);
        }

        String user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);

        String search = SharedPrefrence.get_string(context,SharedPrefrence.share_search_params);
        String search_gender = "", search_filter_by ="", search_age_min= "", search_age_max = "", search_distance= "";
        try{

            JSONObject search_obj = new JSONObject(search);
            search_gender = search_obj.optString("gender",Variables.defalut_gander);
            search_age_min = search_obj.optString("age_min",Variables.defalut_min_age);
            search_age_max = search_obj.optString("age_max",Variables.defalut_max_age);
            search_distance = search_obj.optString("search_distance",Variables.defalut_max_distance);
        }catch (Exception n){
            search_gender = Variables.defalut_gander;
            search_filter_by = Variables.defalut_search_by_status;
            search_age_min = Variables.defalut_min_age;
            search_age_max = Variables.defalut_max_age;
            search_distance = Variables.defalut_max_distance;
            progress_bar.setVisibility(View.GONE);
        }


        final JSONObject parameters = new JSONObject();


        try {
            parameters.put("fb_id", user_id);
            parameters.put("lat_long",  location_string);
            parameters.put("gender",  search_gender );
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
            progress_bar.setVisibility(View.GONE);
        }

        ApiRequest.Call_Api(
                context,
                "" + Api_Links.userNearByMe,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String resp) {

                        progress_bar.setVisibility(View.GONE);
                        Functions.Log_d_msg(context,"Near from Search \n " + parameters.toString() );
                        Functions.Log_d_msg(context,"Near from Response  \n " + resp );
                        try {
                            JSONObject response = new JSONObject(resp);

                            JSONArray msg_arr = response.getJSONArray("msg");

                            if(msg_arr.length() == 0){

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

                                    user_obj.getString("image2");
                                    user_obj.getString("image3");
                                    user_obj.getString("image4");
                                    user_obj.getString("image5");
                                    user_obj.getString("image6");



                                    user_obj.getString("job_title");
                                    user_obj.getString("company");
                                    user_obj.getString("school");
                                    user_obj.getString("living");
                                    user_obj.getString("children");
                                    user_obj.getString("smoking");
                                    user_obj.getString("drinking");
                                    user_obj.getString("relationship");
                                    user_obj.getString("sexuality");
                                    user_obj.getString("block");


                                    Get_Set_Nearby nearby = new Get_Set_Nearby(
                                            "" +  user_obj.getString("fb_id"),
                                            "" + user_obj.getString("first_name"),
                                            "" + user_obj.getString("last_name"),
                                            "" + user_obj.getString("birthday"),
                                            "" + user_obj.getString("about_me"),
                                            "" + user_obj.getString("distance"),
                                            "" +  user_obj.getString("image1"),
                                            "like",

                                            "" + user_obj.getString("job_title"),
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


                                    adapter.add(nearby);
                                    nearby_list.add(nearby);

                                }

                                cardStackView.setAdapter(adapter);

                            }


                        }catch (Exception b){
                            progress_bar.setVisibility(View.GONE);
                        }
                    }
                }
        );
    }




    public void display_filter_dialogue(){

        final Dialog dialog = new Dialog(getContext());

        final View dialogview =  LayoutInflater.from(getContext()).inflate(R.layout.item_filter_dialog_layout,null);

        String handle_search = SharedPrefrence.get_string(context, SharedPrefrence.share_search_params);

        String handle_search_gender = "", handle_search_filter_by ="", handle_search_age_min= "", handle_search_age_max = "",
                handle_search_distance= "";

        search_gender = Variables.defalut_gander;
        search_filter_by = Variables.defalut_search_by_status;
        search_age_min = Variables.defalut_min_age;
        search_age_max = Variables.defalut_max_age;
        search_distance = Variables.defalut_max_distance;

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

        search_place = dialogview.findViewById(R.id.search_place);
        if(user_search_place != null){
            // If it is not Null
            search_place.setText("" + user_search_place);
        }else{
            search_place.setText("People nearby");
        }


        final ImageView iv1 = (ImageView) dialogview.findViewById(R.id.dialog_cross_Id);
        final ImageView iv2 = (ImageView) dialogview.findViewById(R.id.dialog_tick_id);
        RangeBar rangeBar = (RangeBar) dialogview.findViewById(R.id.ww_RB_id);

        RelativeLayout near_by_RL_id = dialogview.findViewById(R.id.near_by_RL_id);


        near_by_RL_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchPlaces_A.class));
            }
        });


        final TextView text_age = dialogview.findViewById(R.id.text_age);
        final TextView distance = dialogview.findViewById(R.id.distance);
        SeekBar simpleSeekBar = dialogview.findViewById(R.id.simpleSeekBar);
        simpleSeekBar.setProgress(Integer.parseInt(Variables.defalut_max_distance));
        distance.setText("" + simpleSeekBar.getProgress() );
        dialog.setContentView(dialogview);

        if(handle_search != null){

            try{

                JSONObject search_obj = new JSONObject(handle_search);
                handle_search_gender = search_obj.getString("gender");
                handle_search_filter_by = search_obj.getString("filter_by");
                handle_search_age_min = search_obj.getString("age_min");
                handle_search_age_max = search_obj.getString("age_max");
                handle_search_distance = search_obj.getString("search_distance");

                text_age.setText(handle_search_age_min + " - " +handle_search_age_max );

                rangeBar.setRangePinsByValue( Float.parseFloat(handle_search_age_min) ,Float.parseFloat(handle_search_age_max));

            }catch (Exception n){
            }

            search_distance = handle_search_distance;
            search_age_min = handle_search_age_min;
            search_age_max = handle_search_age_max;
            search_gender = handle_search_gender;
            try{
                if(handle_search_distance != null){
                    simpleSeekBar.setProgress(Integer.parseInt(handle_search_distance));
                    distance.setText(handle_search_distance );
                }else{
                    distance.setText(Variables.defalut_max_distance);
                    simpleSeekBar.setProgress(Integer.parseInt(Variables.defalut_max_distance));
                }

            }catch (Exception b){

            }

            if(handle_search_gender.equals("female")){
                tv1.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv.setBackgroundResource(R.drawable.d_gray_border);
                tv2.setBackgroundResource(R.drawable.d_gray_border);
            }else if(handle_search_gender.equals("male")){
                tv1.setBackgroundResource(R.drawable.d_gray_border);
                tv.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv2.setBackgroundResource(R.drawable.d_gray_border);
            }else{

                tv1.setBackgroundResource(R.drawable.d_gray_border);
                tv.setBackgroundResource(R.drawable.d_gray_border);
                tv2.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            }



            if(handle_search_filter_by.equals("All")){

                tv4.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv5.setBackgroundResource(R.drawable.d_gray_border);
                tv6.setBackgroundResource(R.drawable.d_gray_border);

            }else if(handle_search_filter_by.equals("Online")){
                tv4.setBackgroundResource(R.drawable.d_gray_border);
                tv5.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv6.setBackgroundResource(R.drawable.d_gray_border);

            }else{
                tv4.setBackgroundResource(R.drawable.d_gray_border);
                tv5.setBackgroundResource(R.drawable.d_gray_border);
                tv6.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
            }

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
                }
            });

        }catch (Exception b){

        }


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv1.setBackgroundResource(R.drawable.d_gray_border);
                tv2.setBackgroundResource(R.drawable.d_gray_border);
                add_gender(tv);

            }
        });


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv.setBackgroundResource(R.drawable.d_gray_border);
                tv2.setBackgroundResource(R.drawable.d_gray_border);
                add_gender(tv1);
            }
        });


        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setBackgroundResource(R.drawable.d_round_blue_border_radius8);
                tv.setBackgroundResource(R.drawable.d_gray_border);
                tv1.setBackgroundResource(R.drawable.d_gray_border);
                add_gender(tv2);
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

                Variables.is_searched=1;

                get_Near_by_Users();
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




    public void init_adp(){

        nearby_list.clear();
        adapter = new Card_Adapter(context, 0, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {

                if(view.getId()==R.id.right_overlay){
                    swipeRight();
                }
                else if(view.getId()==R.id.left_overlay){

                    swipe_left();
                }
            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        }, nearby_list);



    }


    public void swipe_left(){
        View target = cardStackView.getTopView();
        View targetOverlay = cardStackView.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(400);
        translateY.setStartDelay(400);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardStackView.swipe(SwipeDirection.Left, cardAnimationSet, overlayAnimationSet);

    }


    public void swipeRight() {

        View target = cardStackView.getTopView();
        View targetOverlay = cardStackView.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(400);
        translateY.setStartDelay(400);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardStackView.swipe(SwipeDirection.Right, cardAnimationSet, overlayAnimationSet);
    }



    public void save_token_firebase (final String token){

        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();;
        // todo: Get User ID from Shared Prefrence:

        final String user_id = Functions.get_info(context,"fb_id");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh");
        final String formattedDate = df.format(c);

        rootref.child("Users").child(user_id).child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map mymap=new HashMap<>();
                    mymap.put("token","" + token);
                    rootref.child("Users").child(user_id).updateChildren(mymap);

                }else {
                    Map mymap=new HashMap<>();
                    mymap.put("token","" + token);
                    rootref.child("Users").child(user_id).setValue(mymap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void get_user_info(String user_id){

        Functions.get_user_info("" + user_id,context);

    }
}
