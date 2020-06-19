package com.qboxus.hugme.Chat_pkg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.qboxus.hugme.All_Activities.Chat_A;
import com.qboxus.hugme.All_Activities.MainActivity;
import com.qboxus.hugme.All_Fragments.User_likes_F;
import com.qboxus.hugme.All_Model_Classes.Inbox_Get_Set_List;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Fragment_Callback;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Model_Classes.Match_Model;
import com.qboxus.hugme.All_Fragments.Main_F;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.InAppSubscription.InApp_Subscription_A;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Utils.OnSwipeTouchListener;
import com.qboxus.hugme.Utils.SimpleGestureFilter;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.qboxus.hugme.Chat_pkg.Expand_list_Adp.listTitleTextView;
import static com.qboxus.hugme.Shared_pref.SharedPrefrence.share_filter_inbox_key;

public class Inbox_F extends RootFragment implements View.OnClickListener{
    private SimpleGestureFilter detector;
    View v;
    RecyclerView match_recyclerview, inbox_recyclerview;
    public static Match_Adapter adp;

    public static ExpandableListView ex;
    Expand_list_Adp list_adp;
    Context context;
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    Inbox_Adapter adapter;
    public static List<Match_Model> List_MyMatch = new ArrayList<>();
    TextView no_my_match;

    DatabaseReference rootref;
    ArrayList<Inbox_Get_Set_List> inbox_arraylist;
    ProgressBar progress_loader;

    Expand_list_Adp.CallBack_click callback;

    String inbox_filter_key ;
    TextView no_record;


    SimpleDraweeView likes_image;
    TextView likes_count_txt;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_inbox, null);

        context = getContext();
        match_recyclerview =  v.findViewById(R.id.match_recyclerview);
        inbox_recyclerview =  v.findViewById(R.id.inbox_recyclerview);
        adp = new Match_Adapter(context,List_MyMatch);
        no_my_match = v.findViewById(R.id.no_my_match);
        progress_loader = v.findViewById(R.id.progress_loader);
        Main_F.tb.setVisibility(View.GONE); // todo: Display ToolBar in MAIN_F
        no_record = v.findViewById(R.id.no_record);


        match_recyclerview.setClipToPadding(false);
        match_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        match_recyclerview.setHasFixedSize(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        inbox_recyclerview.setHasFixedSize(false);
        inbox_recyclerview.setLayoutManager(linearLayoutManager);


        match_recyclerview.setAdapter(adp);

        My_Match();

        ex = (ExpandableListView) v.findViewById(R.id.expandable_layout);
        ex.setClickable(true);

        RelativeLayout Main_RL = v.findViewById(R.id.Main_RL);
        setexpandlist();


        inbox_recyclerview.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {

                ex.collapseGroup(0);
            }
            public void onSwipeRight() {

            }
            public void onSwipeLeft() {
                }
            public void onSwipeBottom() {
                ex.expandGroup(0);
            }

        });


        ex.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
                 ex.collapseGroup(0);
            }
            public void onSwipeRight() {

            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
                ex.expandGroup(0);
            }

        });



        likes_image=v.findViewById(R.id.likes_image);
        likes_count_txt=v.findViewById(R.id.likes_count_txt);


        v.findViewById(R.id.likes_count_layout).setOnClickListener(this);


        display_inbox();



        return v;
    }

    private void setexpandlist(){

        setchilditems();
        setGroupParents();

        list_adp = new Expand_list_Adp(getActivity(), parentItems, childItems, new Expand_list_Adp.CallBack_click() {
            @Override
            public void Sort_by_date_new(String child) {

                SharedPrefrence.save_string(context,"" + child,"" + share_filter_inbox_key);

                setchilditems();


                if(child.equals("Date")){

                  Sort_by_date();
                }else if(child.equals("Favorites")){

                    Sort_by_like();
                }else if(child.equals("Visits")){

                    Sort_by_visits();
                }else if(child.equals("Chats")){

                    Sort_by_date();
                }

            }
        });

        ex.setAdapter(list_adp);


        ex.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {



                return false;
            }
        });

    }

    private void setchilditems() {

        childItems.clear();

        String local_filter = SharedPrefrence.get_string(context, SharedPrefrence.share_filter_inbox_key);
        if(local_filter == null){
            local_filter="All Connections";
        }

        ArrayList<String> child = new ArrayList<String>();

            child.add("All Connections");
            child.add("Online");
            child.add("Visits");
            child.add("Date");
            child.add("Favorites");

            child.remove(local_filter);

        childItems.add(child);
    }

    private void setGroupParents() {
        parentItems.clear();
        parentItems.add("All Connections");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.likes_count_layout:
                if(MainActivity.purduct_purchase)
                open_user_list();
                else
                    open_subscription_view();
                break;
        }

    }


    public void open_subscription_view(){

        InApp_Subscription_A inApp_subscription_a = new InApp_Subscription_A();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.Main_F, inApp_subscription_a)
                .addToBackStack(null)
                .commit();

    }

    public void open_user_list(){

        User_likes_F user_likes_f = new User_likes_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {
                My_Match();
            }
        },false);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("like_count",likes_count_txt.getText().toString());
        user_likes_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Main_F, user_likes_f).commit();

    }


    // API to get All My Match
    public void My_Match(){
        String fb_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);

        Functions.Show_loader(context,false,false);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fb_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(
                context,
                "" + Api_Links.myMatch,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String response) {

                        Functions.cancel_loader();

                        Parse_match_data(response);

                    }
                }


        );



    }


    public void display_inbox(){

        String user_id = Functions.get_info(context,"fb_id");
        String first_name = Functions.get_info(context,"first_name");
        String user_pic = Functions.get_info(context,"image1");



        Variables.user_id = user_id;
        Variables.user_name = first_name;
        Variables.user_pic= user_pic;
        rootref = FirebaseDatabase.getInstance().getReference();
        inbox_arraylist=new ArrayList<>();

       adapter = new Inbox_Adapter(context, inbox_arraylist, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {

                Inbox_Get_Set_List inbox_get_set_list = (Inbox_Get_Set_List)Model;


                Intent myIntent = new Intent(context, Chat_A.class);
                myIntent.putExtra("receiver_id", inbox_get_set_list.getRid());
                myIntent.putExtra("receiver_name", inbox_get_set_list.getName());
                myIntent.putExtra("receiver_pic", inbox_get_set_list.getPic());
                myIntent.putExtra("is_block", inbox_get_set_list.getBlock());
                myIntent.putExtra("match_api_run", "0");
                context.startActivity(myIntent);
            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

                inbox_recyclerview.setAdapter(adapter);



        user_id = SharedPrefrence.get_social_info(context,SharedPrefrence.u_login_detail,"fb_id");

        ValueEventListener eventListener;
        Query inbox_query;
        progress_loader.setVisibility(View.VISIBLE);
        inbox_query = rootref.child("Inbox").child(user_id).orderByChild("timestamp");
        inbox_query.keepSynced(true);

        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();
                progress_loader.setVisibility(View.GONE);

                if(!dataSnapshot.exists()){
                  } else {

                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        Inbox_Get_Set_List model = ds.getValue(Inbox_Get_Set_List.class);

                        if(model.getLike() != null){
                            if(model.getLike().equals("1")){
                                // If Like
                                inbox_arraylist.add(0,model);
                                adapter.notifyDataSetChanged();
                            }else{
                                inbox_arraylist.add(model);
                                adapter.notifyDataSetChanged();
                            }

                        }

                }

                }


                if(inbox_arraylist.size()==0){
                    show_norecord_text();
                }else{
                    v.findViewById(R.id.no_record).setVisibility(View.GONE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inbox_query.addValueEventListener(eventListener);




    }

    public void show_norecord_text(){
           v.findViewById(R.id.no_record).setVisibility(View.VISIBLE);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {


            My_Match();


            Variables.Var_tab_change = 1; // todo: one

            inbox_filter_key = SharedPrefrence.get_string(context,"" + share_filter_inbox_key);

            if(listTitleTextView != null){
                listTitleTextView.setText("" + inbox_filter_key);
            }


            if(inbox_filter_key != null){
                   if(inbox_filter_key.equals("Date")){
                    Sort_by_date();
                }else if(inbox_filter_key.equals("Favorites")){

                    Sort_by_like();
                }else if(inbox_filter_key.equals("Visits")){
                    Sort_by_visits();
                }else if(inbox_filter_key.equals("Chats")){
                    Sort_by_date();
                }
            }



        }


    }

    public void Parse_match_data(String loginData){
        try {
            JSONObject jsonObject=new JSONObject(loginData);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                List_MyMatch.clear();
                JSONArray msg=jsonObject.getJSONArray("msg");
                for (int i=0; i<msg.length();i++){
                    JSONObject userdata=msg.getJSONObject(i);
                    JSONObject username_obj=userdata.getJSONObject("effect_profile_name");

                    Match_Model model = new Match_Model(
                            "" + userdata.getString("effect_profile"),
                            "" + username_obj.getString("image1"),
                            "" + username_obj.getString("first_name")+" "+username_obj.getString("last_name")
                    );


                    List_MyMatch.add(model);
                }
                adp.notifyDataSetChanged();

                 if(List_MyMatch.isEmpty()){
                    no_my_match.setVisibility(View.VISIBLE);
                }else {

                    no_my_match.setVisibility(View.GONE);
                }



                JSONObject myLikes=jsonObject.optJSONObject("myLikes");
                if(myLikes!=null){
                    int count=myLikes.optInt("total");
                    String image1=myLikes.optString("image1");

                    if(count>0){
                        likes_count_txt.setText(count+" Likes");

                        if(image1!=null && !image1.equals("")) {
                            Uri uri = Uri.parse(image1);
                            likes_image.setImageURI(uri);
                        }


                        v.findViewById(R.id.likes_count_layout).setVisibility(View.VISIBLE);
                        v.findViewById(R.id.likes_count_layout).setOnClickListener(this);

                    }

                    else {
                        v.findViewById(R.id.likes_count_layout).setVisibility(View.GONE);
                    }
                }


            }
        } catch (JSONException e) {
            Functions.toast_msg(context,"" + e.toString());
            e.printStackTrace();
        }


    }


    // Method to sort by Date
    public void Sort_by_date(){
         String user_id = SharedPrefrence.get_social_info(context,SharedPrefrence.u_login_detail,"fb_id");

        ValueEventListener eventListener;
        Query inbox_query;
        progress_loader.setVisibility(View.VISIBLE);
        inbox_query = rootref.child("Inbox").child(user_id).orderByChild("sort");

        inbox_query.keepSynced(true);

        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();
                progress_loader.setVisibility(View.GONE);

                if(!dataSnapshot.exists()){
                  } else {
                     LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);

                    inbox_recyclerview.setLayoutManager(linearLayoutManager);

                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        Inbox_Get_Set_List model = ds.getValue(Inbox_Get_Set_List.class);
                    inbox_arraylist.add(model);
                        adapter.notifyDataSetChanged();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inbox_query.addValueEventListener(eventListener);




    }

    public static void Like_chat (final String Receiverid, Context context, final String is_like){

         final Date c = Calendar.getInstance().getTime();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference inbox_change_status_1=reference.child("Inbox").child(Variables.user_id+"/"+Receiverid);

        inbox_change_status_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("rid").getValue().equals(Receiverid)){
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("like",  is_like);
                        inbox_change_status_1.updateChildren(result);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    // Method to get data Order by like
    public void Sort_by_like(){
          String user_id = SharedPrefrence.get_social_info(context,SharedPrefrence.u_login_detail,"fb_id");

        ValueEventListener eventListener;
        Query inbox_query;
        progress_loader.setVisibility(View.VISIBLE);
        inbox_query = rootref.child("Inbox").child(user_id).orderByChild("timestamp");
        inbox_query.keepSynced(true);

        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();
                progress_loader.setVisibility(View.GONE);

                if(!dataSnapshot.exists()){
                  } else {

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setReverseLayout(false);
                    linearLayoutManager.setStackFromEnd(false);

                    inbox_recyclerview.setLayoutManager(linearLayoutManager);


                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        Inbox_Get_Set_List model = ds.getValue(Inbox_Get_Set_List.class);
                         if(model.getLike().equals("1")){
                             inbox_arraylist.add(0,model);
                            adapter.notifyDataSetChanged();
                        }else{
                            inbox_arraylist.add(model);
                            adapter.notifyDataSetChanged();
                        }




                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inbox_query.addValueEventListener(eventListener);





    }

    // Method to sort by Msg Read
    public void Sort_by_visits(){
               String user_id = SharedPrefrence.get_social_info(context,SharedPrefrence.u_login_detail,"fb_id");

        ValueEventListener eventListener;
        Query inbox_query;
        progress_loader.setVisibility(View.VISIBLE);
        inbox_query = rootref.child("Inbox").child(user_id).orderByChild("timestamp");
        inbox_query.keepSynced(true);

        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();
                progress_loader.setVisibility(View.GONE);

                if(!dataSnapshot.exists()){
                 } else {

                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        Inbox_Get_Set_List model = ds.getValue(Inbox_Get_Set_List.class);
                        //    Functions.toast_msg(context,"Okk " + model.getLike());
                        if(model.getStatus().equals("0")){
                            // If msg not Read
                            inbox_arraylist.add(0,model);
                            adapter.notifyDataSetChanged();
                        }else{
                            // If msg read
                            inbox_arraylist.add(model);
                            adapter.notifyDataSetChanged();
                        }




                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inbox_query.addValueEventListener(eventListener);





    }



}
