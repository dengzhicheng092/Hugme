package  com.qboxus.hugme.Chat_pkg.Videos;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.qboxus.hugme.Code_Classes.Functions;
import  com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Activities.Splashscreen_A;


/**
 * Created by AQEEL on 6/7/2018.
 */

public class Chat_Send_file_Service extends Service {

    Uri uri;
    StorageReference reference;
    DatabaseReference rootref;
    String Sender_id,Sender_name,Sender_pic;
    String Receiverid,Receiver_name,Receiver_pic;

    String token;

    public Chat_Send_file_Service() {
        super();
    }



    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);
        reference= FirebaseStorage.getInstance().getReference();
        rootref= FirebaseDatabase.getInstance().getReference();
       // Functions.toast_msg(this,"Start");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Functions.toast_msg(this,"Start");
        if(intent!=null){
        if (intent.getAction().equals("startservice")) {
            showNotification();

           String uri_string= intent.getStringExtra("uri");
            uri = Uri.parse(uri_string);



            Sender_id=intent.getStringExtra("sender_id");
            Sender_name=intent.getStringExtra("sender_name");
            Sender_pic=intent.getStringExtra("sender_pic");

            Receiverid=intent.getStringExtra("receiver_id");
            Receiver_name=intent.getStringExtra("receiver_name");
            Receiver_pic=intent.getStringExtra("receiver_pic");

            token=intent.getStringExtra("token");

            String type=intent.getStringExtra("type");
            if(type.equals("video")){

                save_video_in_firebase();

            }
            else if(type.equals("pdf")){
                save_pdf_in_firebase();
            }

        }


    }
        return Service.START_STICKY;
    }



    private void showNotification() {
        Intent notificationIntent = new Intent(this, Splashscreen_A.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification.Builder notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Uploading Video")
                .setContentText("Please wait! Video is uploading....")
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        android.R.drawable.stat_sys_upload))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        startForeground(101, notification.build());

    }


    public void save_video_in_firebase(){
        DatabaseReference dref=rootref.child("chat").child(Sender_id+"-"+Receiverid).push();
        final String key=dref.getKey();

        reference.child("Video").child(key+".mp4").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                stopSelf();
                Upload_video(taskSnapshot.getDownloadUrl().toString(),key);
            }
        });
    }

    // this method will upload the image in chhat
    public void Upload_video(String url, String key){
         Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);



        final String current_user_ref = "chat" + "/" + Sender_id + "-" + Receiverid;
        final String chat_user_ref = "chat" + "/" + Receiverid + "-" + Sender_id;


                HashMap message_user_map = new HashMap<>();
                message_user_map.put("receiver_id", Receiverid);
                message_user_map.put("sender_id", Sender_id);
                message_user_map.put("sender_name", Variables.user_name);
                message_user_map.put("chat_id",key);

                message_user_map.put("rec_img","");
                message_user_map.put("pic_url",url);
                message_user_map.put("lat","");
                message_user_map.put("long","");

                message_user_map.put("text", "");
                message_user_map.put("type","video");
                message_user_map.put("status", "0");
                message_user_map.put("time", "");
                message_user_map.put("timestamp", formattedDate);

                HashMap user_map = new HashMap<>();

                user_map.put(current_user_ref + "/" + key, message_user_map);
                user_map.put(chat_user_ref + "/" + key, message_user_map);

                rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        String inbox_sender_ref = "Inbox" + "/" + Sender_id + "/" + Receiverid;
                        String inbox_receiver_ref = "Inbox" + "/" + Receiverid + "/" + Sender_id;


                        HashMap<String, String> sendermap=new HashMap<>();
                        sendermap.put("rid",Sender_id);
                        sendermap.put("name",Sender_name);
                        sendermap.put("msg","send a video");
                        sendermap.put("pic",Sender_pic);
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
                        receivermap.put("msg","send a video");
                        receivermap.put("pic",Receiver_pic);
                        receivermap.put("timestamp",formattedDate);
                        receivermap.put("date",formattedDate);

                        receivermap.put("sort",""+Calendar.getInstance().getTimeInMillis());
                        receivermap.put("status","0");
                        receivermap.put("block","0");
                        receivermap.put("read","0");
                        receivermap.put("like","0");



                        HashMap both_user_map = new HashMap<>();
                        both_user_map.put(inbox_sender_ref , receivermap);
                        both_user_map.put(inbox_receiver_ref , sendermap);

                        rootref.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                SendPushNotification("Send a video file");
                            }
                        });
                    }
                });
            }



    public void save_pdf_in_firebase(){
        DatabaseReference dref=rootref.child("chat").child(Sender_id+"-"+Receiverid).push();
        final String key=dref.getKey();

        reference.child("Document").child(key+".pdf").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                stopSelf();
                Upload_pdf(taskSnapshot.getDownloadUrl().toString(),key);
            }
        });
    }

    // this method will upload the image in chhat
    public void Upload_pdf(String url, String key){
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);



        final String current_user_ref = "chat" + "/" + Sender_id + "-" + Receiverid;
        final String chat_user_ref = "chat" + "/" + Receiverid + "-" + Sender_id;


        HashMap message_user_map = new HashMap<>();
        message_user_map.put("receiver_id", Receiverid);
        message_user_map.put("sender_id", Sender_id);
        message_user_map.put("chat_id",key);
        message_user_map.put("text", "");
        message_user_map.put("type","pdf");
        message_user_map.put("pic_url",url);
        message_user_map.put("status", "0");
        message_user_map.put("time", "");
        message_user_map.put("sender_name", Variables.user_name);
        message_user_map.put("timestamp", formattedDate);

        HashMap user_map = new HashMap<>();

        user_map.put(current_user_ref + "/" + key, message_user_map);
        user_map.put(chat_user_ref + "/" + key, message_user_map);

        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                String inbox_sender_ref = "Inbox" + "/" + Sender_id + "/" + Receiverid;
                String inbox_receiver_ref = "Inbox" + "/" + Receiverid + "/" + Sender_id;

                HashMap<String, String> sendermap=new HashMap<>();
                sendermap.put("id",Sender_id);
                sendermap.put("name",Sender_name);
                sendermap.put("message","send a video");
                sendermap.put("pic",Sender_pic);
                sendermap.put("status","0");
                sendermap.put("type","user");
                sendermap.put("timestamp",formattedDate);

                HashMap<String, String> receivermap=new HashMap<>();
                receivermap.put("id",Receiverid);
                receivermap.put("name",Receiver_name);
                receivermap.put("message","send a Pdf");
                receivermap.put("pic",Receiver_pic);
                receivermap.put("status","0");
                receivermap.put("type","user");
                receivermap.put("timestamp",formattedDate);

                HashMap both_user_map = new HashMap<>();
                both_user_map.put(inbox_sender_ref , receivermap);
                both_user_map.put(inbox_receiver_ref , sendermap);

                rootref.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        SendPushNotification("Send a Pdf file");
                    }
                });
            }
        });
    }

    public void SendPushNotification(String message){
        if(!token.equals("null")){

        Map<String, String> notimap= new HashMap<>();
        notimap.put("name",Sender_name);
        notimap.put("message",message);
        notimap.put("picture", Sender_pic);
        notimap.put("token",token);
        notimap.put("RidorGroupid", Sender_id);
        notimap.put("FromWhere", "user");
        rootref.child("notifications").child(Sender_id).push().setValue(notimap);
        }
    }
}