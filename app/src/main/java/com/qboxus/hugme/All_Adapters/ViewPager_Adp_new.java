package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;

public class ViewPager_Adp_new extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    List<String> list_profile_img;
    public ViewPager_Adp_new(Context context, List<String> list_profile_img) {
        this.context = context;
        this.list_profile_img = list_profile_img;
    }


    @Override
    public int getCount() {
        return list_profile_img.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==(RelativeLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_view_prof,container,false);

        ImageView IV = (ImageView) view.findViewById(R.id.view_prof_item_iv_id);

        try{
           Picasso.get().load(list_profile_img.get(position)).fit().centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(IV);

        }catch (Exception b){
            Functions.toast_msg(context,"Err " + b.toString());
        }



        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);

    }

}
