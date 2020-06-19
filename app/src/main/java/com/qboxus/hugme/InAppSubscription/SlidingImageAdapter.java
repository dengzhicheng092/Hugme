package com.qboxus.hugme.InAppSubscription;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.qboxus.hugme.R;

import java.util.ArrayList;

/**
 * Created by Dinosoftlabs on 10/18/2019.
 */

public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context, ArrayList<Integer> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_slidingimages, view, false);
         ImageView imageView=null;
         TextView textView=null;

       if(imageLayout != null){
            imageView = (ImageView) imageLayout
                   .findViewById(R.id.image);
            textView=imageLayout.findViewById(R.id.textview);

       };

       if(position==0){
           imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
           textView.setText("See Who Likes you?");

       }

       else if(position==1){
           imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_boost));
           textView.setText("Unlimited Boost");
       }

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
