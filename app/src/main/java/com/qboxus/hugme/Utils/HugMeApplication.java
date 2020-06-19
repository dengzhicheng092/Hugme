package com.qboxus.hugme.Utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.LiveStreaming.Constants;
import com.qboxus.hugme.LiveStreaming.rtc.AgoraEventHandler;
import com.qboxus.hugme.LiveStreaming.rtc.EngineConfig;
import com.qboxus.hugme.LiveStreaming.rtc.EventHandler;
import com.qboxus.hugme.LiveStreaming.stats.StatsManager;
import com.qboxus.hugme.LiveStreaming.utils.FileUtil;
import com.qboxus.hugme.LiveStreaming.utils.PrefManager;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Volley_Package.CallBack;

import io.agora.rtc.RtcEngine;
import io.fabric.sdk.android.Fabric;

public class HugMeApplication extends Application {

    private RtcEngine mRtcEngine;
    private EngineConfig mGlobalConfig = new EngineConfig();
    private AgoraEventHandler mHandler = new AgoraEventHandler();
    private StatsManager mStatsManager = new StatsManager();
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
        AudienceNetworkAds.initialize(this);


        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), getString(R.string.agora_app_id), mHandler);
            mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(FileUtil.initializeLogFile(this));
        } catch (Exception e) {
            e.printStackTrace();
        }




        initConfig();
    }


    private void initConfig() {
        SharedPreferences pref = PrefManager.getPreferences(getApplicationContext());
        mGlobalConfig.setVideoDimenIndex(pref.getInt(
                Constants.PREF_RESOLUTION_IDX, Constants.DEFAULT_PROFILE_IDX));

        boolean showStats = pref.getBoolean(Constants.PREF_ENABLE_STATS, false);
        mGlobalConfig.setIfShowVideoStats(showStats);
        mStatsManager.enableStats(showStats);

        mGlobalConfig.setMirrorLocalIndex(pref.getInt(Constants.PREF_MIRROR_LOCAL, 0));
        mGlobalConfig.setMirrorRemoteIndex(pref.getInt(Constants.PREF_MIRROR_REMOTE, 0));
        mGlobalConfig.setMirrorEncodeIndex(pref.getInt(Constants.PREF_MIRROR_ENCODE, 0));
    }

    public EngineConfig engineConfig() {
        return mGlobalConfig;
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public StatsManager statsManager() {
        return mStatsManager;
    }

    public void registerEventHandler(EventHandler handler) {
        mHandler.addHandler(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        mHandler.removeHandler(handler);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RtcEngine.destroy();
    }
}
