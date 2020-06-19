package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.qboxus.hugme.All_Model_Classes.Get_Set_Nearby;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;

public class Card_Adapter extends ArrayAdapter<Get_Set_Nearby> {


    Adapter_ClickListener adapter_clickListener;
    List<Get_Set_Nearby> nearby_list;

    public Card_Adapter(Context context, int resources,  Adapter_ClickListener adapter_clickListener, List<Get_Set_Nearby> nearby_list ) {
        super(context, resources);
        this.adapter_clickListener = adapter_clickListener;
        this.nearby_list = nearby_list;
    }

    float initialX, initialY;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {


        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_card_layout, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        final Get_Set_Nearby item = getItem(position);

        holder.image.setImageResource(android.R.color.transparent);
        try{

            Uri uri = Uri.parse(item.getImage1() );
            holder.image.setImageURI(uri);
            holder.image.getHierarchy().setPlaceholderImage(getContext().getResources().getDrawable(R.drawable.profile_image_placeholder));

        }catch (Exception v){
            Functions.Log_d_msg(getContext(),"Error In Fresco " + getItem(position) + " Err " + v.toString());
        }

        holder.onbind(position,item,adapter_clickListener);
        return contentView;
    }

    private static class ViewHolder {

        public SimpleDraweeView image;
        ImageView like,dislike;
        public ViewHolder(View view) {
            image=view.findViewById(R.id.card_item_userimage_id);
            like =view.findViewById(R.id.right_overlay);
            dislike = view.findViewById(R.id.left_overlay);

        }

        public void onbind(final int pos, final Get_Set_Nearby model, final Adapter_ClickListener adapter_clickListener){

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter_clickListener.On_Item_Click(pos,model,view);
                }
            });

            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter_clickListener.On_Item_Click(pos,model,view);
                }
            });

        }
    }


}
