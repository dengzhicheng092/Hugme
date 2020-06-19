package com.qboxus.hugme.All_Activities;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;

import com.qboxus.hugme.All_Adapters.Segment_Adapter;
import com.qboxus.hugme.All_Fragments.Segment_add_pic_F;
import com.qboxus.hugme.All_Fragments.Segment_dob_F;
import com.qboxus.hugme.All_Fragments.Segment_email_F;
import com.qboxus.hugme.All_Fragments.Segment_gender_F;
import com.qboxus.hugme.All_Fragments.Segment_name_F;
import com.qboxus.hugme.R;

public class Segments_A extends AppCompatActivity {

    public static SegmentedProgressBar spb;
    public static ViewPager vp;
    Segment_Adapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segments);

        spb = (SegmentedProgressBar) findViewById(R.id.segment_bar_id);
        vp = (ViewPager) findViewById(R.id.view_pager_id);
        vp.setOffscreenPageLimit(5);


        adp = new Segment_Adapter(getSupportFragmentManager());
        adp.add_fragment(new Segment_gender_F(), "");
        adp.add_fragment(new Segment_name_F(), "");
        adp.add_fragment(new Segment_dob_F(), "");
        adp.add_fragment(new Segment_email_F(), "");
        adp.add_fragment(new Segment_add_pic_F(), "");

        vp.setAdapter(adp);
        spb.setCompletedSegments(1);

        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vp.setCurrentItem(vp.getCurrentItem());
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (vp.getCurrentItem() == 0){
            super.onBackPressed();
        }else {
            vp.setCurrentItem(vp.getCurrentItem() - 1);
        }

    }

}
