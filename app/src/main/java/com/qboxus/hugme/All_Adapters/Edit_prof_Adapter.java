package com.qboxus.hugme.All_Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;

public class Edit_prof_Adapter extends RecyclerView.Adapter<Edit_prof_Adapter.ViewHolder> {

     Context context;
    Activity activity;

    List<String> list_user_image;

    public final int image_type=1;
    public final int browse_type=2;

    Adapter_ClickListener adapter_clickListener;
    public Edit_prof_Adapter(Context context, Activity activity, List<String> list_user_image,   Adapter_ClickListener adapter_clickListener ){
        this.context = context;
        this.activity = activity;
        this.adapter_clickListener = adapter_clickListener;
        this.list_user_image = list_user_image;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        View v ;
        if(viewtype==image_type){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_edit_prof, null);
            return new ViewHolder(v);
        }
        else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_browse_profile_image, null);
            return new ViewHolder(v);
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {

        final String images = list_user_image.get(i);
        viewHolder.bind(i,images,adapter_clickListener);

        if(viewHolder.cancel!=null) {

            viewHolder.iv.setClipToOutline(true);
            if(i==0){
                viewHolder.cancel.setVisibility(View.GONE);
            }else {
                viewHolder.cancel.setVisibility(View.VISIBLE);
            }

            try {
                Uri uri = Uri.parse(images);
                viewHolder.iv.setImageURI(uri);
            } catch (Exception v) {

            }

        }
        else {


        }






    }

    @Override
    public int getItemViewType(int position) {
        if(list_user_image.get(position).equals("0") || list_user_image.get(position).equals("")){
           return browse_type;
        }else{
            return image_type;
        }

    }

    @Override
    public int getItemCount() {
        return list_user_image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView iv;
        ImageView cancel;
        RelativeLayout RL_add_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv =  itemView.findViewById(R.id.edit_prof_IV_id);
            cancel = itemView.findViewById(R.id.cancel);


            RL_add_img = itemView.findViewById(R.id.RL_add_img);

        }

        public void bind(final  int pos ,final String  item , final Adapter_ClickListener onClickListner){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListner.On_Item_Click(pos,item,view);
                }
            });


            if(RL_add_img!=null)
            RL_add_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListner.On_Item_Click(pos,item,v);
                }
            });

            if(cancel!=null)
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListner.On_Item_Click(pos,item,v);
                    }
                });
        }

    }


}
