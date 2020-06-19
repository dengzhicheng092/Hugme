package com.qboxus.hugme.All_Activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;

import static com.qboxus.hugme.Code_Classes.Variables.user_gender;

public class Basic_info_A extends AppCompatActivity {

    Toolbar tb;
    ImageView iv;
    TextView name_id, birthday_id;
    Context context;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    RadioButton radio_female_RB_id, radio_male_RB_id;

    String user_gender_from_pre_page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        context = Basic_info_A.this;

        String first_name = Functions.get_info(context ,"first_name");
        String birthday = Functions.get_info(context,"birthday");
        String gender = Functions.get_info(context,"gender");

        try{
            Intent intent = getIntent();
            user_gender_from_pre_page = intent.getStringExtra("gender");

        }catch (Exception b){

        }

        radioGroup = (RadioGroup) findViewById(R.id.basic_info_RG_id);
        birthday_id = findViewById(R.id.birthday_id);
        radio_female_RB_id = findViewById(R.id.female_RB_id);
        radio_male_RB_id = findViewById(R.id.male_RB_id);
        user_gender = gender;
        if(gender.equals("male")){
            // If gender is male
            radio_male_RB_id.setChecked(true);
        }else if(gender.equals("female")){
            radio_female_RB_id.setChecked(true);
        }

        if(user_gender_from_pre_page != null){
            // If not Nul
            if(user_gender_from_pre_page.equals("male") || user_gender_from_pre_page.equals("Male")){
                // If gender is mAle
                radio_male_RB_id.setChecked(true);
            }else if(user_gender_from_pre_page.equals("female") || user_gender_from_pre_page.equals("Female")){
                radio_female_RB_id.setChecked(true);
            }

        }else if(!user_gender_from_pre_page.isEmpty()){
             // If not empty
            if(user_gender_from_pre_page.equals("male")){
                // If gender is mAle
                radio_male_RB_id.setChecked(true);
            }else if(user_gender_from_pre_page.equals("female")){
                radio_female_RB_id.setChecked(true);
            }
        }

        tb = (Toolbar) findViewById(R.id.basic_info_TB_id);
        iv = (ImageView) tb.findViewById(R.id.basic_info_back_id);
        name_id = findViewById(R.id.name_id);
        name_id.setText("Name\n" + first_name);
        birthday_id.setText("Birthday\n" + birthday);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                user_gender = radioButton.getTag().toString();
                finish();
            }
        });
    }
}
