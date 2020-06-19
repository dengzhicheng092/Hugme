package com.qboxus.hugme.All_Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.qboxus.hugme.All_Adapters.Gif_Adapter;
import com.qboxus.hugme.All_Adapters.Msg_adapter;
import com.qboxus.hugme.All_Model_Classes.Chat_GetSet;
import com.qboxus.hugme.All_Fragments.See_Full_Image_F;
import  com.qboxus.hugme.Chat_pkg.Videos.Chat_Send_file_Service;
import  com.qboxus.hugme.Chat_pkg.Videos.Play_Video_F;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import  com.qboxus.hugme.Code_Classes.Variables;
//import com.com.hugme.pro.EditProfile;
import  com.qboxus.hugme.R;
//import com.com.olx.Utils.EdgeChanger;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Video_Calling.VideoActivity;
import com.qboxus.hugme.Volley_Package.Api_Links;
import  com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;

import static com.qboxus.hugme.Chat_pkg.Inbox_F.List_MyMatch;
import static com.qboxus.hugme.Chat_pkg.Inbox_F.adp;

public class Chat_A extends AppCompatActivity implements View.OnClickListener {

    MediaType mediaType;
    EditText msg;
    RecyclerView recyclerView;
    private static final int CAMERA_REQUEST = 1888;

    public static SharedPreferences download_pref;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private List<Chat_GetSet> mChats=new ArrayList<>();
    ImageView iv  ;
    Context context;
    public static String token="null";
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    Msg_adapter adapter;

    String Receiver_name = "Rec Name";
    String Receiver_pic = "Image";
    String is_match_api_run, is_block;

    String remove_position_in_my_match;

    Query query_getchat;
    DatabaseReference rootref;
    String Receiverid = "890";
    ProgressBar p_bar;
    private DatabaseReference Adduser_to_inbox;
    private DatabaseReference mchatRef_reteriving;

    public static String uploading_image_id="none";
    ProgressDialog pd;
    String blocking_text;

    View view;

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(adapter !=null){
                adapter.notifyDataSetChanged();
            }

        }
    };


    TextView t_user_rec_name;

    Toolbar header;

    public void Change_Color_Dynmic (){
        try{
            header = findViewById(R.id.tb_id);
        }catch (Exception v){

        }

    }

    public void hide_sending_view(){

        upload_gif_btn.setVisibility(View.GONE);
        upload_stiker_btn.setVisibility(View.GONE);
        ImageButton uploadimagebtn = findViewById(R.id.uploadimagebtn);
        uploadimagebtn.setVisibility(View.GONE);
        send_btn.setImageDrawable( context.getResources().getDrawable(R.drawable.ic_msg_send_purple));


    }

    public void show_sending_view(){

        upload_gif_btn.setVisibility(View.VISIBLE);
        upload_stiker_btn.setVisibility(View.VISIBLE);
        ImageButton uploadimagebtn = findViewById(R.id.uploadimagebtn);
        uploadimagebtn.setVisibility(View.GONE);
        send_btn.setImageDrawable( context.getResources().getDrawable(R.drawable.ic_msg_send_gray));



    }


    LinearLayout gif_layout;
    ImageButton upload_stiker_btn,upload_gif_btn;
    ImageView send_btn;
    SimpleDraweeView userimage;
    TextView blocked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context= Chat_A.this;

        view = findViewById(R.id.Chat_F);
        send_btn = findViewById(R.id.send_btn);

        userimage = findViewById(R.id.userimage);

        blocked = findViewById(R.id.blocked);

        pd = new ProgressDialog(context);
        pd.setMessage(getResources().getString(R.string.loading));
        pd.setCancelable(false);


        findViewById(R.id.ic_more).setOnClickListener(this::onClick);

        gif_layout = findViewById(R.id.gif_layout);

        recyclerView = findViewById(R.id.chatlist);

        t_user_rec_name = findViewById(R.id.user_name);

        p_bar = findViewById(R.id.progress_bar);

        upload_gif_btn=findViewById(R.id.upload_gif_btn);
        upload_stiker_btn= findViewById(R.id.upload_stiker_btn);

        upload_gif_btn.setOnClickListener(this::onClick);
        upload_stiker_btn.setOnClickListener(this::onClick);



        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String user_id = Functions.get_info(context,"fb_id");
        String first_name = Functions.get_info(context,"first_name")+" "+Functions.get_info(context,"last_name");
        String user_pic = Functions.get_info(context,"image1");


        Variables.user_id = user_id;
        Variables.user_name = first_name;
        Variables.user_pic= user_pic;

        Intent intent = getIntent();
        Receiverid = intent.getStringExtra("receiver_id");
        Receiver_name = intent.getStringExtra("receiver_name");
        Receiver_pic = intent.getStringExtra("receiver_pic");

        try{

            is_match_api_run = intent.getStringExtra("match_api_run");
            remove_position_in_my_match = intent.getStringExtra("position_to_remove");
            is_block = intent.getStringExtra("is_block");


            if(is_block.equals("0")){
                blocking_text = "Block User";
            }else if(is_block.equals("1")){
                blocking_text = "Un Block User";
            }else if(is_block == null){
                blocking_text = "Block User";
            }else{
                blocking_text = "Block User";

            }


        }catch (Exception b){

        }

        if(Receiver_name.length()>14){

            Receiver_name = Receiver_name.substring(0,14) + " ...";
        }else{
            Receiver_name = Receiver_name;
        }

        t_user_rec_name.setText("" + Receiver_name);


        display_user_image(Receiver_pic, Receiver_name);

        get_latest_user_profile(Receiverid);


        findViewById(R.id.select_camera_2).setOnClickListener(this::onClick);
        findViewById(R.id.uploadimagebtn).setOnClickListener(this::onClick);
        download_pref = context.getSharedPreferences(Variables.download_pref,Context.MODE_PRIVATE);


        get_user_is_block_or_not();

        rootref = FirebaseDatabase.getInstance().getReference();
        Adduser_to_inbox=FirebaseDatabase.getInstance().getReference();

        iv = (ImageView) findViewById(R.id.back_id);

        view.findViewById(R.id.back_id).setOnClickListener(this::onClick);

        msg = findViewById(R.id.msgedittext);

        msg.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){

                if(msg.getText().toString().length() == 0){

                    send_btn.setEnabled(false);
                    show_sending_view();
                }else if(msg.getText().toString().length() > 0){
                    hide_sending_view();
                    send_btn.setEnabled(true);
                }
            }
        });


        send_btn.setOnClickListener(this::onClick);





        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Chat_A.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Msg_adapter(mChats, Variables.user_id, context, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {
                Chat_GetSet chat_getSet = (Chat_GetSet) Model;


                if(chat_getSet.getType().equals("image"))
                    OpenfullsizeImage(chat_getSet);

                if(chat_getSet.getType().equals("video")){

                    File fullpath = new File(Environment.getExternalStorageDirectory() +"/Chatbuzz/"+chat_getSet.chat_id+".mp4");
                    if(fullpath.exists()) {
                        OpenVideo(fullpath.getAbsolutePath());
                    }
                    else {
                        if(download_pref.getString(chat_getSet.getChat_id(),"").equals("")) {
                            long downlodid = Functions.DownloadData_forChat(context, chat_getSet);
                            download_pref.edit().putString(chat_getSet.getChat_id(), "" + downlodid).commit();
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

                recyclerView.setAdapter(adapter);


        view.findViewById(R.id.video_call_btn).setOnClickListener(this);
        view.findViewById(R.id.voice_call_btn).setOnClickListener(this::onClick);

        Change_Color_Dynmic();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.video_call_btn:
                if(!checkPermissionForCameraAndMicrophone()){

                    requestPermissionForCameraAndMicrophone();
                }else {
                    int video_call_time= SharedPrefrence.get_int(Chat_A.this,SharedPrefrence.video_calling_used_time);

                    if(video_call_time< Variables.max_video_calling_time) {
                        Functions.Show_Alert(this, "Alert", "For demo purpose we only allow to "+Variables.max_video_calling_time/1000+"s for video Call", new CallBack() {
                            @Override
                            public void Get_Response(String requestType, String response) {
                                Intent intent2 = new Intent(Chat_A.this, VideoActivity.class);
                                intent2.putExtra("id", Receiverid);
                                intent2.putExtra("name", Receiver_name);
                                intent2.putExtra("image", Receiver_pic);
                                intent2.putExtra("status", VideoActivity.Call_Send);
                                intent2.putExtra("call_type","video_call");
                                intent2.putExtra("roomname", Functions.get_Random_string(10));
                                startActivity(intent2);
                            }
                        });
                    }
                    else {
                        Functions.Show_Alert(this, "Alert", "You have reached out of your Video Call limit.",null);
                    }

                }
                break;

            case R.id.voice_call_btn:
                if(!checkPermissionForCameraAndMicrophone()){

                    requestPermissionForCameraAndMicrophone();
                }else {
                    int video_call_time= SharedPrefrence.get_int(Chat_A.this,SharedPrefrence.voice_calling_used_time);

                    if(video_call_time< Variables.max_voice_calling_time) {
                        Functions.Show_Alert(this, "Alert", "For demo purpose we only allow to "+Variables.max_voice_calling_time/1000+"s for voice Call", new CallBack() {
                            @Override
                            public void Get_Response(String requestType, String response) {
                                Intent intent2 = new Intent(Chat_A.this, VideoActivity.class);
                                intent2.putExtra("id", Receiverid);
                                intent2.putExtra("name", Receiver_name);
                                intent2.putExtra("image", Receiver_pic);
                                intent2.putExtra("status", VideoActivity.Call_Send);
                                intent2.putExtra("call_type","voice_call");
                                intent2.putExtra("roomname", Functions.get_Random_string(10));
                                startActivity(intent2);
                            }
                        });
                    }
                    else {
                        Functions.Show_Alert(this, "Alert", "You have reached out of your Voice Call limit.",null);
                    }

                }
                break;


            case R.id.send_btn:

                if(msg.getText().toString().length() == 0){

                }else if(msg.getText().toString().length() > 0){

                    SendMessage(msg.getText().toString());
                    msg.setText("");
                    show_sending_view();

                }
                break;

            case R.id.back_id:
                finish();
                break;

            case R.id.uploadimagebtn:
                selectfile();
                break;

            case R.id.select_camera_2:
                selectfile();
                break;

            case R.id.upload_stiker_btn:
                msg.setHint("Search Sticker");
                mediaType=MediaType.sticker;
                if(gif_layout.getVisibility()!= View.VISIBLE){
                    slideUp();
                }
                GetGipy();
                break;

            case R.id.upload_gif_btn:
                msg.setHint("Search Gifs");
                mediaType= MediaType.gif;
                if(gif_layout.getVisibility()!= View.VISIBLE){
                    slideUp();
                }
                GetGipy();
                break;

            case R.id.ic_more:
                Show_Dialogue();
                break;

        }

    }




    ValueEventListener valueEventListener;

    ChildEventListener eventListener;
    @Override
    public void onStart() {
        super.onStart();

        Variables.Opened_Chat_id = Variables.user_id;

        mChats.clear();
        mchatRef_reteriving = FirebaseDatabase.getInstance().getReference();
        query_getchat = mchatRef_reteriving.child("chat").child(Variables.user_id + "-" + Receiverid);

        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {

                    Log.d( "Count" + dataSnapshot.getChildrenCount() + " resp", dataSnapshot.toString());
                    dataSnapshot.getChildrenCount();
                    Chat_GetSet model = dataSnapshot.getValue(Chat_GetSet.class);
                    mChats.add(model);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(mChats.size() - 1);

                } catch (Exception ex) {
                    Log.e("", ex.getMessage());

                }
                ChangeStatus();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    try {
                        Chat_GetSet model = dataSnapshot.getValue(Chat_GetSet.class);

                        for (int i = mChats.size() - 1; i >= 0; i--) {
                            if (mChats.get(i).getTimestamp().equals(dataSnapshot.child("timestamp").getValue())) {
                                mChats.remove(i);
                                mChats.add(i, model);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", databaseError.getMessage());
            }
        };

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Variables.user_id + "-" + Receiverid)){

                    p_bar.setVisibility(View.GONE);
                    mchatRef_reteriving.removeEventListener(valueEventListener);
                }
                else {
                    p_bar.setVisibility(View.GONE);
                    mchatRef_reteriving.removeEventListener(valueEventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query_getchat.limitToLast(20).addChildEventListener(eventListener);

        mchatRef_reteriving.child("chat").addValueEventListener(valueEventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(query_getchat!=null && eventListener!=null)
        query_getchat.removeEventListener(eventListener);
    }

     public void ChangeStatus(){

        final Date c = Calendar.getInstance().getTime();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query query1 = reference.child("chat").child(Receiverid+"-"+Variables.user_id).orderByChild("status").equalTo("0");
        final Query query2 = reference.child("chat").child(Variables.user_id+"-"+Receiverid).orderByChild("status").equalTo("0");

        final DatabaseReference inbox_change_status_1=reference.child("Inbox").child(Variables.user_id+"/"+Receiverid);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    if(!nodeDataSnapshot.child("sender_id").getValue().equals(Variables.user_id)){
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "chat" + "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        result.put("time", Variables.df2.format(c));
                        reference.child(path).updateChildren(result);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodeDataSnapshot : dataSnapshot.getChildren()) {
                    if(!nodeDataSnapshot.child("sender_id").getValue().equals(Variables.user_id)){
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        String path = "chat" + "/" + dataSnapshot.getKey() + "/" + key;
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        result.put("time",Variables.df2.format(c));
                        reference.child(path).updateChildren(result);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inbox_change_status_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("rid").getValue().equals(Receiverid)){
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", "1");
                        inbox_change_status_1.updateChildren(result);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    public void SendMessage(final String message) {

        // todo: API RUN when send first msg
        Match_API_run_at_first_msg();


        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        final String current_user_ref = "chat" + "/" + Variables.user_id + "-" + Receiverid;
        final String chat_user_ref = "chat" + "/" + Receiverid + "-" + Variables.user_id;

        DatabaseReference reference = rootref.child("chat").child(Variables.user_id + "-" + Receiverid).push();
        final String pushid = reference.getKey();
        final HashMap message_user_map = new HashMap<>();
        message_user_map.put("chat_id",pushid);
        message_user_map.put("sender_id", Variables.user_id);
        message_user_map.put("receiver_id", Receiverid);
        message_user_map.put("sender_name", Variables.user_name);

        message_user_map.put("rec_img","" + Receiver_pic);  // Rec Pic
        message_user_map.put("pic_url","");
        message_user_map.put("lat","");
        message_user_map.put("long","");

        message_user_map.put("text", message);
        message_user_map.put("type","text");
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("timestamp", formattedDate);

        final HashMap user_map = new HashMap<>();
        user_map.put(current_user_ref + "/" + pushid, message_user_map);
        user_map.put(chat_user_ref + "/" + pushid, message_user_map);

        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Add_inbox(formattedDate,message);
            }
        });
    }


    // this method will upload the image in chhat
    public void UploadImage(ByteArrayOutputStream byteArrayOutputStream){

        // todo: API RUN when send first msg
        Match_API_run_at_first_msg();

        byte[] data = byteArrayOutputStream.toByteArray();

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        StorageReference reference= FirebaseStorage.getInstance().getReference();
        DatabaseReference dref=rootref.child("chat").child(Variables.user_id+"-"+Receiverid).push();
        final String key=dref.getKey();
        uploading_image_id=key;
        final String current_user_ref = "chat" + "/" + Variables.user_id + "-" + Receiverid;
        final String chat_user_ref = "chat" + "/" + Receiverid + "-" + Variables.user_id;

        HashMap my_dummi_pic_map = new HashMap<>();
        my_dummi_pic_map.put("receiver_id", Receiverid);
        my_dummi_pic_map.put("sender_id", Variables.user_id);
        my_dummi_pic_map.put("sender_name", Variables.user_name);
        my_dummi_pic_map.put("chat_id",key);

        my_dummi_pic_map.put("rec_img","");
        my_dummi_pic_map.put("pic_url","none");
        my_dummi_pic_map.put("lat","");
        my_dummi_pic_map.put("long","");

        my_dummi_pic_map.put("text", "");
        my_dummi_pic_map.put("type","image");
        my_dummi_pic_map.put("status", "0");
        my_dummi_pic_map.put("time", "");
        my_dummi_pic_map.put("timestamp", formattedDate);

        HashMap dummy_push = new HashMap<>();
        dummy_push.put(current_user_ref + "/" + key, my_dummi_pic_map);
        rootref.updateChildren(dummy_push);

        reference.child("images").child(key+".jpg").putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploading_image_id="none";

                HashMap message_user_map = new HashMap<>();
                message_user_map.put("receiver_id", Receiverid);
                message_user_map.put("sender_id", Variables.user_id);
                message_user_map.put("sender_name", Variables.user_name);
                message_user_map.put("chat_id",key);

                message_user_map.put("rec_img","");
                message_user_map.put("pic_url",taskSnapshot.getDownloadUrl().toString());
                message_user_map.put("lat","");
                message_user_map.put("long","");

                message_user_map.put("text", "");
                message_user_map.put("type","image");
                message_user_map.put("status", "0");
                message_user_map.put("time", "");
                message_user_map.put("timestamp", formattedDate);

                HashMap user_map = new HashMap<>();

                user_map.put(current_user_ref + "/" + key, message_user_map);
                user_map.put(chat_user_ref + "/" + key, message_user_map);

                rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        Add_inbox(formattedDate,"send an image");
                    }
                });
            }
        });
    }



    public void Add_inbox(String formattedDate,String message){

        String inbox_sender_ref = "Inbox" + "/" + Variables.user_id + "/" + Receiverid;
        String inbox_receiver_ref = "Inbox" + "/" + Receiverid + "/" + Variables.user_id;


        HashMap<String, String> sendermap=new HashMap<>();
        sendermap.put("rid",Variables.user_id);
        sendermap.put("name",Variables.user_name);
        sendermap.put("msg",message);
        sendermap.put("pic",Variables.user_pic);
        sendermap.put("timestamp",formattedDate);
        sendermap.put("date",formattedDate);

        sendermap.put("sort",""+Calendar.getInstance().getTimeInMillis());
        sendermap.put("status","0");
        sendermap.put("block","0");
        sendermap.put("read","0");
        sendermap.put("like","0");

        HashMap<String, String> receivermap=new HashMap<>();
        receivermap.put("rid",Receiverid);
        receivermap.put("name",Receiver_name);
        receivermap.put("msg",message);
        receivermap.put("pic",Receiver_pic);
        receivermap.put("timestamp",formattedDate);
        receivermap.put("date",formattedDate);

        receivermap.put("sort",""+Calendar.getInstance().getTimeInMillis());
        receivermap.put("status","1");
        receivermap.put("block","0");
        receivermap.put("read","0");
        receivermap.put("like","0");


        HashMap both_user_map = new HashMap<>();
        both_user_map.put(inbox_sender_ref , receivermap);
        both_user_map.put(inbox_receiver_ref , sendermap);

        Adduser_to_inbox.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Functions.send_push_notification(context, "" + Receiverid, "send a picture ","messege");

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean check_camrapermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA}, Variables.permission_camera_code);
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean check_ReadStoragepermission(){
        if (ContextCompat.checkSelfPermission(Chat_A.this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Variables.permission_Read_data );
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean check_writeStoragepermission(){
        if (ContextCompat.checkSelfPermission(Chat_A.this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Variables.permission_write_data );
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Variables.permission_camera_code) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap again", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();

            }
        }

        if (requestCode == Variables.permission_Read_data) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap again", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == Variables.permission_write_data) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap Again", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == Variables.permission_Recording_audio) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tap Again", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void selectfile() {

        final CharSequence[] options = { "Take Photo", "Choose Photo from Gallery", "Select video" , "Cancel" };


        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);

        builder.setTitle("Select");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))
                {

                    if(check_camrapermission() && check_writeStoragepermission())
                    openCameraIntent();


                }

                else if (options[item].equals("Choose Photo from Gallery"))

                {

                    if(check_ReadStoragepermission()) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                }


                else if (options[item].equals("Select video"))

                {
                    if(check_ReadStoragepermission()) {
                        chooseVideo();
                    }
                }


                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCameraIntent() {

            Intent pictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if(pictureIntent.resolveActivity(getPackageManager()) != null){

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Functions.Log_d_msg(Chat_A.this,"" + ex.toString());
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(pictureIntent, CAMERA_REQUEST);
                }
            }


    }

    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Functions.Log_d_msg(Chat_A.this,"" + imageFilePath);

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    public String getPath(Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }



    private void chooseVideo() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, Variables.vedio_request_code);
    }



    //on image select activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Functions.Log_d_msg(Chat_A.this,"In Camera 1");
        if (resultCode == RESULT_OK) {
            Functions.Log_d_msg(Chat_A.this,"In Camera 2");
            if (requestCode == CAMERA_REQUEST) {
                Functions.Log_d_msg(Chat_A.this,"In Camera 3");
                Matrix matrix = new Matrix();
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
               // Uri selectedImage =(Uri.fromFile(new File(imageFilePath)));

                Functions.Log_d_msg(Chat_A.this,"" + imageFilePath);
                               InputStream imageStream = null;
                Uri selectedImage =(Uri.fromFile(new File(imageFilePath)));
                Functions.Log_d_msg(Chat_A.this,"ok " + selectedImage);
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Functions.Log_d_msg(Chat_A.this,"" + e.toString());
                }

                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);


                UploadImage(baos);


            }

            else if (requestCode == PICK_IMAGE_GALLERY) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path=getPath(selectedImage);
                Matrix matrix = new Matrix();
                ExifInterface exif = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    try {
                        exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                UploadImage(baos);

            }

            else if(requestCode == Variables.vedio_request_code){
                Uri selectedImageUri = data.getData();
                Chat_Send_file_Service mService = new Chat_Send_file_Service();

                // todo: API RUN when send first msg
                Match_API_run_at_first_msg();

                if (!Functions.isMyServiceRunning(context,mService.getClass())) {
                    Intent mServiceIntent = new Intent(context.getApplicationContext(), mService.getClass());
                    mServiceIntent.setAction("startservice");
                    mServiceIntent.putExtra("uri",""+selectedImageUri);
                    mServiceIntent.putExtra("type","video");

                    mServiceIntent.putExtra("sender_id",Variables.user_id);
                    mServiceIntent.putExtra("sender_name",Variables.user_name);
                    mServiceIntent.putExtra("sender_pic",Variables.user_pic);

                    mServiceIntent.putExtra("receiver_id",Receiverid);
                    mServiceIntent.putExtra("receiver_name",Receiver_name);
                    mServiceIntent.putExtra("receiver_pic",Receiver_pic);

                    mServiceIntent.putExtra("token",token);

                    context.startService(mServiceIntent);
                }
                else {
                    Toast.makeText(context, "Please wait video already in uploading progress", Toast.LENGTH_LONG).show();
                }

            }

        }

    }

    //this method will get the big size of image in private chat
    public void OpenfullsizeImage(Chat_GetSet item){
        See_Full_Image_F see_image_f = new See_Full_Image_F();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putSerializable("image_url", item.getPic_url());
        args.putSerializable("chat_id", item.getChat_id());
        see_image_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Chat_F, see_image_f).commit();

    }

    //this method will get the big size of image in private chat
    public void OpenVideo(String path){
        Play_Video_F play_video_f = new Play_Video_F();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("path", path);
        play_video_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Chat_F, play_video_f).commit();

    }


    public void get_latest_user_profile (String user_id){

        try{
            JSONObject  user_data_objs = new JSONObject();
            user_data_objs.put("fb_id", user_id);

            ApiRequest.Call_Api(
                    Chat_A.this,
                   Api_Links.getUserInfo,
                    user_data_objs,
                    new CallBack() {
                        @Override
                        public void Get_Response(String requestType, String resp) {

                            try{

                                JSONObject response = new JSONObject(resp);

                                if(response.getString("code").equals("200")) {

                                    JSONArray msg_obj = response.getJSONArray("msg");
                                    JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                    Receiver_pic = user_info_obj.getString("image1");
                                    Receiver_name = user_info_obj.getString("first_name")+" "+user_info_obj.getString("last_name");
                                    // todo: Method to display user image
                                    display_user_image(Receiver_pic, Receiver_name);

                                }

                            }catch (Exception b){
                                Functions.Log_d_msg(Chat_A.this,"" + b.toString());

                            }
                        }
                    }
            );

        }catch (Exception b){
            //swipe_loader.setRefreshing(false);
            Functions.Log_d_msg(Chat_A.this ,"" + b.toString());

        }



    }  // End Method to get user Updated Profile_F.


    // Method to display top Menu Button
    public void Show_Dialogue(){
        // Method to display dialogue for Top Menu
        final CharSequence[] options = {"Un Match User", "" + blocking_text , "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Un Match User")) {
                    dialog.dismiss();
                    // if user click on Un Match
                    // Calling An API..
                    unMatch_user_API();



                } else if (options[item].equals("" + blocking_text)) {
                    dialog.dismiss();
                    // If User Click On Block user

                    Block_User_from_Chat(Receiverid);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    } // End Method to show Top Menu


    // Method to Un Block the user
    public void unMatch_user_API(){
        // Method to Un Block the user

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.user_id);
            parameters.put("other_id", Receiverid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Calling An API

        ApiRequest.Call_Api(
                context,
                "" + Api_Links.unMatch,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String response) {
                        // Variables.pDialog.hide();
                        pd.hide();

                        try{
                            List_MyMatch.remove(Integer.parseInt(remove_position_in_my_match));

                            //adapter.notifyItemRemoved(Integer.parseInt(remove_position_in_my_match));
                            adp.notifyItemRemoved(Integer.parseInt(remove_position_in_my_match));
                            adp.notifyItemRangeChanged(Integer.parseInt(remove_position_in_my_match), List_MyMatch.size());
                            adp.notifyDataSetChanged();
                            //  Functions.toast_msg(context,"Removed " + remove_position_in_my_match + " Now Size " + List_MyMatch.size());

                        }catch (Exception b){

                        }


                        // oipgu

                        try{

                            JSONObject response_1 = new JSONObject(response);
                            JSONArray msg_arr = response_1.getJSONArray("msg");
                            Functions.toast_msg(context,"" + msg_arr.getJSONObject(0).getString("response"));

                            finish();

                            //  Functions.toast_msg(context,"" + response);

                        }catch (Exception b){

                        }
                    }
                }
        );
    } // End Metho to un blok user

    // Method to Block the user From Inbox_F
    public void Block_User_from_Chat (String rec_id){


        final Date c = Calendar.getInstance().getTime();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query query1 = reference.child("chat").child(Receiverid+"-"+Variables.user_id).orderByChild("status").equalTo("0");
        final Query query2 = reference.child("chat").child(Variables.user_id+"-"+Receiverid).orderByChild("status").equalTo("0");

        final DatabaseReference inbox_change_status_1=reference.child("Inbox").child(Variables.user_id+"/"+Receiverid);
        final DatabaseReference inbox_change_status_2=reference.child("Inbox").child(Receiverid+"/"+Variables.user_id);

        String send_block = "";

        if(is_block.equals("0")){
            // If Un Block and user wanna Block
            is_block = "1";
            blocking_text = "Un Block User";
        }else if(is_block.equals("1")){
            is_block = "0";
            blocking_text = "Block User";
        }

        final String finalSend_block = send_block;

        inbox_change_status_1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("rid").getValue().equals(Receiverid)){
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("block", "" + is_block);
                        inbox_change_status_1.updateChildren(result);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        inbox_change_status_2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("rid").getValue().equals(Receiverid)){
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("block", "" + is_block);
                        inbox_change_status_2.updateChildren(result);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }// End Method to Block the User



    // slide the view from below itself to the current position
    public void slideUp(){
        upload_gif_btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gif));
        gif_layout.setVisibility(View.VISIBLE);
        //sendbtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_search));
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(700);
        gif_layout.startAnimation(animate);
    }


    // slide the view from its current position to below itself
    public void slideDown(){
        msg.setHint("message ...");
        msg.setText("");
        upload_gif_btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gif));
        // sendbtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_send));
        gif_layout.setVisibility(View.GONE);

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(700);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gif_layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        gif_layout.startAnimation(animate);

    }


    // this is related with the list of Gifs that is show in the list below
    Gif_Adapter gif_adapter;
    final ArrayList<String> url_list=new ArrayList<>();
    RecyclerView gips_list;
    GPHApi client ;
    public void GetGipy(){
        if(client==null)
        client = new GPHApiClient(context.getResources().getString(R.string.gif_api_key));

        url_list.clear();
        gips_list = findViewById(R.id.gif_recylerview);

        ImageButton hide_gif_layout = findViewById(R.id.hide_gif_layout);
        hide_gif_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideDown();
            }
        });


        gips_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        gif_adapter=new Gif_Adapter(context, url_list, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {

                String s = (String)Model;

                SendGif(s);
                slideDown();
            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });
        gips_list.setAdapter(gif_adapter);

        client.trending(mediaType, null, null, null,  new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result == null) {
                    // Do what you want to do with the error
                } else {
                    if (result.getData() != null) {
                        for (Media gif : result.getData()) {

                            url_list.add(gif.getId());
                        }
                        gif_adapter.notifyDataSetChanged();

                    } else {
                        Log.e("giphy error", "No results found");
                    }
                }
            }
        });
    }



    public void searchGif(String search){
        /// Gif Search
        client.search(search, mediaType, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result == null) {
                    // Do what you want to do with the error
                } else {
                    if (result.getData() != null) {
                        url_list.clear();
                        for (Media gif : result.getData()) {
                            url_list.add(gif.getId());
                            gif_adapter.notifyDataSetChanged();
                        }
                        gips_list.smoothScrollToPosition(0);

                    } else {
                        Log.e("giphy error", "No results found");
                    }
                }
            }
        });
    }


    // this method will upload the image in chhat
    public void SendGif(String url){
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        // todo: API RUN when send first msg
        Match_API_run_at_first_msg();

        DatabaseReference dref = rootref.child("chat").child(Variables.user_id+"-"+Receiverid).push();
        final String key=dref.getKey();

        String current_user_ref = "chat" + "/" + Variables.user_id + "-" + Receiverid;
        String chat_user_ref = "chat" + "/" + Receiverid + "-" + Variables.user_id;

        HashMap message_user_map = new HashMap<>();
        message_user_map.put("receiver_id", Receiverid);
        message_user_map.put("sender_id", Variables.user_id);
        message_user_map.put("sender_name", Variables.user_name);
        message_user_map.put("chat_id",key);


        message_user_map.put("rec_img","");
        message_user_map.put("pic_url",url);
        message_user_map.put("lat","");
        message_user_map.put("long","");

        message_user_map.put("text", "");
        message_user_map.put("type","gif");
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("timestamp", formattedDate);

        HashMap user_map = new HashMap<>();

        user_map.put(current_user_ref + "/" + key, message_user_map);
        user_map.put(chat_user_ref + "/" + key, message_user_map);

        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Add_inbox(formattedDate,"Send an gif");

            }
        });
    }



    public void display_user_image(String img_link, String Recever_name){

        try{
            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img_link))
                            .setProgressiveRenderingEnabled(false)
                            .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(false)
                    .build();

            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);

            userimage.getHierarchy().setPlaceholderImage(R.drawable.ic_account_gray);
            userimage.getHierarchy().setFailureImage(R.drawable.ic_account_gray);
            userimage.getHierarchy().setRoundingParams(roundingParams);
            userimage.setController(controller);

            t_user_rec_name.setText("" + Recever_name);


        }catch (Exception v){

        }


    }


    public void Match_API_run_at_first_msg(){

       if(is_match_api_run != null){

            if(is_match_api_run.equals("1")){

                Functions.API_firstchat(context, Variables.user_id, Receiverid);


            try{
                List_MyMatch.remove(Integer.parseInt(remove_position_in_my_match));

                 adp.notifyItemRemoved(Integer.parseInt(remove_position_in_my_match));
                adp.notifyItemRangeChanged(Integer.parseInt(remove_position_in_my_match), List_MyMatch.size());
                adp.notifyDataSetChanged();

            }catch (Exception b){

            }

            }


       }

    }



    public void get_user_is_block_or_not (){

        ValueEventListener eventListener;
        Query inbox_query;
        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();

        blocked.setVisibility(View.VISIBLE);
        findViewById(R.id.writechatlayout).setVisibility(View.GONE);
        inbox_query = rootref.child("Inbox").child(Receiverid).child(Variables.user_id);
        inbox_query.keepSynced(true);

        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){

                    blocked.setVisibility(View.GONE);
                    findViewById(R.id.writechatlayout).setVisibility(View.VISIBLE);

                }else {

                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                       if(ds.getKey().equals("block")){

                            if(ds.getValue().equals("1")){
                                blocked.setVisibility(View.VISIBLE);

                                findViewById(R.id.writechatlayout).setVisibility(View.GONE);

                            }else if(ds.getValue().equals("0")){

                                blocked.setVisibility(View.GONE);

                                findViewById(R.id.writechatlayout).setVisibility(View.VISIBLE);
                            }
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



    private boolean checkPermissionForCameraAndMicrophone() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int resultCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            int resultMic = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
            return resultCamera == PackageManager.PERMISSION_GRANTED &&
                    resultMic == PackageManager.PERMISSION_GRANTED;
        }
        else {
            return true;
        }

    }

    private void requestPermissionForCameraAndMicrophone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                    shouldShowRequestPermissionRationale(
                            Manifest.permission.RECORD_AUDIO)) {

            } else {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                        Variables.CAMERA_MIC_PERMISSION_REQUEST_CODE);
            }
        }
    }



}
