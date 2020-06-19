package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class User_Like_Adapter extends RecyclerView.Adapter<User_Like_Adapter.CustomViewHolder >{
    public Context context;
    ArrayList<Get_Set_Nearby> dataList = new ArrayList<>();


    Adapter_ClickListener adapterClickListener;

    int width;


    public User_Like_Adapter(Context context, ArrayList<Get_Set_Nearby> user_dataList, Adapter_ClickListener adapterClickListener) {


        this.context = context;
        this.dataList=user_dataList;

        width=(Variables.width/2)-20;

        this.adapterClickListener=adapterClickListener;

    }

    @Override
    public User_Like_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(width, Variables.width-300));
        User_Like_Adapter.CustomViewHolder viewHolder = new User_Like_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView name,age,distance_txt;
        public SimpleDraweeView image;
        public FrameLayout left_overlay,right_overlay;



        public CustomViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.username);
            image=view.findViewById(R.id.image);
            distance_txt=view.findViewById(R.id.distance_txt);
            left_overlay=view.findViewById(R.id.left_overlay);
            right_overlay=view.findViewById(R.id.right_overlay);
        }

        public void bind(final int pos,final Get_Set_Nearby item,
                         final Adapter_ClickListener adapterClickListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterClickListener.On_Item_Click(pos,item,v);
                }
            });

        }


    }


    @Override
    public void onBindViewHolder(final User_Like_Adapter.CustomViewHolder holder, final int i) {

        final Get_Set_Nearby item=dataList.get(i);

        holder.bind(i,item,adapterClickListener);


        holder.name.setText(item.getFirst_name());
        holder.distance_txt.setText(item.getDistance());

        if(item.getImage1()!=null && !item.getImage1().equals("")) {
            Uri uri = Uri.parse(item.getImage1());
            holder.image.setImageURI(uri);
        }

        holder.right_overlay.setVisibility(View.GONE);
        holder.left_overlay.setVisibility(View.GONE);


   }



}