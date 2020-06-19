package com.qboxus.hugme.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qboxus.hugme.All_Model_Classes.Chat_GetSet;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.R;

public class Chatviewholder extends RecyclerView.ViewHolder {

    public TextView message,datetxt,message_seen,msg_date;
    public View view;

    public ImageView user_image;
    public TextView user_name,time_txt;
    public LinearLayout upperlayout;

    public Chatviewholder(View itemView) {
        super(itemView);
        view = itemView;

        this.message = view.findViewById(R.id.msgtxt);
        this.datetxt=view.findViewById(R.id.datetxt);
        message_seen=view.findViewById(R.id.message_seen);
        msg_date=view.findViewById(R.id.msg_date);

        this.upperlayout=view.findViewById(R.id.upperlayout);
    }

    public void bind(final  int pos,final Chat_GetSet item,
                     final Adapter_ClickListener adapter_clickListener) {
        message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                adapter_clickListener.On_Long_Item_Click(pos,item,v);
                return false;
            }
        });
    }


}
