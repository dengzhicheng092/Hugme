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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;

import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;

import static com.qboxus.hugme.All_Activities.Edit_profile_A.user_drinking;

public class Drinking_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1,iv2;
    RadioGroup rg;
    Context context;
    TextView title;
    RadioButton radioButton;
    String drink_from_pre;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinking);

        context = Drinking_A.this;
        title = findViewById(R.id.title);

        tb = (Toolbar) findViewById(R.id.drinking_TB_id);
        iv1 = (ImageView) tb.findViewById(R.id.drinking_back_id);
        listView = (ListView) findViewById(R.id.simple_list);

        try{
            Intent intent = getIntent();
            drink_from_pre = intent.getStringExtra("drinking"); //if it's a string you stored.

        }catch (Exception b){

        }

        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.arr_list_drink);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // SparseBooleanArray checked = listView.getCheckedItemPositions();
               //Functions.toast_msg(context,"Pos " + position + " Val  " + Variables.arr_list_drink[position]);

                Variables.Var_drinking = Variables.arr_list_drink[position];

                user_drinking.setText("" + Variables.arr_list_drink[position]);

            }

        });


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
       // listView.setItemChecked(select, true);
        try{
            int select_sec = Arrays.asList( Variables.arr_list_drink).indexOf("" + drink_from_pre);
          //  Functions.toast_msg(context,"Child New " + select_sec + " Selec " + select + " " + drink_from_pre);
            listView.setItemChecked(select_sec, true);
        }catch (Exception o){

        }

        iv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drinking_back_id:
                addListenerOnButton();
                finish();
                break;
        }
    }

    public void addListenerOnButton() {

        rg = (RadioGroup) findViewById(R.id.drinking_RG_id);
        int selectedId = rg.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

    }



}
