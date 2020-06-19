package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;

public class Gif_Adapter extends RecyclerView.Adapter<Gif_Adapter.CustomViewHolder > {

    public Context context;
    ArrayList<String> gif_list = new ArrayList<>();

    Adapter_ClickListener adapter_clickListener;

    public Gif_Adapter(Context context, ArrayList<String> urllist, Adapter_ClickListener adapter_clickListener) {
        this.context = context;
        this.gif_list=urllist;
        this.adapter_clickListener = adapter_clickListener;

    }

    @Override
    public Gif_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gif_layout,null);
        Gif_Adapter.CustomViewHolder viewHolder = new Gif_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return gif_list.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView gif_image;

        public CustomViewHolder(View view) {
            super(view);
            gif_image=view.findViewById(R.id.gif_image);
        }

        public void bind(final int pos, final String item, final Adapter_ClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.On_Item_Click(pos,item,v);
                }
            });


        }

    }


    @Override
    public void onBindViewHolder(final Gif_Adapter.CustomViewHolder holder, final int i) {
        holder.bind(i,gif_list.get(i),adapter_clickListener);
        Glide.with(context)
                .load(Variables.gif_firstpart+gif_list.get(i)+ Variables.gif_secondpart)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(context.getResources().getDrawable(R.drawable.image_placeholder)).centerCrop())
                .into(holder.gif_image);
    }

}
