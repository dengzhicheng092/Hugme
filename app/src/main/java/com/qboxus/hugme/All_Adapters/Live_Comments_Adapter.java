package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.qboxus.hugme.All_Model_Classes.Live_Comment_Model;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.R;


import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Live_Comments_Adapter extends RecyclerView.Adapter<Live_Comments_Adapter.CustomViewHolder > {

    public Context context;
    private Adapter_ClickListener listener;
    private ArrayList<Object> dataList;



      public interface OnItemClickListener {
        void onItemClick(int positon, Object item, View view);
    }

    public Live_Comments_Adapter(Context context, ArrayList<Object> dataList, Adapter_ClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public Live_Comments_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_comment_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Live_Comments_Adapter.CustomViewHolder viewHolder = new Live_Comments_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }


    @Override
    public void onBindViewHolder(final Live_Comments_Adapter.CustomViewHolder holder, final int i) {
        final Live_Comment_Model item= (Live_Comment_Model) dataList.get(i);


        holder.username.setText(item.user_name);


        holder.message.setText(item.comment);
        if(item.user_picture!=null && !item.user_picture.equals("")) {
            Uri uri = Uri.parse(item.user_picture);
            holder.user_pic.setImageURI(uri);
        }

        holder.bind(i,item,listener);

   }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username,message;
        SimpleDraweeView user_pic;


        public CustomViewHolder(View view) {
            super(view);

            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            message=view.findViewById(R.id.message);

        }

        public void bind(final int postion,final Live_Comment_Model item, final Adapter_ClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.On_Item_Click(postion,item,v);
                }
            });

        }


    }





}