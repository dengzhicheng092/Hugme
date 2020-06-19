package com.qboxus.hugme.Code_Classes;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qboxus.hugme.All_Fragments.Swipe_F;
import com.qboxus.hugme.All_Model_Classes.Chat_GetSet;
import com.qboxus.hugme.All_Model_Classes.Recyclerview_GetSet;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;
import com.qboxus.hugme.Utils.TimeAgo2;
import com.qboxus.hugme.Volley_Package.Api_Links;
import com.qboxus.hugme.Volley_Package.CallBack;
import com.qboxus.hugme.Volley_Package.ApiRequest;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.DOWNLOAD_SERVICE;

public class Functions {

    public static void Log_d_msg(Context contect, String Msg) {
        Log.d(Variables.tag,  Msg);
    }

    public static void toast_msg(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
        Functions.Log_d_msg(context, "Log \n " + msg);
    }


    public static boolean isEmailValid (String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }


    public static void alert_dialogue(final Context context, String title, String msg) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle("" + title);
            builder1.setMessage(msg);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();

                        }
                    });


            AlertDialog alert11 = builder1.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                alert11.show();

            }else{
                alert11.show();
            }


    }






    public static Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {

            return null;
        }
    }


    public static void display_fb_ad (final Context context){

        final String  TAG = "Main";
        final com.facebook.ads.InterstitialAd interstitialAd;
        interstitialAd = new com.facebook.ads.InterstitialAd(context, "YOUR_PLACEMENT_ID");
        int num_click_post = SharedPrefrence.get_int(context,  SharedPrefrence.u_click);

         interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
                SharedPrefrence.save_int(context,0, SharedPrefrence.u_click);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                SharedPrefrence.save_int(context,0, SharedPrefrence.u_click);

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage() + " Code " + adError.getErrorCode());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

          interstitialAd.loadAd();

             if(num_click_post == Variables.Var_num_click_to_show_Ads){
                 interstitialAd.loadAd();


        }

    }


    public static int Count_num_click (Context context) {

        int num_click_post = SharedPrefrence.get_int(context,  SharedPrefrence.u_click);

        SharedPrefrence.save_int(context, num_click_post+1, SharedPrefrence.u_click);

            if(num_click_post >= Variables.Var_num_click_to_show_Ads ){
                num_click_post = 1;
                SharedPrefrence.save_int(context, num_click_post, SharedPrefrence.u_click);

            }

        return num_click_post;

    }




    public static int getAge (String dobString){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }



        return age;
    }


    public static Drawable getRoundRect() {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                25, 25, 25, 25,
                0, 25, 25, 25
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);

        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        return shapeDrawable;
    }

    public static String parseDateToddMMyyyy (String time, Context context) {

        String inputPattern = "dd-MM-yyyy HH:mm:ss";
        String outputPattern = "yyyy-MM-dd HH:mm:ss";

        //  yyyy-MM-dd HH:mm:ss

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
         }

        return str;
    }


       public static String get_time_ago_org (String date_time){

        TimeAgo2 timeAgo2 = new TimeAgo2();
        String MyFinalValue = timeAgo2.covertTimeToText(date_time);

        return MyFinalValue;

    }



    public static long DownloadData_forChat (Context context, Chat_GetSet item) {

        long downloadReference;
         DownloadManager downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(item.getPic_url()));

         request.setTitle(item.getSender_name());

        if(item.getType().equals("video")) {
            request.setDescription("Downloading Video");
            request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Chatbuzz/" + item.getChat_id() + ".mp4")));
        }
        else if(item.getType().equals("audio")){
            request.setDescription("Downloading Audio");
            request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Chatbuzz/" + item.getChat_id() + ".mp3")));
        }
        else if(item.getType().equals("pdf")){

            request.setDescription("Downloading Pdf Document");
            request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Chatbuzz/" + item.getChat_id() + ".pdf")));

        }


        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }


    public static boolean isMyServiceRunning(Context context,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }



    public static void delete_Account (final Context context, String fb_id){
            Functions.Show_loader(context,false,false);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fb_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(
                context,
                "" + Api_Links.deleteAccount,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String response) {
                        Functions.cancel_loader();
                        try{
                           SharedPrefrence.logout_user(context);
                            JSONObject response_1 = new JSONObject(response);
                            JSONArray msg_arr = response_1.getJSONArray("msg");
                            Functions.toast_msg(context,"" + msg_arr.getJSONObject(0).getString("response"));

                        }catch (Exception b){

                        }


                    }
                }


        );





    }



    public static void delete_image (final Context context, String image_link, final String fb_id){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("image_link", image_link);
            parameters.put("fb_id", fb_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(
                context,
                "" + Api_Links.deleteImages,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String response) {
                      try{
                            JSONObject response_1 = new JSONObject(response);
                            JSONArray msg_arr = response_1.getJSONArray("msg");
                            Functions.toast_msg(context,"" + msg_arr.getJSONObject(0).getString("response"));

                            get_user_info(fb_id,context);

                        }catch (Exception b){

                        }


                    }
                }


        );





    }



    // TODO: Report User

    public static void report_user (final Context context, String fb_id){

         Functions.Show_loader(context,false,false);

        String my_id = Functions.get_info(context,"fb_id");
            JSONObject parameters = new JSONObject();
            try {
                parameters.put("fb_id", fb_id);
                parameters.put("my_id", my_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ApiRequest.Call_Api(
                    context,
                    "" + Api_Links.flat_user,
                    parameters,
                    new CallBack() {
                        @Override
                        public void Get_Response(String requestType, String response) {
                            Functions.cancel_loader();

                            try{
                                JSONObject response_1 = new JSONObject(response);
                                JSONArray msg_arr = response_1.getJSONArray("msg");
                                Functions.toast_msg(context,"" + msg_arr.getJSONObject(0).getString("response"));

                            }catch (Exception b){

                            }



                        }
                    }


            );

    }
     public static void API_firstchat (final Context context, String fb_id, String effected_id){

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fb_id);
            parameters.put("effected_id", effected_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(
                context,
                "" + Api_Links.firstchat,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String response) {



                    }
                }


        );





    }

    public static String get_info (Context context, String val_want){
        String get_val = "";

            get_val = SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    val_want
            );


            if(val_want.equals("fb_id")){
                SharedPrefrence.save_string(context,"" + get_val, "" + SharedPrefrence.u_id);
            }


        return get_val;


    }


    public static Uri resourceToUri (Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID) );
    }



    public static void Add_values_to_RV (ArrayList<Recyclerview_GetSet> list, String[] arr_val){

        for(int i=0; i< arr_val.length; i++){
            list.add(new Recyclerview_GetSet("" + arr_val[i] ) );
        } // End For Loop



    }

      public static void Show_Banner_ad ( Context context, RelativeLayout layout){
       AdView adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(context.getResources().getString(R.string.banner_ad_unit_id));


        RelativeLayout.LayoutParams right_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        right_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        right_params.setMargins(20,20,20,0);
        adView.setLayoutParams(right_params); //causes layout updat

          layout.addView(adView);
         AdRequest adRequest = new AdRequest.Builder()
                .build();

        adView.loadAd(adRequest);


    }





   public static Dialog dialog;
    public static void Show_loader(Context context, boolean outside_touch, boolean cancleable) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_loading_layout);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.d_round_white_background_10));

        CamomileSpinner loader=dialog.findViewById(R.id.loader);
        loader.start();

        if(!outside_touch)
           dialog.setCanceledOnTouchOutside(false);

        if(!cancleable)
            dialog.setCancelable(false);

        dialog.show();

    }


    public static void cancel_loader(){
        try{
            if(dialog!=null){
               dialog.dismiss();
                dialog.cancel();
            }

        }catch (Exception b){

        }
    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    public static void get_user_info (String user_id, final Context context) {
         JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(
                context,
                Api_Links.getUserInfo,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String resp) {
                        try {




                            JSONObject response = new JSONObject(resp);


                            if (response.getString("code").equals("200")) {


                                JSONArray msg_obj = response.getJSONArray("msg");
                                JSONObject user_info_obj = msg_obj.getJSONObject(0);

                                  SharedPrefrence.save_string(context, "" + user_info_obj.toString(),
                                        "" + SharedPrefrence.u_login_detail);
                            }

                        } catch (Exception b) {

                        }


                    }
                }
        );

    }

    public static void share_link(Context context){
        try {
            String name = SharedPrefrence.get_social_info(context,
                    "" + SharedPrefrence.u_login_detail,
                    "first_name"
            );

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Profile_F");
            String shareMessage= "\n I am "+name+" This Is sharing Profile_F.  \n\n";
           shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));

        } catch(Exception e) {

        }
    }



    public static void other_user_profile_share (Context context, String user_name, String image_url){
        BitmapDrawable bitmapDrawable;
        Bitmap bitmap1;

        bitmap1 = getBitmapFromURL(image_url);

        String imgBitmapPath= MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap1,"Sharing Profile_F",null);
        Uri imgBitmapUri=Uri.parse(imgBitmapPath);
        String shareText="Share Profile_F of " + user_name + ", member .";
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM,imgBitmapUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(shareIntent,"Sharing Profile_F"));


    }
    public static void Show_or_hide_profile (String user_id, String status ,final Context context) {

         Functions.Show_loader(context, false, false);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);
            parameters.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(
                context,
                Api_Links.show_or_hide_profile,
                parameters,
                new CallBack() {
                    @Override
                    public void Get_Response(String requestType, String resp) {
                        try {


                            Functions.cancel_loader();

                            JSONObject response = new JSONObject(resp);


                            if (response.getString("code").equals("200")) {

                                  JSONArray msg_arr = response.getJSONArray("msg");
                                Functions.toast_msg(context,"" + msg_arr.getJSONObject(0).getString("response"));


                            }

                        } catch (Exception b) {

                        }


                    }
                }
        );

    }
     public static void send_push_notification (final Context context, final String receverid, final String message, final String noti_type){



        Query inbox_query;
        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();


        inbox_query = rootref.child("Users").child(receverid);
        inbox_query.keepSynced(true);


        inbox_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                }else {
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        Swipe_F.token_other_user = ds.getValue().toString();

                        JSONObject notimap = new JSONObject();
                        try {
                            notimap.put("title", Functions.get_info(context,"first_name"));
                            notimap.put("message", message);
                            notimap.put("icon", Functions.get_info(context,"image1"));
                            notimap.put("tokon", Swipe_F.token_other_user);
                            notimap.put("senderid", "" + Functions.get_info(context,"fb_id"));
                            notimap.put("receiverid", receverid);
                            notimap.put("action_type", "" + noti_type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ApiRequest.Call_Api(
                                context,
                                Api_Links.sendPushNotification,
                                notimap,
                                new CallBack() {
                                    @Override
                                    public void Get_Response(String requestType, String resp) {
                                        try {

                                            Functions.cancel_loader();

                                        } catch (Exception b) {

                                        }


                                    }
                                }
                        );

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        if(noti_type.equals("message")){

            Log.d("hugme ","msg type api called  ");

            JSONObject notimap = new JSONObject();
            try {
                notimap.put("title", Functions.get_info(context,"first_name"));
                notimap.put("message", message);
                notimap.put("icon", Functions.get_info(context,"image1"));
                notimap.put("tokon", Swipe_F.token_other_user);
                notimap.put("senderid", "" + Functions.get_info(context,"fb_id"));
                notimap.put("receiverid", receverid);
                notimap.put("action_type", "" + noti_type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("hugme ","like data is "+notimap + "link is "+   Api_Links.sendPushNotification);

            ApiRequest.Call_Api(
                    context,
                    Api_Links.sendPushNotification,
                    notimap,
                    new CallBack() {
                        @Override
                        public void Get_Response(String requestType, String resp) {
                            try {

                                Log.d("hugme ","responce of api is  "+resp);
                                Functions.cancel_loader();

                            } catch (Exception b) {

                            }


                        }
                    }
            );

        }



    }


    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

    public static void openBrowser (final Context context, String url) {

        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Choose browser"));// Choose browser is arbitrary :)

    }


    public static void showlistdialog(Activity activity, String title, final String[] item_list, final TextView textView) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);

        builder.setSingleChoiceItems( item_list, 0, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                String selection = item_list[item];
                textView.setText(selection);

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog1= builder.create();
        alertDialog1.show();

    }


    public static void Opendate_picker(Context context, final EditText editText){
        final SimpleDateFormat format= new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());


        String dateString=editText.getText().toString();
        if(dateString.equals("")){
            dateString="01/01/2000";
        }

        String[] parts = dateString.split("/");

        DatePickerDialog mdiDialog =new DatePickerDialog(context, R.style.datepicker_style,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                Date chosenDate = cal.getTime();
                editText.setText(format.format(chosenDate));

            }
        }, Integer.parseInt(parts[2]),Integer.parseInt(parts[0])-1,Integer.parseInt(parts[1]));
        mdiDialog.show();
    }


    public static String get_Random_string(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


    public static String convertSeconds(int seconds){
        int h = seconds/ 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        String sh = (h > 0 ? String.valueOf(h) + " " + "h" : "");
        String sm = (m < 10 && m > 0 && h > 0 ? "0" : "") + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : String.valueOf(m) + " " + "min") : "");
        String ss = (s == 0 && (h > 0 || m > 0) ? "" : (s < 10 && (h > 0 || m > 0) ? "0" : "") + String.valueOf(s) + " " + "sec");
        return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "")+ss;
    }


    public static void Show_Alert(Context context,String title,String description,CallBack callBack){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(callBack!=null)
                callBack.Get_Response("alert","OK");
            }
        });
        builder.create();
        builder.show();

}

    static BroadcastReceiver broadcastReceiver;
    static IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    public static void unRegisterConnectivity(Context mContext) {
        try {
                mContext.unregisterReceiver(broadcastReceiver);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void RegisterConnectivity(Context context, final CallBack callback) {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnectedToInternet(context)) {
                    callback.Get_Response("alert","connected");
                } else {
                    callback.Get_Response("alert","disconnected");
                }
            }
        };

        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static Boolean isConnectedToInternet(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("NetworkChange", e.getMessage());
            return false;
        }
    }



}
