package com.qboxus.hugme.Chat_pkg;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import com.qboxus.hugme.All_Model_Classes.Inbox_Get_Set_List;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.R;

public class Inbox_Adapter extends RecyclerView.Adapter<Inbox_Adapter.CustomViewHolder > implements Filterable {

    public Context context;
    ArrayList<Inbox_Get_Set_List> inbox_dataList = new ArrayList<>();
    ArrayList<Inbox_Get_Set_List> inbox_dataList_filter = new ArrayList<>();
    Integer today_day=0;

    Adapter_ClickListener adapter_clickListener;

    public interface OnLongItemClickListener{
        void onLongItemClick(Inbox_Get_Set_List item);
    }

    public Inbox_Adapter(Context context, ArrayList<Inbox_Get_Set_List> user_dataList,  Adapter_ClickListener adapter_clickListener) {
        this.context = context;
        this.inbox_dataList=user_dataList;
        this.inbox_dataList_filter=user_dataList;
        this.adapter_clickListener = adapter_clickListener;

        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public Inbox_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inbox_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Inbox_Adapter.CustomViewHolder viewHolder = new Inbox_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return inbox_dataList_filter.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView user_name,last_message,date_created;
        SimpleDraweeView user_image;
        RelativeLayout mainlayout;
        ImageView ic_star;

        public CustomViewHolder(View view) {
            super(view);
            this.mainlayout=view.findViewById(R.id.mainlayout);
            this.user_name=view.findViewById(R.id.username);
            this.last_message=view.findViewById(R.id.message);
            this.date_created=view.findViewById(R.id.datetxt);
            this.user_image=view.findViewById(R.id.userimage);
            this.ic_star = view.findViewById(R.id.ic_star);


        }

        public void bind(final int pos,final Inbox_Get_Set_List item, final Adapter_ClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.On_Item_Click(pos,item,v);
                }
            });

            mainlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    adapter_clickListener.On_Long_Item_Click(pos,item,v);
                    return false;
                }
            });
        }

    }


    @Override
    public void onBindViewHolder(final Inbox_Adapter.CustomViewHolder holder, final int i) {
        final Inbox_Get_Set_List item=inbox_dataList_filter.get(i);

        holder.bind(i,item,adapter_clickListener);

        String check = Functions.parseDateToddMMyyyy(item.getDate(),context);

        holder.date_created.setText(Functions.get_time_ago_org(check));

        String msg = item.getMsg();

        holder.last_message.setText(msg);
        if(item.getStatus()!=null & item.getStatus().equalsIgnoreCase("0")){
            holder.last_message.setTypeface(null, Typeface.BOLD);
            holder.last_message.setTextColor(context.getResources().getColor(R.color.black));
        }else {
            holder.last_message.setTypeface(null, Typeface.NORMAL);
            holder.last_message.setTextColor(context.getResources().getColor(R.color.gray));
        }

        String name = item.getName();

        if(name.length() > 10){
            name = name.substring(0,10) + " ...";
        }

        holder.user_name.setText(name);


        item.getLike();

        if(item.getLike().equals("0")){
            holder.ic_star.setTag("unlike");
            holder.ic_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_unfilled));
        }else{

            holder.ic_star.setTag("like");
            holder.ic_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_filled));


        }


        try{
            ImageRequest request =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(item.getPic()))
                            .setProgressiveRenderingEnabled(false)
                            .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(false)
                    .build();

            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);

            holder.user_image.getHierarchy().setPlaceholderImage(R.drawable.image_placeholder);
            holder.user_image.getHierarchy().setFailureImage(R.drawable.image_placeholder);
            holder.user_image.getHierarchy().setRoundingParams(roundingParams);
            holder.user_image.setController(controller);
        }catch (Exception v){

        }

        holder.ic_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.ic_star.getTag().equals("unlike")){

                    holder.ic_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_filled));
                    holder.ic_star.setTag("like");
                    Inbox_F.Like_chat("" + item.getRid(), context, "1");


                }else{
                   holder.ic_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_unfilled));
                   holder.ic_star.setTag("unlike");
                    Inbox_F.Like_chat("" + item.getRid(), context, "0");
                }
            }
        });
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inbox_dataList_filter = inbox_dataList;
                } else {
                    ArrayList<Inbox_Get_Set_List> filteredList = new ArrayList<>();
                    for (Inbox_Get_Set_List row : inbox_dataList) {

                       if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    inbox_dataList_filter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = inbox_dataList_filter;
                return filterResults;

            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inbox_dataList_filter = (ArrayList<Inbox_Get_Set_List>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
