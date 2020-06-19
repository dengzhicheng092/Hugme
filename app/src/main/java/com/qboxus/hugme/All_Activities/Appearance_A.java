package com.qboxus.hugme.All_Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;

public class Appearance_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1,iv2;
    RelativeLayout height_RL,weight_RL,body_RL,eye_RL,hair_RL;
    TextView height_tv,weight_tv,body_tv,eye_tv,hair_tv;

    String[] height = {"No answer","<39 kg (<86 lbs)","41 kg (90 lbs)","No answer","<39 kg (<86 lbs)","41 kg (90 lbs)","No answer","<39 kg (<86 lbs)","41 kg (90 lbs)",
            "No answer","<39 kg (<86 lbs)","41 kg (90 lbs)","No answer","<39 kg (<86 lbs)","41 kg (90 lbs)","No answer","<39 kg (<86 lbs)","41 kg (90 lbs)"};
    String[] weight = {"No answer","40 kg (88 lbs)","92 cm","No answer","40 kg (88 lbs)","92 cm","No answer","40 kg (88 lbs)","92 cm",
            "No answer","40 kg (88 lbs)","92 cm","No answer","40 kg (88 lbs)","92 cm","No answer","40 kg (88 lbs)","92 cm","No answer","40 kg (88 lbs)","92 cm"};
    String[] body_type = {"No answer","Athletic","Average","A few extra pounds","Muscular","Big and bold","slim"};
    String[] eye_color = {"No answer","Black","Blue","Brown","Muscular","Green","Grey","Hazel","Other"};
    String[] hair_color = {"No answer","Black","Blond","Brown","Dyed","Grey","Red","Shaved","White"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);

        tb = (Toolbar) findViewById(R.id.apperarnce_TB_id);
        iv1 = (ImageView) findViewById(R.id.apperarnce_back_id);

        iv1.setOnClickListener(this);

        height_RL = (RelativeLayout) findViewById(R.id.height_RL_id);
        height_tv = (TextView) findViewById(R.id.height_tv_id);

        height_RL.setOnClickListener(this);

        weight_RL = (RelativeLayout) findViewById(R.id.weight_RL_id);
        weight_tv = (TextView) findViewById(R.id.weight_tv_id);

        weight_RL.setOnClickListener(this);

        body_RL = (RelativeLayout) findViewById(R.id.body_type_RL_id);
        body_tv = (TextView) findViewById(R.id.body_type_tv_id);

        body_RL.setOnClickListener(this);

        eye_RL = (RelativeLayout) findViewById(R.id.eye_color_RL_id);
        eye_tv = (TextView) findViewById(R.id.eye_color_tv_id);

        eye_RL.setOnClickListener(this);

        hair_RL = (RelativeLayout) findViewById(R.id.hair_color_RL_id);
        hair_tv = (TextView) findViewById(R.id.hair_color_tv_id);

        hair_RL.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apperarnce_back_id:
                finish();
                break;
            case R.id.height_RL_id:
                Functions.showlistdialog(Appearance_A.this,"Height",height,height_tv);
                break;
            case R.id.weight_RL_id:
                Functions.showlistdialog(this, "Weight", weight, weight_tv);
                break;
            case R.id.body_type_RL_id:
                Functions.showlistdialog(this, "Body type", body_type, body_tv);
                break;
            case R.id.eye_color_RL_id:
                Functions.showlistdialog(this, "Eye colour", eye_color, eye_tv);
                break;
            case R.id.hair_color_RL_id:
                Functions.showlistdialog(this, "Hair colour", hair_color, hair_tv);
                break;
        }
    }
}
