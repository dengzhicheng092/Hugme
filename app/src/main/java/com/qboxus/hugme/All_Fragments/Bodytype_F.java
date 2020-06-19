package com.qboxus.hugme.All_Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Activities.Edit_profile_VP_A;
import com.qboxus.hugme.All_Adapters.Profile_Options_Adapter;
import com.qboxus.hugme.All_Model_Classes.Recyclerview_GetSet;
import com.qboxus.hugme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bodytype_F extends RootFragment {

    View view;
    RecyclerView rv;
    Profile_Options_Adapter adapter;
    ArrayList<Recyclerview_GetSet> list;


    public Bodytype_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bodytype, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rv_id);

        list = new ArrayList<>();
        Functions.Add_values_to_RV(list, Variables.arr_list_smoke);





        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) Edit_profile_VP_A.vp_rl.getLayoutParams();
        lp1.height = (int) (Variables.height/2);

        Edit_profile_VP_A.vp_rl.setLayoutParams(lp1);

        Edit_profile_VP_A.next.setVisibility(View.VISIBLE);



        adapter = new Profile_Options_Adapter(getContext(), list, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {
                Edit_profile_VP_A.vp.setCurrentItem(Edit_profile_VP_A.vp.getCurrentItem() + 1);
                Edit_profile_VP_A.frag_counter.setText((Edit_profile_VP_A.vp.getCurrentItem() + 1)+"/11");
                Edit_profile_VP_A.color_rl.setBackgroundColor(getResources().getColor(R.color.green));

                METHOD_hidelinearlayout(Edit_profile_VP_A.living_ll, Edit_profile_VP_A.desc_ll, Edit_profile_VP_A.happy_ll,
                        Edit_profile_VP_A.gender_ll, Edit_profile_VP_A.kids_ll, Edit_profile_VP_A.bodytype_ll, Edit_profile_VP_A.drink_ll,
                        Edit_profile_VP_A.smoke_ll, Edit_profile_VP_A.profqs_ll, Edit_profile_VP_A.relation_ll, Edit_profile_VP_A.missing_ll);

            }

            @Override
            public void On_Long_Item_Click(int postion, Object Model, View view) {

            }
        });

        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rv.setAdapter(adapter);

        return view;
    }


    private void METHOD_hidelinearlayout(LinearLayout ll1, LinearLayout ll2, LinearLayout ll3, LinearLayout ll4,
                                         LinearLayout ll5, LinearLayout ll6, LinearLayout ll7, LinearLayout ll8,
                                         LinearLayout ll9, LinearLayout ll10, LinearLayout ll11){

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
