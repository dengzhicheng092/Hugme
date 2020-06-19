package com.qboxus.hugme.All_Activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;

import static com.qboxus.hugme.All_Activities.Edit_profile_A.user_smoking;

public class Smoking_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1,iv2;
    RelativeLayout RL;
    TextView tv, title;
    String[] list = {"No answer","I'm a heavy smoker","I hate smoking","i don't like it","i'm a social smoker","I smoke occasionally"};
    Context context;
    String smoking_from_pre;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoking);
        context = Smoking_A.this;
        tb = (Toolbar) findViewById(R.id.smoking_TB_id);
        iv1 = (ImageView) tb.findViewById(R.id.smoking_back_id);
        title = tb.findViewById(R.id.title);
        RL = (RelativeLayout) findViewById(R.id.smoking_RL_id);
        tv = (TextView) findViewById(R.id.smoking_tv_id);

        iv1.setOnClickListener(this);
        RL.setOnClickListener(this);


        try{
            Intent intent = getIntent();
            smoking_from_pre = intent.getStringExtra("smoking"); //if it's a string you stored.

        }catch (Exception b){

        }


        listView = (ListView) findViewById(R.id.simple_list);

        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.arr_list_smoke);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Variables.Var_smoking = Variables.arr_list_smoke[position];

                user_smoking.setText("" + Variables.arr_list_smoke[position]);

            }

        });



        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

       try{
            int select_sec = Arrays.asList( Variables.arr_list_smoke).indexOf("" + smoking_from_pre);
            listView.setItemChecked(select_sec, true);

        }catch (Exception u){

       }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.smoking_back_id:

                finish();
                break;
            case R.id.smoking_RL_id:
                Functions.showlistdialog(Smoking_A.this,"Smoking_A", list, tv);
                break;
        }
    }
}
