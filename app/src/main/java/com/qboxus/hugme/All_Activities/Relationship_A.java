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

import static com.qboxus.hugme.All_Activities.Edit_profile_A.user_relationship;

public class Relationship_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1,iv2;
    RadioGroup rg;
    Context context;
    TextView title;
    RadioButton radioButton;

    ListView listView;
    String relation_from_pre;
    //Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);

        context = Relationship_A.this;
        tb = (Toolbar) findViewById(R.id.relationship_TB_id);
        iv1 = (ImageView) tb.findViewById(R.id.relationship_back_id);
        title = findViewById(R.id.title);

        listView = (ListView) findViewById(R.id.simple_list);

        try{
            Intent intent = getIntent();
            relation_from_pre = intent.getStringExtra("relation");

        }catch (Exception b){

        }


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice,
                Variables.arr_list_relationship);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Variables.Var_relationship = Variables.arr_list_relationship[position];

                user_relationship.setText("" + Variables.arr_list_relationship[position]);


            }

        });



        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        //listView.setItemChecked(select, true);
        try{
            int select_sec = Arrays.asList( Variables.arr_list_relationship).indexOf("" + relation_from_pre);
            listView.setItemChecked(select_sec, true);
        }catch (Exception b){

        }

        iv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relationship_back_id:
                addListenerOnButton();
                finish();
                break;
        }
    }


    public void addListenerOnButton() {

        rg = (RadioGroup) findViewById(R.id.relationship_RG_id);
        int selectedId = rg.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
    }
}
