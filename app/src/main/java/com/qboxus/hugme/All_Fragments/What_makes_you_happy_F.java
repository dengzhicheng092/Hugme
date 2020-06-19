package com.qboxus.hugme.All_Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Activities.Edit_profile_VP_A;
import com.qboxus.hugme.All_Adapters.List_CustomAdapter;
import com.qboxus.hugme.All_Model_Classes.List_GetSet;
import com.qboxus.hugme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class What_makes_you_happy_F extends RootFragment {

    View view;
    ListView lv;

    private ArrayList<List_GetSet> modelArrayList;
    private List_CustomAdapter customAdapter;

    private  String[] animallist = new String[]{"Music", "Football", "Listening to music", "Relaxing","Comedy"};




    public What_makes_you_happy_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_what_makes_you_happy, container, false);

        lv = (ListView) view.findViewById(R.id.listview_id);


        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) Edit_profile_VP_A.vp_rl.getLayoutParams();
        lp1.height = (int) (Variables.height/2);

        Edit_profile_VP_A.vp_rl.setLayoutParams(lp1);

        Edit_profile_VP_A.next.setVisibility(View.VISIBLE);

        modelArrayList = getModel(false);
        customAdapter = new List_CustomAdapter(getContext(), modelArrayList, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {
                Edit_profile_VP_A.vp.setCurrentItem(Edit_profile_VP_A.vp.getCurrentItem()+1);
                Edit_profile_VP_A.frag_counter.setText((Edit_profile_VP_A.vp.getCurrentItem() + 1)+"/11");
                Edit_profile_VP_A.color_rl.setBackgroundColor(getResources().getColor(R.color.lightblue));

                METHOD_hidelinearlayout(Edit_profile_VP_A.gender_ll, Edit_profile_VP_A.desc_ll, Edit_profile_VP_A.happy_ll,
                        Edit_profile_VP_A.relation_ll, Edit_profile_VP_A.kids_ll, Edit_profile_VP_A.living_ll, Edit_profile_VP_A.drink_ll,
                        Edit_profile_VP_A.smoke_ll, Edit_profile_VP_A.profqs_ll, Edit_profile_VP_A.bodytype_ll, Edit_profile_VP_A.missing_ll);

            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });
        lv.setAdapter(customAdapter);




        return view;
    }







    private ArrayList<List_GetSet> getModel(boolean isSelect){
        ArrayList<List_GetSet> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){

            List_GetSet model = new List_GetSet();
            model.setSelected(isSelect);
            model.setText(animallist[i]);
            list.add(model);
        }
        return list;
    }







    private void METHOD_hidelinearlayout(LinearLayout ll1, LinearLayout ll2, LinearLayout ll3, LinearLayout ll4,
                                         LinearLayout ll5, LinearLayout ll6, LinearLayout ll7, LinearLayout ll8,
                                         LinearLayout ll9, LinearLayout ll10,LinearLayout ll11){

        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        ll4.setVisibility(View.GONE);
        ll5.setVisibility(View.GONE);
        ll6.setVisibility(View.GONE);
        ll7.setVisibility(View.GONE);
        ll8.setVisibility(View.GONE);
        ll9.setVisibility(View.GONE);
        ll10.setVisibility(View.GONE);
        ll11.setVisibility(View.GONE);

    }

}
