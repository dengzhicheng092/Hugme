package com.qboxus.hugme.All_Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Volley_Package.Api_Links;

import static com.qboxus.hugme.Code_Classes.Variables.user_gender;

public class Setting_A extends AppCompatActivity {

    Toolbar TB;
    ImageView IV;
    TextView TV1,TV2,TV3,TV4,TV5, Chat_Inbox_text, text_app_version, setting_blocked_id, setting_about_id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = Setting_A.this;
        TB = (Toolbar) findViewById(R.id.setting_TB_id);
        IV = (ImageView) TB.findViewById(R.id.setting_back_id);
        text_app_version = findViewById(R.id.t_app_verson_1);
        TV1 = (TextView) findViewById(R.id.setting_basic_id);
        TV2 = (TextView) findViewById(R.id.setting_account_id);
        TV3 = (TextView) findViewById(R.id.setting_account_pref_id);
        TV4 = (TextView) findViewById(R.id.setting_help_id);
        TV5 = (TextView) findViewById(R.id.setting_about_id);
        Chat_Inbox_text = findViewById(R.id.Chat_Inbox);
        setting_blocked_id = findViewById(R.id.setting_blocked_id);
        setting_about_id = findViewById(R.id.setting_about_id);

        setting_blocked_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent_gender = new Intent(Setting_A.this, Blocked_User_A.class);
                startActivity(myIntent_gender);
            }
        });


        setting_about_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              try{
                    Functions.openBrowser(context, Api_Links.App_Privacy_Policy_new);
                }catch (Exception vq){
               }


            }
        });


        // TODO: Load FB Full Page Ads :-S
        Functions.display_fb_ad(context);


        try{
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            text_app_version.setText("Version: " + info.versionName);  // todo: Dynamica Version name


        }catch (Exception b){
            Functions.toast_msg(context," " + b.toString());
        }


        Chat_Inbox_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting_A.this, Chat_Inbox_A.class));
            }
        });

        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent_gender = new Intent(Setting_A.this, Basic_info_A.class);
                myIntent_gender.putExtra("gender", "" + user_gender);
                startActivity(myIntent_gender);


            }
        });

        TV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting_A.this, Account_A.class));
            }
        });

        TV3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Functions.openBrowser(context,Api_Links.App_Privacy_Policy_new);
                }catch (Exception vq){

                }
            }
        });
    }
}
