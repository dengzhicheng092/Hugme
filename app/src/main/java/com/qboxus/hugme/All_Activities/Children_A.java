package com.qboxus.hugme.All_Activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Adapters.Profile_Options_Adapter;
import com.qboxus.hugme.All_Model_Classes.Recyclerview_GetSet;
import com.qboxus.hugme.R;

import static com.qboxus.hugme.All_Activities.Edit_profile_A.user_children;

public class Children_A extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    ImageView iv1,iv2;
    RadioGroup rg;
    RadioButton radioButton;
    Context context;
    TextView title;
    RecyclerView rv;
    Profile_Options_Adapter adapter;
    ArrayList<Recyclerview_GetSet> list;

    ListView listView;
    String child_from_pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        context = Children_A.this;
        tb = (Toolbar) findViewById(R.id.children_TB_id);
        iv1 = (ImageView) findViewById(R.id.children_back_id);
        title = findViewById(R.id.title);
        iv1.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.simple_list);

        try{
            Intent intent = getIntent();
            child_from_pre = intent.getStringExtra("child"); //if it's a string you stored.

        }catch (Exception b){

        }


        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.arr_list_children);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

               // SparseBooleanArray checked = listView.getCheckedItemPositions();
              //  Functions.toast_msg(context,"Pos " + position + " Val  " + Variables.arr_list_children[position]);

                Variables.Var_children = Variables.arr_list_children[position];

                user_children.setText("" + Variables.arr_list_children[position]);


            }

        });



        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

       try{
            int select_sec = Arrays.asList( Variables.arr_list_children ).indexOf("" + child_from_pre);
            listView.setItemChecked(select_sec, true);

        }catch (Exception b){

       }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.children_back_id:
                //addListenerOnButton();
                finish();
                break;
        }
    }


    public void addListenerOnButton() {

        rg = (RadioGroup) findViewById(R.id.children_RG_id);
        int selectedId = rg.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        Variables.Var_children =  radioButton.getText().toString();

        user_children.setText("" + radioButton.getText());

        Edit_profile_A.get_val_from_radio_box(title,"" + radioButton.getText(),context);

        addRadioButtons(5);

    }



    public void addRadioButtons(int number) {

        for (int row = 0; row < 1; row++) {
            RadioGroup ll = new RadioGroup(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            for (int i = 1; i <= number; i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(View.generateViewId());
                rdbtn.setText("Radio " + rdbtn.getId());
                ll.addView(rdbtn);
            }
            ((ViewGroup) findViewById(R.id.children_RG_id)).addView(ll);
        }
    }


}
