package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.qboxus.hugme.R;

public class ViewPager_Adapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    List<String> list_profile_img;
    public ViewPager_Adapter(Context context) {
        this.context = context;

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

        SimpleDraweeView IV =  view.findViewById(R.id.view_prof_item_iv_id);

        try{

            Uri uri = Uri.parse(list_profile_img.get(position) );
            IV.setImageURI(uri);
            IV.getHierarchy().setPlaceholderImage(container.getResources().getDrawable(R.drawable.image_placeholder));

        }catch (Exception v){
        }


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);

    }

}
