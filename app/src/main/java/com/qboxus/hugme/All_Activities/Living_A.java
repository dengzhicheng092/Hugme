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

import static com.qboxus.hugme.All_Activities.Edit_profile_A.user_living;

public class Living_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1,iv2;
    String[] list = {"No answer","By myself","Student residence","With parents","With partner","With housemate(s)"};
    RelativeLayout RL;
    TextView tv, title;
    Context context;
    ListView listView;
    String living_from_pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living);
        context = Living_A.this;
        tb = (Toolbar) findViewById(R.id.living_TB_id);
        iv1 = (ImageView) tb.findViewById(R.id.living_back_id);
        title = tb.findViewById(R.id.title);
        RL = (RelativeLayout) findViewById(R.id.living_RL_id);
        tv = (TextView) findViewById(R.id.living_tv_id);

        try{
            Intent intent = getIntent();
            living_from_pre = intent.getStringExtra("living"); //if it's a string you stored.

        }catch (Exception b){

        }


        iv1.setOnClickListener(this);
        RL.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.simple_list);

        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.arr_list_living);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Variables.Var_living = Variables.arr_list_living[position];

                user_living.setText("" + Variables.arr_list_living[position]);


            }

        });



        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
       // listView.setItemChecked(select, true);
//        if(select == -1){
        try{
            int select_sec = Arrays.asList( Variables.arr_list_living).indexOf("" + living_from_pre);
            //Functions.toast_msg(context,"New " + select_sec + " Selec " + select);
            listView.setItemChecked(select_sec, true);

        }catch (Exception n){

        }

        //}



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.living_back_id:

                finish();
                break;
            case R.id.living_RL_id:
                Functions.showlistdialog(Living_A.this,"Living_F",list,tv);
                break;
        }
    }
}
