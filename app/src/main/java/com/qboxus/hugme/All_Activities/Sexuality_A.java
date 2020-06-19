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

import static com.qboxus.hugme.All_Activities.Edit_profile_A.user_sex;

public class Sexuality_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1,iv2;
    RadioGroup rg;
    Context context;
    TextView title;
    RadioButton radioButton;

    String sexx_from_pre;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sexuality);
        context = Sexuality_A.this;
        tb = (Toolbar) findViewById(R.id.sex_TB_id);
        iv1 = (ImageView) tb.findViewById(R.id.sex_back_id);
        title = findViewById(R.id.title);

        listView = (ListView) findViewById(R.id.simple_list);

        try{
            Intent intent = getIntent();
            sexx_from_pre = intent.getStringExtra("sexuality"); //if it's a string you stored.

        }catch (Exception b){

        }


        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.arr_list_sexuality);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Variables.Var_sexuality = Variables.arr_list_sexuality[position];

                user_sex.setText("" + Variables.arr_list_sexuality[position]);
            }

        });



        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        try{
            int select_sec = Arrays.asList( Variables.arr_list_sexuality).indexOf("" + sexx_from_pre);
            listView.setItemChecked(select_sec, true);
        }catch (Exception m){

        }







        iv1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sex_back_id:
                finish();
                break;
        }
    }

    public void addListenerOnButton() {

        rg = (RadioGroup) findViewById(R.id.sex_RG_id);
        int selectedId = rg.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        Variables.Var_sexuality = radioButton.getText().toString();
        user_sex.setText("" + radioButton.getText().toString());
    }
}
