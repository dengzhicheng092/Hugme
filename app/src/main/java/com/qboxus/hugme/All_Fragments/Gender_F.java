package com.qboxus.hugme.All_Fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.All_Activities.Edit_profile_VP_A;
import com.qboxus.hugme.All_Adapters.Profile_Options_Adapter;
import com.qboxus.hugme.All_Model_Classes.Recyclerview_GetSet;
import com.qboxus.hugme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Gender_F extends RootFragment {

    View view;
    RecyclerView rv;
    Profile_Options_Adapter adapter;
    ArrayList<Recyclerview_GetSet> list;

    public Gender_F() {
        // Required empty public constructor
    }

    Context context;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gender, container, false);
        context = getContext();
        rv = (RecyclerView) view.findViewById(R.id.rv_id);

        list = new ArrayList<>();
        list.add(new Recyclerview_GetSet("Male"));
        list.add(new Recyclerview_GetSet("Female"));
        list.add(new Recyclerview_GetSet("I dont want to disclose"));
        list.add(new Recyclerview_GetSet("No Way"));
        list.add(new Recyclerview_GetSet("Ask Me"));
//        list.add(new Recyclerview_GetSet());


        listView = (ListView) view.findViewById(R.id.simple_list);

        final ArrayAdapter<String> adapter_1

                = new ArrayAdapter<String>(context,

                android.R.layout.simple_list_item_single_choice,

                Variables.arr_list_gender);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // SparseBooleanArray checked = listView.getCheckedItemPositions();
                //  Functions.toast_msg(context,"Pos " + position + " Val  " + Variables.arr_list_living[position]);

                Variables.user_gender = Variables.arr_list_gender[position];

                int adaptor_position_total = Edit_profile_VP_A.adp.getCount();
                int adp_current_item_position = Edit_profile_VP_A.vp.getCurrentItem()+1;

                if(adaptor_position_total == adp_current_item_position){
                   // Functions.toast_msg(context,"Equal " + " Current " +adp_current_item_position + " Total " + adaptor_position_total );
                    // Calling API
                    Edit_profile_VP_A.create_Json_for_API(context);
                    getActivity().finish();
                }else{
                    // If pager element is not last

                    Edit_profile_VP_A.vp.setCurrentItem(Edit_profile_VP_A.vp.getCurrentItem()+1);
                    Edit_profile_VP_A.frag_counter.setText((Edit_profile_VP_A.vp.getCurrentItem() + 1)+"/ " + Edit_profile_VP_A.adp.getCount());
                    Edit_profile_VP_A.color_rl.setBackgroundColor(getResources().getColor(R.color.pink));

                    Edit_profile_VP_A.get_fragment_name(adp_current_item_position);

                }

            }

        });


        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter_1);




        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) Edit_profile_VP_A.vp_rl.getLayoutParams();
        lp1.height = (int) (Variables.height/2);

        Edit_profile_VP_A.vp_rl.setLayoutParams(lp1);

        Edit_profile_VP_A.next.setVisibility(View.VISIBLE);

        adapter = new Profile_Options_Adapter(getContext(), list, new Adapter_ClickListener() {
            @Override
            public void On_Item_Click(int postion, Object Model, View view) {

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
