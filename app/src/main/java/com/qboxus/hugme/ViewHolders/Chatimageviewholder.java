package com.qboxus.hugme.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qboxus.hugme.All_Model_Classes.Chat_GetSet;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.R;

public class Chatimageviewholder extends RecyclerView.ViewHolder {

    public ImageView chatimage;
    public TextView datetxt,message_seen;
    public ProgressBar p_bar;
    public ImageView not_send_message_icon;
    public View view;


    public ImageView user_image;
    public TextView user_name,time_txt;
    public LinearLayout upperlayout;

    public Chatimageviewholder(View itemView) {
        super(itemView);
        view = itemView;

        this.chatimage = view.findViewById(R.id.chatimage);
        this.datetxt=view.findViewById(R.id.datetxt);
        message_seen=view.findViewById(R.id.message_seen);
        not_send_message_icon=view.findViewById(R.id.not_send_messsage);
        p_bar=view.findViewById(R.id.p_bar);


       // user_name=view.findViewById(R.id.user_name);
        this.upperlayout=view.findViewById(R.id.upperlayout);


    }

    public void bind(final int pos,final Chat_GetSet item, final Adapter_ClickListener adapter_clickListener) {

        chatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_clickListener.On_Item_Click(pos,item,v);
            }
        });

        chatimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
              adapter_clickListener.On_Long_Item_Click(pos,item,v);
                return false;
            }
        });
    }
}
