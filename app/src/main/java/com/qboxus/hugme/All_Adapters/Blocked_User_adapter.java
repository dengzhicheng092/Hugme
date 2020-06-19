package com.qboxus.hugme.All_Adapters;

import android.content.Context;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.qboxus.hugme.All_Model_Classes.Inbox_Get_Set_List;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Chat_pkg.Inbox_F;
import com.qboxus.hugme.R;

public class Blocked_User_adapter extends RecyclerView.Adapter<Blocked_User_adapter.CustomViewHolder > implements Filterable {

    public Context context;
    ArrayList<Inbox_Get_Set_List> inbox_dataList = new ArrayList<>();
    ArrayList<Inbox_Get_Set_List> inbox_dataList_filter = new ArrayList<>();
    Adapter_ClickListener adapter_clickListener;

    Integer today_day=0;

    public interface OnItemClickListener {
        void onItemClick(Inbox_Get_Set_List item);
    }

    public interface OnLongItemClickListener{
        void onLongItemClick(Inbox_Get_Set_List item);
    }

    public Blocked_User_adapter(Context context, ArrayList<Inbox_Get_Set_List> user_dataList, Adapter_ClickListener adapter_clickListener) {
        this.context = context;

        this.inbox_dataList=user_dataList;
        this.inbox_dataList_filter=user_dataList;
        this.adapter_clickListener=adapter_clickListener;
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public Blocked_User_adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inbox_block_user,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Blocked_User_adapter.CustomViewHolder viewHolder = new Blocked_User_adapter.CustomViewHolder(view);
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

        public void bind(final  int pos,final Inbox_Get_Set_List item, final Adapter_ClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.On_Item_Click(pos,item,v);
                }
            });

            mainlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.On_Long_Item_Click(pos,item,v);
                    return false;
                }
            });
        }

    }


    @Override
    public void onBindViewHolder(final Blocked_User_adapter.CustomViewHolder holder, final int i) {
        final Inbox_Get_Set_List item=inbox_dataList_filter.get(i);

        holder.bind(i,item,adapter_clickListener);

        String check = Functions.parseDateToddMMyyyy(item.getDate(),context);

        holder.date_created.setText(Functions.get_time_ago_org(check));
        String msg = item.getMsg();


        holder.last_message.setText(msg);

        String name = item.getName();

        if(name.length() > 10){

          name = name.substring(0,10) + " ...";

        }else{
            name = name;
        }

        holder.user_name.setText(name);


        item.getLike();

        if(item.getLike().equals("0")){
            // If unlike
            holder.ic_star.setTag("unlike");
            holder.ic_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_unfilled));


        }else{
            // If Like
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
                // Functions.toast_msg(context,"Like " + item.getLike());

                if(holder.ic_star.getTag().equals("unlike")){
                    // If tag is unlike and user want to like....
                    holder.ic_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_filled));
                    //   Functions.toast_msg(context,"Click on unLike " + holder.ic_star.getTag() + " ID " +  item.getRid());
                    holder.ic_star.setTag("like");
                    Inbox_F.Like_chat("" + item.getRid(), context, "1");
                }else{
                    // if tag is like and user wants to like the chat. ;-D
                    holder.ic_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_unfilled));
                    // Functions.toast_msg(context,"Click on Like " + holder.ic_star.getTag());
                    holder.ic_star.setTag("unlike");
                    Inbox_F.Like_chat("" + item.getRid(), context, "0");


                } // End else


            }
        });

    }

    public String ChangeDate(String date){
        try {
            //current date in millisecond
            long currenttime = System.currentTimeMillis();

            //database date in millisecond
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            long databasedate = 0;
            Date d = null;
            try {
                d = f.parse(date);
                databasedate = d.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = currenttime - databasedate;
            if (difference < 86400000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if (today_day == chatday)
                    return sdf.format(d);
                else if ((today_day - chatday) == 1)
                    return "Yesterday";
            } else if (difference < 172800000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if ((today_day - chatday) == 1)
                    return "Yesterday";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");
            return sdf.format(d);
        }catch (Exception e){
            Functions.toast_msg(context,"" + e.toString());
        }finally {
            return "";
        }
    }


    // that function will filter the result
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
