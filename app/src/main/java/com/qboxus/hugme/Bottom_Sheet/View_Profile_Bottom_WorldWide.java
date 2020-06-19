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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import com.qboxus.hugme.All_Activities.Chat_A;
import com.qboxus.hugme.All_Activities.Share_Profile_A;
import com.qboxus.hugme.All_Fragments.World_wide_native_ads_F;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Adapters.Images_Adp;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Images;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.R;

import static com.qboxus.hugme.All_Fragments.World_wide_native_ads_F.recyclerView;
import static com.qboxus.hugme.All_Fragments.World_wide_native_ads_F.nearby_list;

public class View_Profile_Bottom_WorldWide extends BottomSheetDialogFragment implements View.OnClickListener {

    View view;

    TextView distance, about_me_tv_id, simple_about;

    List<Get_Set_Images> list_profile_img;
    ArrayList<String> about_me = new ArrayList<String>();
    public View_Profile_Bottom_WorldWide() {
    }
    String user_id;
    RecyclerView RV_images_list;
    Images_Adp adp_img;
    Context context;
    ImageView like;
    ImageView  move_left, move_right;
    SimpleDraweeView iv;
    FlowLayout item;

    BottomSheetBehavior behavior;
    Get_Set_Nearby get_nearby;

    FrameLayout bottomSheet;

    String swipe_pos_string;
    int swipe_position;
    String view_type;


    ArrayList<Integer> about_me_pics = new ArrayList<Integer>();
    TextView user_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_view_profile_ww, container, false);
        context = getContext();

        user_id = getArguments().getString("user_id");
        swipe_pos_string = getArguments().getString("current_position");
        view_type =  getArguments().getString("view_type");



        swipe_position = World_wide_native_ads_F.swipe_position;


        user_name = view.findViewById(R.id.user_name);



        distance = view.findViewById(R.id.distance);
        about_me_tv_id = view.findViewById(R.id.about_me_tv_id);
        simple_about = view.findViewById(R.id.simple_about);

        like = (ImageView) view.findViewById(R.id.right_overlay);
        list_profile_img = new ArrayList<>();
        RV_images_list = view.findViewById(R.id.RV_images_list);
        iv = view.findViewById(R.id.profile_iv_id);




        move_left = view.findViewById(R.id.move_left);
        move_right = view.findViewById(R.id.move_right);

        get_nearby = (Get_Set_Nearby) getArguments().getSerializable("user_near_by");


        view.findViewById(R.id.chat_btn).setOnClickListener(this);
        view.findViewById(R.id.fl_id).setOnClickListener(this::onClick);
        view.findViewById(R.id.report_user).setOnClickListener(this::onClick);
        view.findViewById(R.id.share_profile).setOnClickListener(this::onClick);



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
                       // user_name.setVisibility(View.VISIBLE);
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


        Fill_data(get_nearby);


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

                dialog.getWindow()
                        .findViewById(R.id.design_bottom_sheet)
                        .setBackgroundResource(android.R.color.transparent);

                behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int i) {
                        if (i == BottomSheetBehavior.STATE_DRAGGING){
                            recyclerView.smoothScrollToPosition(swipe_position);
                            item.removeAllViews();
                            dismiss();
                        }

                        if (i == BottomSheetBehavior.STATE_COLLAPSED){
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            dismiss();
                            recyclerView.smoothScrollToPosition(swipe_position);
                            item.removeAllViews();
                        }

                        if (i == BottomSheetBehavior.STATE_HIDDEN){
                            recyclerView.smoothScrollToPosition(swipe_position);
                            item.removeAllViews();
                            dismiss();
                        }

                    }

                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                });

                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.removeAllViews();
                        try{
                            nearby_list.remove(swipe_position);
                            World_wide_native_ads_F.adapter.notifyItemRemoved(swipe_position);
                            World_wide_native_ads_F.adapter.notifyItemRangeChanged(swipe_position, nearby_list.size());
                            World_wide_native_ads_F.adapter.notifyDataSetChanged();

                        }catch (Exception b){
                            Functions.toast_msg(context,"Removed Error " + b.toString());
                        }


                        int list_size = nearby_list.size()-1;
                        if(swipe_position == list_size ){
                            swipe_position = 0;
                        }
                        swipe_position = swipe_position + 1;
                        if(nearby_list.get(swipe_position) instanceof Get_Set_Nearby){

                            try{
                                get_nearby = (Get_Set_Nearby) nearby_list.get(swipe_position);
                            }catch (Exception b){
                            }

                        } else{
                            swipe_position = swipe_position + 1;

                            get_nearby = (Get_Set_Nearby) nearby_list.get(swipe_position);
                        }

                        World_wide_native_ads_F.updatedata_onrightSwipe(getContext(),get_nearby);

                        Fill_data(get_nearby);

                    }
                });





                move_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        item.removeAllViews();
                        int list_size = nearby_list.size()-1;
                        if(swipe_position == list_size ){
                            swipe_position = 0;
                        }

                        swipe_position = swipe_position + 1;


                        if(nearby_list.get(swipe_position) instanceof Get_Set_Nearby){

                            try{
                                get_nearby = (Get_Set_Nearby) nearby_list.get(swipe_position);
                            }catch (Exception b){
                            }

                        } else{
                            swipe_position = swipe_position + 2;

                        try{
                            get_nearby = (Get_Set_Nearby) nearby_list.get(swipe_position);
                        }catch (Exception b){}
                        }


                        Fill_data(get_nearby);

                    }
                });


                move_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        item.removeAllViews();

                        int list_size = nearby_list.size()-1;


                        if(swipe_position == 0){
                            swipe_position = list_size;
                        }else{
                            swipe_position = swipe_position - 1;
                        }

                        if(nearby_list.get(swipe_position) instanceof Get_Set_Nearby){

                            try{
                                get_nearby = (Get_Set_Nearby) nearby_list.get(swipe_position);
                            }catch (Exception b){
                            }

                        } else{
                            if(swipe_position == 0){
                                swipe_position = list_size;
                            }else{
                                swipe_position = swipe_position - 1;
                            }

                           get_nearby = (Get_Set_Nearby) nearby_list.get(swipe_position);

                        }

                        Fill_data(get_nearby);


                    }
                });

            }
        });







        return view;
    }



    public void Fill_data(Get_Set_Nearby get_nearby){

        about_me_tv_id.setText("" + get_nearby.getAbout_me());
        distance.setText("" + get_nearby.getDistance());
        user_name.setText("" + get_nearby.getFirst_name() + ", " + get_nearby.getBirthday() + " \n " + get_nearby.getDistance());

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


        if(!get_nearby.getChildren().equals("")){
            about_me.add(get_nearby.getChildren());
            about_me_pics.add(R.drawable.ic_childrens);

        }

        if(!get_nearby.getLiving().equals("")){
            about_me.add(get_nearby.getLiving());
            about_me_pics.add(R.drawable.ic_living);
        }

        if(!get_nearby.getRelationship().equals("")){
            about_me.add(get_nearby.getRelationship());
            about_me_pics.add(R.drawable.ic_relationship);

        }

        if(!get_nearby.getSchool().equals("")){
            about_me.add(get_nearby.getSchool());
            about_me_pics.add(R.drawable.ic_living);

        }

        if(!get_nearby.getChildren().equals("")){
            about_me.add(get_nearby.getChildren());
            about_me_pics.add(R.drawable.ic_childrens);
        }

        if(!get_nearby.getSexuality().equals("")){
            about_me.add(get_nearby.getSexuality());
            about_me_pics.add(R.drawable.ic_sexuality);
        }

        if(!get_nearby.getSmoking().equals("")){
            about_me.add(get_nearby.getSmoking());
            about_me_pics.add(R.drawable.ic_smoking);

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

        final GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        RV_images_list.setLayoutManager(layoutManager);


        RV_images_list.setAdapter(adp_img);

        try{
            Uri uri = Uri.parse(get_nearby.getImage1());
            iv.setImageURI(uri);  // Attachment
            iv.getHierarchy().setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.profile_image_placeholder));

        }catch (Exception v9){
        }
        inflate_layout();

    }

    // End Method to inflate Layout
    public void inflate_layout(){

        item = view.findViewById(R.id.inflate_layout);
        for(int i=0; i< about_me.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.item_intrest, null);//child.xml

            TextView intrest_name = child.findViewById(R.id.intrest_name);
            ImageView image = child.findViewById(R.id.ic_image);
            LinearLayout main_linear_layout = child.findViewById(R.id.main_linear_layout);

            if(about_me.get(i).equals("0") || about_me.get(i).equals(" ")){
            }else{
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.chat_btn:
                Intent myIntent = new Intent(context, Chat_A.class);
                myIntent.putExtra("receiver_id", get_nearby.getFb_id());
                myIntent.putExtra("receiver_name", get_nearby.getFirst_name()+" "+get_nearby.getLast_name());
                myIntent.putExtra("receiver_pic", get_nearby.getImage1());
                myIntent.putExtra("is_block", "0");
                myIntent.putExtra("match_api_run", "0");
                context.startActivity(myIntent);
                break;

            case R.id.fl_id:
                try{
                    recyclerView.smoothScrollToPosition(swipe_position);
                }catch (Exception b){

                }
                dismiss();
                break;


            case R.id.report_user:
                Functions.report_user(context,"" + get_nearby.getFb_id());
                break;


            case R.id.share_profile:
                String  user_image =  get_nearby.getImage1();

                String user_name = get_nearby.getFirst_name()
                        +" "+ get_nearby.getLast_name();

                Intent intent=new Intent(getActivity(), Share_Profile_A.class);
                intent.putExtra("name",user_name);
                intent.putExtra("image",user_image);

                startActivity(intent);
                break;
        }
    }


}
