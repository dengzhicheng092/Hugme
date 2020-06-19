package com.qboxus.hugme.Chat_pkg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.qboxus.hugme.All_Activities.Chat_A;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.All_Model_Classes.Match_Model;
import com.qboxus.hugme.R;

public class Match_Adapter extends RecyclerView.Adapter<Match_Adapter.ViewHolder> {

    Context context;
    List<Match_Model> List_MyMatch;



    public Match_Adapter(Context context, List<Match_Model> list_MyMatch) {
        this.context = context;
        List_MyMatch = list_MyMatch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_match_layout, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final Match_Model my_match = List_MyMatch.get(i);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.IV.setClipToOutline(true);
        }

        try{
            Uri uri = Uri.parse(my_match.getPicture());
            viewHolder.IV.setImageURI(uri);


        }catch (Exception v){
            Functions.toast_msg(context,"Error Adp image " + v.toString());
        }


        viewHolder.IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent myIntent = new Intent(context, Chat_A.class);
                myIntent.putExtra("receiver_id", my_match.getU_id());
                myIntent.putExtra("receiver_name", my_match.getUsername());
                myIntent.putExtra("receiver_pic", my_match.getPicture());
                myIntent.putExtra("match_api_run", "1");
                myIntent.putExtra("position_to_remove", "" + i);
                myIntent.putExtra("is_block", "0");



                context.startActivity(myIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return List_MyMatch.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView add_photo;
        SimpleDraweeView IV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            IV = itemView.findViewById(R.id.chat_item_IV_id);
            add_photo = (ImageView) itemView.findViewById(R.id.add_photo_img_id);

        }
    }
}
