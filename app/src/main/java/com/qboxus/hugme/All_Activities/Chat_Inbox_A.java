package com.qboxus.hugme.All_Activities;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.qboxus.hugme.Chat_pkg.Inbox_Adapter;
import com.qboxus.hugme.All_Adapters.Msg_adapter;
import com.qboxus.hugme.All_Model_Classes.Inbox_Get_Set_List;
import com.qboxus.hugme.All_Model_Classes.Chat_Data_Model;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;
//import com.com.olx.Utils.EdgeChanger;

public class Chat_Inbox_A extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseDatabase fire_db = FirebaseDatabase.getInstance();
    List<Chat_Data_Model> Msg_list = new ArrayList<>();
    RecyclerView Msg_RV;
    Msg_adapter msg_adp;
    String rec_user_id = "25"; // For Demo Only
    String sender_id = "34"; // For Demo Only
    Inbox_Adapter adapter;
    Context context;
    ArrayList<Inbox_Get_Set_List> inbox_arraylist;
    DatabaseReference rootref;
    Toolbar header;
    ImageView back_id;

    ProgressBar progress_loader;
    public void Change_Color_Dynmic (){
        try{
            header = findViewById(R.id.tb_id);
        }catch (Exception v){

        }

    }
    TextView no_record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_inbox);


        context = Chat_Inbox_A.this;
        progress_loader = findViewById(R.id.loader);

        Msg_RV  = findViewById(R.id.chatlist);
        no_record = findViewById(R.id.no_record);

        Msg_RV.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        // TODO: For Desc.
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        // TODO: For Divider in recyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Msg_RV.getContext(),
                linearLayoutManager.getOrientation());
        Msg_RV.addItemDecoration(dividerItemDecoration);

        Msg_RV.setLayoutManager(linearLayoutManager);

        back_id = findViewById(R.id.back_id);


        String user_id = Functions.get_info(context,"fb_id");
        String first_name = Functions.get_info(context,"first_name");
        String user_pic = Functions.get_info(context,"image1");


     //   Functions.toast_msg(context,"" + user_id + " " + first_name + " " + user_pic);

        Variables.user_id = user_id;
        Variables.user_name = first_name;
        Variables.user_pic= user_pic;
        rootref = FirebaseDatabase.getInstance().getReference();
        inbox_arraylist=new ArrayList<>();

        back_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        // inntialize the adapter and set the adapter to recylerview
        adapter = new Inbox_Adapter(context, inbox_arraylist, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {

                Inbox_Get_Set_List inbox_get_set_list = (Inbox_Get_Set_List)Model;


                chatFragment(inbox_get_set_list.getRid(),inbox_get_set_list.getName(),inbox_get_set_list.getPic(),inbox_get_set_list.getBlock());
            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

        Msg_RV.setAdapter(adapter);

        //  read_Msgs();

        Change_Color_Dynmic();

    }

    // on start we get the all inbox data from database
    ValueEventListener eventListener;
    Query inbox_query;
    @Override
    public void onStart() {
        super.onStart();


        progress_loader.setVisibility(View.VISIBLE);
        inbox_query = rootref.child("Inbox").child(Variables.user_id).orderByChild("timestamp");
        inbox_query.keepSynced(true);

        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();
                progress_loader.setVisibility(View.GONE);

                if(!dataSnapshot.exists()){
                   // Functions.toast_msg(context,"No Msgs");
                    //no_data_layout.setVisibility(View.VISIBLE);
                }else {
                    //no_data_layout.setVisibility(View.GONE);
                  //  Functions.toast_msg(context,"Msgs present ");

                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        Inbox_Get_Set_List model = ds.getValue(Inbox_Get_Set_List.class);
                        inbox_arraylist.add(model);
                        adapter.notifyDataSetChanged();
                    }

                }

                if(inbox_arraylist.size()==0){
                    // If size is Zero
                    show_norecord_text();
                }else{
                    findViewById(R.id.no_record).setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inbox_query.addValueEventListener(eventListener);
    }




    @Override
    public void onStop() {
        super.onStop();
        inbox_query.removeEventListener(eventListener);

    }

    public void chatFragment(String receiverid, String name,String receiver_pic, String is_block){

        // Open New Activity
        Intent myIntent = new Intent(Chat_Inbox_A.this, Chat_A.class);
        myIntent.putExtra("receiver_id", receiverid);
        myIntent.putExtra("receiver_name", name);
        myIntent.putExtra("receiver_pic", receiver_pic);
        myIntent.putExtra("is_block", is_block);
        myIntent.putExtra("match_api_run", "0");
        startActivity(myIntent);



    }
    public void show_norecord_text(){
        // Method to show no record TextView
        findViewById(R.id.no_record).setVisibility(View.VISIBLE);
    }


}
