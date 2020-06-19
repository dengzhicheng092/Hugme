package com.qboxus.hugme.All_Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

import static com.qboxus.hugme.All_Activities.Segments_A.spb;
import static com.qboxus.hugme.All_Activities.Segments_A.vp;

public class Segment_name_F extends RootFragment {

    View v;
    Button btn;

    public static String signup_name ;


    EditText edit_name;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_name, null);

        edit_name = v.findViewById(R.id.name_ET_id);
        context = getContext();


        String name = SharedPrefrence.get_social_info(context,
                "" + SharedPrefrence.share_social_info,
                "name"
                );
        edit_name.setText("" + name);

        btn = (Button) v.findViewById(R.id.continue_btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signup_name = edit_name.getText().toString();

                if(signup_name.isEmpty()){
                    edit_name.setError("Please fill this");
                }else{
                    vp.setCurrentItem(2);
                    spb.setCompletedSegments(2);
                }


            }
        });

        return v;
    }
}
