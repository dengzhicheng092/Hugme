package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qboxus.hugme.All_Model_Classes.Live_user_Model;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Live_user_Adapter extends RecyclerView.Adapter<Live_user_Adapter.CustomViewHolder >{
    public Context context;
    ArrayList<Live_user_Model> dataList = new ArrayList<>();


    Adapter_ClickListener adapterClickListener;


    int width;



    public Live_user_Adapter(Context context, ArrayList<Live_user_Model> user_dataList, Adapter_ClickListener adapterClickListener) {

        this.context = context;
        this.dataList=user_dataList;

        width=(Variables.width/2)-20;
         this.adapterClickListener=adapterClickListener;

    }

    @Override
    public Live_user_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(width, Variables.width-300));
        Live_user_Adapter.CustomViewHolder viewHolder = new Live_user_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView name,age,distance_txt;
        public SimpleDraweeView image;



        public CustomViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.username);
            image=view.findViewById(R.id.image);
            distance_txt=view.findViewById(R.id.distance_txt);

        }

        public void bind(final int pos,final Live_user_Model item,
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
    public void onBindViewHolder(final Live_user_Adapter.CustomViewHolder holder, final int i) {

        final Live_user_Model item=dataList.get(i);

        holder.bind(i,item,adapterClickListener);


        holder.name.setText(item.getUser_name());
        if(item.getUser_picture()!=null && !item.getUser_picture().equals("")) {
            Uri uri = Uri.parse(item.getUser_picture());
            holder.image.setImageURI(uri);
        }


   }



}