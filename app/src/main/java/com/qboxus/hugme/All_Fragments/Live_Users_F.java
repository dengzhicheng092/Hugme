package com.qboxus.hugme.All_Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qboxus.hugme.All_Adapters.Live_user_Adapter;
import com.qboxus.hugme.All_Model_Classes.Live_user_Model;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.LiveStreaming.activities.StreamingMain_A;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

import java.util.ArrayList;

import io.agora.rtc.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class Live_Users_F extends RootFragment implements View.OnClickListener {


    View view;
    Context context;
    ArrayList<Live_user_Model> data_list;
    RecyclerView recyclerView;
    Live_user_Adapter adapter;

    DatabaseReference rootref;

    TextView no_data_found;
    public Live_Users_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_live_users, container, false);
        context=getContext();
        rootref= FirebaseDatabase.getInstance().getReference();

        data_list=new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));

        recyclerView.setHasFixedSize(true);

        adapter=new Live_user_Adapter(context, data_list, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {
                Live_user_Model live_user_model=(Live_user_Model) Model;
                Open_hugme_live(live_user_model.getUser_id(),
                        live_user_model.getUser_name(),live_user_model.getUser_picture(),Constants.CLIENT_ROLE_AUDIENCE);
            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

        recyclerView.setAdapter(adapter);


        view.findViewById(R.id.go_live_layout).setOnClickListener(this::onClick);

        Get_Data();

        no_data_found=view.findViewById(R.id.no_data_found);
        return  view;
    }

    ChildEventListener valueEventListener;
   public void Get_Data(){

       valueEventListener=new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Live_user_Model model = dataSnapshot.getValue(Live_user_Model.class);
               data_list.add(model);
               adapter.notifyDataSetChanged();
               no_data_found.setVisibility(View.GONE);

           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

               Live_user_Model model = dataSnapshot.getValue(Live_user_Model.class);

               for (int i=0;i<data_list.size();i++){
                   if(model.getUser_id().equals(data_list.get(i).getUser_id())){
                       data_list.remove(i);
                   }
               }
               adapter.notifyDataSetChanged();

               if(data_list.isEmpty()){
                   no_data_found.setVisibility(View.VISIBLE);
               }


           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       };


       rootref.child("LiveUsers").addChildEventListener(valueEventListener);
   }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(rootref!=null && valueEventListener!=null)
            rootref.child("LiveUsers").removeEventListener(valueEventListener);
    }

    public void Open_hugme_live(String user_id, String user_name, String user_image, int role){

        if(check_permissions()){

            Intent intent=new Intent(getActivity(), StreamingMain_A.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("user_name",user_name);
            intent.putExtra("user_picture",user_image);
            intent.putExtra("user_role", role);
            startActivity(intent);

        }
    }



    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, 2);
        }else {

            return true;
        }

        return false;
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.go_live_layout:
                String user_id = SharedPrefrence.get_string(context,SharedPrefrence.u_id);
                String user_name = Functions.get_info(context,"first_name")
                        +" "+Functions.get_info(context,"last_name");
                String user_image = Functions.get_info(context,"image1");

                Open_hugme_live(user_id,user_name,user_image,Constants.CLIENT_ROLE_BROADCASTER);
                break;
        }
    }
}
