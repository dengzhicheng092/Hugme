package com.qboxus.hugme.All_Activities;

import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.qboxus.hugme.All_Adapters.Msg_adapter;
import com.qboxus.hugme.All_Adapters.Blocked_User_adapter;
import com.qboxus.hugme.All_Model_Classes.Inbox_Get_Set_List;
import com.qboxus.hugme.All_Model_Classes.Chat_Data_Model;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;

public class Blocked_User_A extends AppCompatActivity {

    RecyclerView Msg_RV;
    Blocked_User_adapter adapter;
    Context context;
    ArrayList<Inbox_Get_Set_List> inbox_arraylist;
    DatabaseReference rootref;
    ImageView back_id;
    int i=0;
    ProgressBar progress_loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_user);
        context = Blocked_User_A.this;
        progress_loader = findViewById(R.id.loader);

        Msg_RV  = findViewById(R.id.chatlist);

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


       adapter = new Blocked_User_adapter(context, inbox_arraylist, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {
            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

                    Inbox_Get_Set_List inbox_get_set_list = (Inbox_Get_Set_List)Model;


                chatFragment(inbox_get_set_list.getRid(),inbox_get_set_list.getName(),inbox_get_set_list.getPic(),inbox_get_set_list.getBlock());

            }
        });

                Msg_RV.setAdapter(adapter);
    }


    ValueEventListener eventListener;
    Query inbox_query;
    @Override
    public void onStart() {
        super.onStart();

        if(inbox_arraylist.size()==0){
            show_norecord_text();
        }

        progress_loader.setVisibility(View.VISIBLE);
        inbox_query = rootref.child("Inbox").child(Variables.user_id).orderByChild("timestamp");
        inbox_query.keepSynced(true);

        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();
                progress_loader.setVisibility(View.GONE);

                if(!dataSnapshot.exists()){

                    if(inbox_arraylist.size()==0){
                        show_norecord_text();
                    }else{
                        findViewById(R.id.no_record).setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();


                }else {

                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        ds.child("block").getValue();
                        if(ds.child("block").getValue().equals("1")){
                           i = i+1;
                            Inbox_Get_Set_List model = ds.getValue(Inbox_Get_Set_List.class);
                            inbox_arraylist.add(model);
                            adapter.notifyDataSetChanged();

                            if(inbox_arraylist.size()==0){
                                show_norecord_text();
                            }else{
                                findViewById(R.id.no_record).setVisibility(View.GONE);
                            }

                        }else{
                            // Not Blocked
                        }
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

    public void chatFragment(final String receiverid, final String name, String receiver_pic, final String is_block){


        final CharSequence[] options = {"Un Block", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Un Block")) {
                    dialog.dismiss();
                    Block_User_from_Chat(receiverid, is_block);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });


        builder.show();




    }


    // Method to Block the user From Inbox_F
    public void Block_User_from_Chat (final String Receiverid, String is_block){


        final Date c = Calendar.getInstance().getTime();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference inbox_change_status_1=reference.child("Inbox").child(Variables.user_id+"/"+Receiverid);

        if(is_block.equals("0")){

            is_block = "1";

        }else if(is_block.equals("1")){
            is_block = "0";

        }


        final String finalIs_block = is_block;
        inbox_change_status_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("rid").getValue().equals(Receiverid)){
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("block", "" + finalIs_block);
                        inbox_change_status_1.updateChildren(result);
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }// End Method to Block the User


    public void show_norecord_text(){
        findViewById(R.id.no_record).setVisibility(View.VISIBLE);
    }

}
