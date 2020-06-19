package com.qboxus.hugme.All_Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.BuildConfig;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;

import java.io.File;
import java.io.FileOutputStream;

public class Share_Profile_A extends AppCompatActivity implements View.OnClickListener {


    SimpleDraweeView userimage_img;
    TextView username;

    RelativeLayout profile_card_layout;
    String user_image,user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_profile);


        profile_card_layout=findViewById(R.id.profile_card_layout);
        username=findViewById(R.id.username);
        userimage_img=findViewById(R.id.userimage_img);

        Intent intent=getIntent();

        if(intent!=null) {

             user_image = intent.getStringExtra("image");
             user_name = intent.getStringExtra("name");


            username.setText(user_name);
            if (user_image != null && !user_image.equals("")) {
                Uri uri = Uri.parse(user_image);
                userimage_img.setImageURI(uri);
            }


        }

        findViewById(R.id.share_profile_btn).setOnClickListener(this::onClick);
        findViewById(R.id.back_btn).setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_profile_btn:
                share();
                break;

            case R.id.back_btn:
                finish();
                break;
        }
    }


    private void share(){
        try {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            Uri bitmapUri = Change_Layout_Bitmap_image(profile_card_layout);
            intent.setType("image/*");
            intent.setType("text/plain");
            String link = "Check this person of name:"+user_name+"\n\n"
                    +"https://play.google.com/store/apps/details?id="
                    + getPackageName();
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            intent.putExtra(Intent.EXTRA_TEXT, link);
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

}


    public Uri Change_Layout_Bitmap_image(RelativeLayout view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        return bitmapUri;

    }



}
