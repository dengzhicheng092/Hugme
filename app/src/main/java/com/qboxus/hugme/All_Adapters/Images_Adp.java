package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.qboxus.hugme.All_Model_Classes.Get_Set_Images;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.R;

public class Images_Adp extends RecyclerView.Adapter<Images_Adp.ViewHolder> {

    Context context;
    List<Get_Set_Images> Wall_paper;
    Adapter_ClickListener adapter_clickListener;


    public Images_Adp(Context context, List<Get_Set_Images> list_wall,Adapter_ClickListener adapter_clickListener) {
        this.context = context;
        this.Wall_paper = list_wall;
        this.adapter_clickListener = adapter_clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_img_in_profile, null);
        return new ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Get_Set_Images check = Wall_paper.get(i);

        viewHolder.iv.setClipToOutline(true);

        if(check.getImg_url().equals("") ||  check.getImg_url().equals("0")){

        }else{
            // Fresco Image
            try{
                Uri uri = Uri.parse(check.getImg_url());
                viewHolder.iv.setImageURI(uri);  // Attachment
            }catch (Exception v){

            }
        }

        viewHolder.onbind(i,check,adapter_clickListener);
    }


    @Override
    public int getItemCount() {
        return Wall_paper.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.IV_id);

        }

        public void onbind(final int pos,final  Get_Set_Images get_set_images, final Adapter_ClickListener listner) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.On_Item_Click(pos,get_set_images,v);

                }
            });

        }

    }

}
