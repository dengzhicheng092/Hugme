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
import android.widget.TextView;

import com.qboxus.hugme.Code_Classes.RootFragment;
import com.qboxus.hugme.R;

import static com.qboxus.hugme.All_Activities.Segments_A.spb;
import static com.qboxus.hugme.All_Activities.Segments_A.vp;

public class Segment_gender_F extends RootFragment {

    View view;
    Button btn;
    Context context;
    public static String signup_gender ;
    TextView male,female,signin;
    public static String gender;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_segment_gender, null);
        context = getContext();

        male = (TextView) view.findViewById(R.id.male_TV_id);
        female = (TextView) view.findViewById(R.id.female_TV_id);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = male.getText().toString();
                move_next();
            }
        });


        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = female.getText().toString();
                move_next();
            }
        });

        return view;
    }


    public void move_next () {

        vp.setCurrentItem(1);
        spb.setCompletedSegments(1);

    }
}
