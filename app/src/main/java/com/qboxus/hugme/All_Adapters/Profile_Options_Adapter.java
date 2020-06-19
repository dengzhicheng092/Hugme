package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import com.qboxus.hugme.All_Model_Classes.Recyclerview_GetSet;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.R;

public class Profile_Options_Adapter extends RecyclerView.Adapter<Profile_Options_Adapter.ViewHolder> {

    Context context;
    ArrayList<Recyclerview_GetSet> list;
    Adapter_ClickListener adapter_clickListener;


    public Profile_Options_Adapter(Context context, ArrayList<Recyclerview_GetSet> list, Adapter_ClickListener adapter_clickListener ) {
        this.context = context;
        this.list = list;
        this.adapter_clickListener = adapter_clickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_profile_options, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        Recyclerview_GetSet item = list.get(i);
        viewHolder.tv.setText(item.getText());

        viewHolder.bind(i,item,adapter_clickListener);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        LinearLayout ll;
        RadioButton rb_id;
        LinearLayout linearlayout_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.textview_id);
            ll = (LinearLayout) itemView.findViewById(R.id.linearlayout_id);
            rb_id = itemView.findViewById(R.id.rb_id);
            linearlayout_id = itemView.findViewById(R.id.linearlayout_id);


        }

        public void bind(final  int pos ,final Recyclerview_GetSet  item , final Adapter_ClickListener onClickListner){

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListner.On_Item_Click(pos,item,view);
                }
            });
        }
    }

}
