package com.qboxus.hugme.All_Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.qboxus.hugme.All_Adapters.User_Like_Adapter;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.Bottom_Sheet.View_Profile_Bottom_Swipe;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Fragment_Callback;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.ApiRequest;
import com.qboxus.hugme.Volley_Package.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_likes_F extends RootFragment{

    View view;
    Context context;

    ArrayList<Get_Set_Nearby> data_list;
    RecyclerView recyclerView;
    User_Like_Adapter adapter;

    String likes_count;
    TextView title_txt;
    ProgressBar progress_bar;
    DatabaseReference rootref;

    Boolean is_view_created=false;

    public User_likes_F() {
    }

    Fragment_Callback fragment_callback;
    Boolean is_from_tab=false;
    public User_likes_F(Fragment_Callback fragment_callback, Boolean is_from_tab) {
      this.fragment_callback=fragment_callback;
      this.is_from_tab=is_from_tab;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user_list, container, false);
        context=getContext();


        rootref= FirebaseDatabase.getInstance().getReference();



        progress_bar=view.findViewById(R.id.progress_bar);

        Bundle bundle = getArguments();
        if (bundle != null) {
            likes_count = bundle.getString("like_count");
        }

        title_txt=view.findViewById(R.id.title_txt);
        title_txt.setText(likes_count);

        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fragment_callback!=null){
                    fragment_callback.Responce(null);
                }
                getActivity().onBackPressed();
            }
        });


        data_list=new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));

        recyclerView.setHasFixedSize(true);

        adapter=new User_Like_Adapter(context, data_list, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {
                open_user_detail((Get_Set_Nearby) Model);
            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

        recyclerView.setAdapter(adapter);


        if(is_from_tab){

            view.findViewById(R.id.toolbar).setVisibility(View.GONE);

            view.findViewById(R.id.cardview).
                    setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }

        else {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);

            view.findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

            GetPeople_nearby();
        }

        is_view_created=true;
        return view;
    }



    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN |ItemTouchHelper.UP) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Toast.makeText(context, "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

            return super.getSwipeDirs(recyclerView, viewHolder);

        }
        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

            Log.d("resp",""+swipeDir);

            int position = viewHolder.getAdapterPosition();

            Get_Set_Nearby item=data_list.get(position);
            data_list.remove(position);


            if(swipeDir==8){
                updatedata_onrightSwipe(item);
            }else if(swipeDir==4){
                updatedata_onLeftSwipe(item);
            }

            adapter.notifyItemRemoved(position);
            adapter.notifyItemChanged(position);

        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            super.onChildDraw(c, recyclerView, viewHolder, dX,
                    dY, actionState, isCurrentlyActive);
            Log.d("resp",""+dX);

            if(dX<0.0){
                viewHolder.itemView.findViewById(R.id.left_overlay).setVisibility(View.VISIBLE);
                viewHolder.itemView.findViewById(R.id.right_overlay).setVisibility(View.GONE);
            }else if(dX>0.0) {
                viewHolder.itemView.findViewById(R.id.left_overlay).setVisibility(View.GONE);
                viewHolder.itemView.findViewById(R.id.right_overlay).setVisibility(View.VISIBLE);

            }else {
                viewHolder.itemView.findViewById(R.id.left_overlay).setVisibility(View.GONE);
                viewHolder.itemView.findViewById(R.id.right_overlay).setVisibility(View.GONE);
            }
        }
    };



    private void GetPeople_nearby() {

        progress_bar.setVisibility(View.VISIBLE);
        String user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Api_Links.mylikies, parameters, new CallBack() {
            @Override
            public void Get_Response(String requestType, String response) {
                progress_bar.setVisibility(View.GONE);

                Parse_user_info(response);
            }
        });


    }


    public void Parse_user_info(String loginData){
        try {
            JSONObject jsonObject=new JSONObject(loginData);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                data_list.clear();
                JSONArray msg=jsonObject.getJSONArray("msg");

                for (int i=0; i<msg.length();i++){
                    JSONObject userdata=msg.getJSONObject(i);
                    Get_Set_Nearby item=new Get_Set_Nearby();
                    item.setFb_id(userdata.optString("fb_id"));
                    item.setFirst_name(userdata.optString("first_name"));
                    item.setLast_name(userdata.optString("last_name"));
                    item.setJob_title(userdata.optString("job_title"));
                    item.setCompany(userdata.optString("company"));
                    item.setSchool(userdata.optString("school"));
                    item.setBirthday(userdata.optString("birthday"));
                    item.setAbout_me(userdata.optString("about_me"));
                    item.setDistance(userdata.optString("distance"));
                    item.setGender(userdata.optString("gender"));
                    item.setSwipe(userdata.optString("swipe"));

                    item.setImage1(userdata.optString("image1"));
                    item.setImage2(userdata.optString("image2"));
                    item.setImage3(userdata.optString("image3"));
                    item.setImage4(userdata.optString("image4"));
                    item.setImage5(userdata.optString("image5"));
                    item.setImage6(userdata.optString("image6"));



                    data_list.add(item);
                }

                if(data_list.isEmpty()){
                    view.findViewById(R.id.nodata_found_txt).setVisibility(View.VISIBLE);
                }else {
                    view.findViewById(R.id.nodata_found_txt).setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            view.findViewById(R.id.nodata_found_txt).setVisibility(View.VISIBLE);

        }


    }



    public  void updatedata_onLeftSwipe (final Get_Set_Nearby item){


        try{

            Functions.Count_num_click(context);


            Functions.display_fb_ad(context);
        }catch (Exception b){

            Functions.toast_msg(context,"" + b.toString());

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


    public  void updatedata_onrightSwipe (final Get_Set_Nearby item){
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


    public void open_user_detail(Get_Set_Nearby item){

        Bundle bundle_user_profile = new Bundle();
        bundle_user_profile.putString("user_id", "" + item.getFb_id());

        bundle_user_profile.putSerializable("user_near_by", item);

        View_Profile_Bottom_Swipe view_profile = new View_Profile_Bottom_Swipe();
        view_profile.setArguments(bundle_user_profile);
        view_profile.setCancelable(true);
        view_profile.show(getFragmentManager(), view_profile.getTag());


    }

}
