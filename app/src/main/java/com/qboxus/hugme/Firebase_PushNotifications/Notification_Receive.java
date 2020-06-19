package com.qboxus.hugme.Firebase_PushNotifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;
import com.qboxus.hugme.All_Activities.Splashscreen_A;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

public class Notification_Receive extends FirebaseMessagingService {

    String  pic;
    String  title;
    String  message;
    String receiverid;

    sendNotification sendNotification=new sendNotification(this);


    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

         if (remoteMessage.getData().size() > 0) {

            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("body");
            pic= remoteMessage.getData().get("icon");
            receiverid=remoteMessage.getData().get("receiverid");

            SharedPrefrence.pref.getString( SharedPrefrence.share_social_info,"null");
            final String user_id = Functions.get_info(this,"fb_id");


            if(user_id.equals(receiverid)){
                sendNotification.execute(pic);
            }

        }

    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        if(s==null){

        }else if(s.equals("null")){

        }
        else if(s.equals("")){

        }
        else if(s.length()<6){

        }
        else {
            SharedPrefrence.save_string(this, s, SharedPrefrence.u_Device_token);
        }


    }



    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;


        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }


        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);

            try {
                final String CHANNEL_ID = receiverid;
                final String CHANNEL_NAME = receiverid;

                Intent notificationIntent;


                notificationIntent = new Intent(ctx, Splashscreen_A.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(ctx.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel defaultChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(defaultChannel);
                }



                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(ctx,CHANNEL_ID);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(R.mipmap.ic_app_icon);
                } else {
                    builder.setSmallIcon(R.mipmap.ic_app_icon);
                }

                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(result)
                        .setContentTitle(title)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentText(message).setAutoCancel(true).setSound(soundUri)
                        .setContentIntent(pendingIntent);

                Notification notification = builder.build();
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notificationManager.notify(1, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }




}
