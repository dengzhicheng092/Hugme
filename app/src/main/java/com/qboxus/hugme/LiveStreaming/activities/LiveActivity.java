package com.qboxus.hugme.LiveStreaming.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qboxus.hugme.All_Activities.MainActivity;
import com.qboxus.hugme.All_Adapters.Live_Comments_Adapter;
import com.qboxus.hugme.All_Model_Classes.Live_Comment_Model;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.LiveStreaming.stats.LocalStatsData;
import com.qboxus.hugme.LiveStreaming.stats.RemoteStatsData;
import com.qboxus.hugme.LiveStreaming.stats.StatsData;
import com.qboxus.hugme.LiveStreaming.ui.VideoGridContainer;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Utils.KeyboardHeightObserver;
import com.qboxus.hugme.Utils.KeyboardHeightProvider;

import java.util.ArrayList;
import java.util.HashMap;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class LiveActivity extends RtcBaseActivity implements View.OnClickListener, KeyboardHeightObserver {
    private static final String TAG = LiveActivity.class.getSimpleName();

    private VideoGridContainer mVideoGridContainer;
    private ImageView mMuteAudioBtn;
    private ImageView mMuteVideoBtn;

    private VideoEncoderConfiguration.VideoDimensions mVideoDimension;

    DatabaseReference rootref;

    String user_id,user_name,user_picture;
    int user_role;
    EditText message_edit;
    private KeyboardHeightProvider keyboardHeightProvider;
    RelativeLayout write_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        rootref= FirebaseDatabase.getInstance().getReference();

        Intent bundle=getIntent();
        if(bundle!=null){
            user_id=bundle.getStringExtra("user_id");
            user_name=bundle.getStringExtra("user_name");
            user_picture=bundle.getStringExtra("user_picture");
            user_role=bundle.getIntExtra("user_role", io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);

        }

        if(user_role== io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER){

            Add_firebase_node();
            findViewById(R.id.live_btn_mute_video).setOnClickListener(this);

            if(!SharedPrefrence.get_boolean(this,SharedPrefrence.is_puchase))
              StartTimer();

        }else {
            listener_node();
        }

        TextView live_user_name=findViewById(R.id.live_user_name);
        live_user_name.setText(user_name);

        initUI();
        initData();

        message_edit=findViewById(R.id.message_edit);
        write_layout=findViewById(R.id.write_layout);
        keyboardHeightProvider = new KeyboardHeightProvider(this);


        findViewById(R.id.cross_btn).setOnClickListener(this::onClick);
        findViewById(R.id.swith_camera_btn).setOnClickListener(this::onClick);
        findViewById(R.id.live_btn_mute_audio).setOnClickListener(this::onClick);
        findViewById(R.id.live_btn_mute_video).setOnClickListener(this::onClick);
        findViewById(R.id.live_btn_beautification).setOnClickListener(this::onClick);
        findViewById(R.id.send_btn).setOnClickListener(this::onClick);

        findViewById(R.id.live_activity).post(new Runnable() {
            public void run() {
                keyboardHeightProvider.start();
            }
        });

        Get_Comment_Data();
    }

    private void initUI() {
        
        initUserIcon();

       boolean isBroadcaster =  (user_role == Constants.CLIENT_ROLE_BROADCASTER);

        mMuteVideoBtn = findViewById(R.id.live_btn_mute_video);
        mMuteVideoBtn.setActivated(isBroadcaster);

        mMuteAudioBtn = findViewById(R.id.live_btn_mute_audio);
        mMuteAudioBtn.setActivated(isBroadcaster);

        ImageView beautyBtn = findViewById(R.id.live_btn_beautification);
        beautyBtn.setActivated(true);
        rtcEngine().setBeautyEffectOptions(beautyBtn.isActivated(),
                com.qboxus.hugme.LiveStreaming.Constants.DEFAULT_BEAUTY_OPTIONS);

        mVideoGridContainer = findViewById(R.id.live_video_grid_layout);
        mVideoGridContainer.setStatsManager(statsManager());

        rtcEngine().setClientRole(user_role);
        if (isBroadcaster) startBroadcast();
    }

    private void initUserIcon() {
        SimpleDraweeView iconView = findViewById(R.id.live_name_board_icon);
        if(user_picture!=null && !user_picture.equals("")) {
            Uri uri = Uri.parse(user_picture);
            iconView.setImageURI(uri);
        }
    }

    private void initData() {
        mVideoDimension = com.qboxus.hugme.LiveStreaming.Constants.VIDEO_DIMENSIONS[
                config().getVideoDimenIndex()];
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        RelativeLayout topLayout = findViewById(R.id.live_room_top_layout);
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) topLayout.getLayoutParams();
        params.height = mStatusBarHeight + topLayout.getMeasuredHeight();
        topLayout.setLayoutParams(params);
        topLayout.setPadding(0, mStatusBarHeight, 0, 0);
    }

    private void startBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        SurfaceView surface = prepareRtcVideo(0, true);
        mVideoGridContainer.addUserVideoSurface(0, surface, true);
        mMuteAudioBtn.setActivated(true);
    }

    private void stopBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        removeRtcVideo(0, true);
        mVideoGridContainer.removeUserVideo(0, true);
        mMuteAudioBtn.setActivated(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        // Do nothing at the moment
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        // Do nothing at the moment
    }

    @Override
    public void onUserOffline(final int uid, int reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeRemoteUser(uid);
            }
        });
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                renderRemoteUser(uid);
            }
        });
    }

    private void renderRemoteUser(int uid) {
        SurfaceView surface = prepareRtcVideo(uid, false);
        mVideoGridContainer.addUserVideoSurface(uid, surface, false);
    }

    private void removeRemoteUser(int uid) {
        removeRtcVideo(uid, false);
        mVideoGridContainer.removeUserVideo(uid, false);
    }

    @Override
    public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setWidth(mVideoDimension.width);
        data.setHeight(mVideoDimension.height);
        data.setFramerate(stats.sentFrameRate);
    }

    @Override
    public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setLastMileDelay(stats.lastmileDelay);
        data.setVideoSendBitrate(stats.txVideoKBitRate);
        data.setVideoRecvBitrate(stats.rxVideoKBitRate);
        data.setAudioSendBitrate(stats.txAudioKBitRate);
        data.setAudioRecvBitrate(stats.rxAudioKBitRate);
        data.setCpuApp(stats.cpuAppUsage);
        data.setCpuTotal(stats.cpuAppUsage);
        data.setSendLoss(stats.txPacketLossRate);
        data.setRecvLoss(stats.rxPacketLossRate);
    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        if (!statsManager().isEnabled()) return;

        StatsData data = statsManager().getStatsData(uid);
        if (data == null) return;

        data.setSendQuality(statsManager().qualityToString(txQuality));
        data.setRecvQuality(statsManager().qualityToString(rxQuality));
    }

    @Override
    public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setWidth(stats.width);
        data.setHeight(stats.height);
        data.setFramerate(stats.rendererOutputFrameRate);
        data.setVideoDelay(stats.delay);
    }

    @Override
    public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setAudioNetDelay(stats.networkTransportDelay);
        data.setAudioNetJitter(stats.jitterBufferDelay);
        data.setAudioLoss(stats.audioLossRate);
        data.setAudioQuality(statsManager().qualityToString(stats.quality));
    }

    @Override
    public void finish() {
        super.finish();
        statsManager().clearAllData();
    }



    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(user_role== io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER){
          Remove_node();
        }else {
            remove_listener();
        }
        keyboardHeightProvider.close();

        remove_comment_listener();

        Stop_timer();
    }

    public void Add_firebase_node(){
        HashMap map=new HashMap();
        map.put("user_id",user_id);
        map.put("user_name",user_name);
        map.put("user_picture",user_picture);
        rootref.child("LiveUsers").child(user_id).setValue(map);
    }

    public void Remove_node(){
        rootref.child("LiveUsers").child(user_id).removeValue();
    }


    ValueEventListener valueEventListener;
    public void listener_node(){

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        rootref.child("LiveUsers").child(user_id).addValueEventListener(valueEventListener);
    }

    public void remove_listener(){
        if(rootref!=null && valueEventListener!=null){
            rootref.child("LiveUsers").child(user_id).removeEventListener(valueEventListener);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cross_btn:
                finish();
                break;

            case R.id.swith_camera_btn:
                rtcEngine().switchCamera();
                break;

            case R.id.live_btn_mute_audio:
                if (!mMuteVideoBtn.isActivated()) return;

                rtcEngine().muteLocalAudioStream(view.isActivated());
                view.setActivated(!view.isActivated());
                break;

            case R.id.live_btn_beautification:
                view.setActivated(!view.isActivated());
                rtcEngine().setBeautyEffectOptions(view.isActivated(),
                        com.qboxus.hugme.LiveStreaming.Constants.DEFAULT_BEAUTY_OPTIONS);
                break;

            case R.id.live_btn_mute_video:
                if (view.isActivated()) {
                    stopBroadcast();
                } else {
                    startBroadcast();
                }
                view.setActivated(!view.isActivated());
                break;

            case R.id.send_btn:
                if(!TextUtils.isEmpty(message_edit.getText().toString())){
                    Add_Messages();
                  }
                break;
        }
    }


    ArrayList<Object> data_list;
    RecyclerView recyclerView;
    Live_Comments_Adapter adapter;
    public void init_adapter(){
        data_list=new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        adapter=new Live_Comments_Adapter(this, data_list, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {

            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

        recyclerView.setAdapter(adapter);

    }

    public void Add_Messages(){

        DatabaseReference dref = rootref.child("LiveUsers").child(user_id).child("Chat").push();

        final String key=dref.getKey();
        String my_id = SharedPrefrence.get_string(this,SharedPrefrence.u_id);
        String my_name = Functions.get_info(this,"first_name")
                +" "+Functions.get_info(this,"last_name");
        String my_image = Functions.get_info(this,"image1");

        HashMap message_user_map = new HashMap<>();
        message_user_map.put("id",key);
        message_user_map.put("user_id", my_id);
        message_user_map.put("user_name", my_name);
        message_user_map.put("user_picture", my_image);
        message_user_map.put("comment",message_edit.getText().toString());

        rootref.child("LiveUsers").child(user_id).child("Chat").child(key).setValue(message_user_map);

        message_edit.setText(null);

    }

    ChildEventListener childEventListener;
    public void Get_Comment_Data(){
        init_adapter();

        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Live_Comment_Model model = dataSnapshot.getValue(Live_Comment_Model.class);
                data_list.add(model);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(data_list.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        rootref.child("LiveUsers").child(user_id).child("Chat").addChildEventListener(childEventListener);
    }

    public void remove_comment_listener(){
        if(rootref!=null && childEventListener!=null)
            rootref.child("LiveUsers").child(user_id).child("Chat").removeEventListener(childEventListener);

    }



    CountDownTimer countDownTimer;
    public void StartTimer(){
        countDownTimer= new CountDownTimer(Variables.max_streming_time, 1000) {

            public void onTick(long millisUntilFinished) {
                int streaming=SharedPrefrence.get_int(LiveActivity.this,SharedPrefrence.streaming_used_time);
                SharedPrefrence.save_int(LiveActivity.this,streaming+1000,SharedPrefrence.streaming_used_time);

                if(streaming>Variables.max_streming_time){
                    finish();
                }

            }

            public void onFinish() {
               finish();
            }
        }.start();
    }

    public void Stop_timer(){
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);
    }


    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(write_layout.getWidth(), write_layout.getHeight());
        params.bottomMargin = height;
        write_layout.setLayoutParams(params);

    }

}
