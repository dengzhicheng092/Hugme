package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qboxus.hugme.ViewHolders.Chatviewholder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;
import com.qboxus.hugme.All_Activities.Chat_A;
import com.qboxus.hugme.All_Model_Classes.Chat_GetSet;
import com.qboxus.hugme.ViewHolders.Alertviewholder;
import com.qboxus.hugme.ViewHolders.ChatVideoviewholder;
import com.qboxus.hugme.ViewHolders.Chatimageviewholder;

public class Msg_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Chat_GetSet> mDataSet;
    String myID;
    private static final int mychat = 1;

    private static final int friendchat = 2;

    private static final int mychatimage = 3;
    private static final int otherchatimage=  4;

    private static final int mygifimage = 5;
    private static final int othergifimage = 6;

    private static final int alert_message = 7;


    private static final int my_video_message = 10;
    private static final int other_video_message = 11;


    Context context;
    Integer today_day=0;

     Adapter_ClickListener adapter_clickListener;

    public interface OnItemClickListener {
        void onItemClick(Chat_GetSet item, View view);
    }

    public interface OnLongClickListener {
        void onLongclick(Chat_GetSet item, View view);
    }


    public Msg_adapter(List<Chat_GetSet> dataSet, String id, Context context,  Adapter_ClickListener adapter_clickListener) {
        mDataSet = dataSet;
        this.myID=id;
        this.context=context;
        this.adapter_clickListener=adapter_clickListener;
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View v = null;

        switch (viewtype){

            case mychat:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_my, viewGroup, false);
                Chatviewholder mychatHolder = new Chatviewholder(v);
                return mychatHolder;
            case friendchat:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_other, viewGroup, false);
                Chatviewholder friendchatHolder = new Chatviewholder(v);
                return friendchatHolder;
            case mychatimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_image_my, viewGroup, false);
                Chatimageviewholder mychatimageHolder = new Chatimageviewholder(v);
                return mychatimageHolder;
            case otherchatimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_image_other, viewGroup, false);
                Chatimageviewholder otherchatimageHolder = new Chatimageviewholder(v);
                return otherchatimageHolder;



            case mygifimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_gif_my, viewGroup, false);
                Chatimageviewholder mychatgigHolder = new Chatimageviewholder(v);
                return mychatgigHolder;

            case othergifimage:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_gif_other, viewGroup, false);
                Chatimageviewholder otherchatgifHolder = new Chatimageviewholder(v);
                return otherchatgifHolder;

            case my_video_message:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_video_my, viewGroup, false);
                ChatVideoviewholder mychatVideoviewholder = new ChatVideoviewholder(v);
                return mychatVideoviewholder;

            case other_video_message:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_video_other, viewGroup, false);
                ChatVideoviewholder otherchatVideoviewholder = new ChatVideoviewholder(v);
                return otherchatVideoviewholder;


            case alert_message:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_alert, viewGroup, false);
                Alertviewholder alertviewholder = new Alertviewholder(v);
                return alertviewholder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat_GetSet chat = mDataSet.get(position);
        holder.setIsRecyclable(false);

        Log.d("resp","" + position);

        if(chat.getType().equals("text")){
            Chatviewholder chatviewholder=(Chatviewholder) holder;
            // check if the message is from sender or receiver
            // changeDrawableColor
            if(chat.getSender_id().equals(myID)){
                Functions.getRoundRect();
               // chatviewholder.message.setBackground(Functions.getRoundRect());
                if(chat.getStatus().equals("1"))
                    chatviewholder.message_seen.setText(" Seen at "+ChangeDate_to_time(chat.getTime()));

                else
                    chatviewholder.message_seen.setText("Sent");
            }else {

                chatviewholder.message_seen.setText("");
            }

            chatviewholder.message.setText(chat.getText());
            chatviewholder.msg_date.setText(Show_Message_Time(chat.getTimestamp()));


            if (position != 0) {
                Chat_GetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(0, 2).equals(chat.getTimestamp().substring(0, 2))) {

                } else {
                    chatviewholder.datetxt.setVisibility(View.VISIBLE);
                    chatviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }

            }else {
                chatviewholder.datetxt.setVisibility(View.VISIBLE);
                chatviewholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
            }

            chatviewholder.bind(position,chat,adapter_clickListener);

        }

        else if(chat.getType().equals("image")){

            final Chatimageviewholder chatimageholder=(Chatimageviewholder) holder;

            chatimageholder.message_seen.setTextColor(context.getResources().getColor(R.color.gray));
            chatimageholder.datetxt.setVisibility(View.VISIBLE);

            if(chat.getSender_id().equals(myID)){
                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText("" + Show_Message_Time(chat.getTimestamp()));

                if(chat.getStatus().equals("1")){
                    chatimageholder.message_seen.setText("Seen at " + ChangeDate_to_time(chat.getTime()));
                }
                else{
                    chatimageholder.message_seen.setText("Sent");
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);

                }


            }else {

                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText("" + Show_Message_Time(chat.getTimestamp()));

                chatimageholder.message_seen.setText("" + ChangeDate_to_time(chat.getTime()));
            }

            if(chat.getPic_url().equals("none")){
                if(Chat_A.uploading_image_id.equals(chat.getChat_id())){
                    chatimageholder.p_bar.setVisibility(View.VISIBLE);
                    chatimageholder.message_seen.setText("");
                }else {
                    chatimageholder.p_bar.setVisibility(View.GONE);
                    chatimageholder.not_send_message_icon.setVisibility(View.VISIBLE);
                    chatimageholder.message_seen.setText("Not delivered. This is bug");
                }
            }else {
                chatimageholder.not_send_message_icon.setVisibility(View.GONE);
                chatimageholder.p_bar.setVisibility(View.GONE);
            }

            if (position != 0) {
                Chat_GetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(0, 2).equals(chat.getTimestamp().substring(0, 2))) {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                   // Functions.toast_msg(context,"Gone or Visible 4");
                } else {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                    chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }
                try{
                    Picasso.get().load(chat.getPic_url()).placeholder(R.drawable.image_placeholder).resize(400,400).centerCrop().into(chatimageholder.chatimage);
                }catch (Exception n){

                }

            }else {

                chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()) + " Bug ");
                try{
                    Picasso.get().load(chat.getPic_url()).placeholder(R.drawable.image_placeholder).resize(400,400).centerCrop().into(chatimageholder.chatimage);
                }catch (Exception b){
                     }
    }
            chatimageholder.bind(position,mDataSet.get(position),adapter_clickListener);
        }


        else if(chat.getType().equals("video")){

            final ChatVideoviewholder viewholder=(ChatVideoviewholder) holder;
            viewholder.message_seen.setTextColor(context.getResources().getColor(R.color.gray));
            viewholder.datetxt.setVisibility(View.VISIBLE);
            if(chat.getSender_id().equals(myID)){
                if(chat.getStatus().equals("1")) {
                    viewholder.message_seen.setText("Seen at " + ChangeDate_to_time(chat.getTime()));
                }
                else{
                    viewholder.message_seen.setText("Sent");
                    viewholder.datetxt.setVisibility(View.VISIBLE);
                    viewholder.datetxt.setText(Show_Message_Time(chat.getTimestamp()));
                }
            }else {
                viewholder.message_seen.setText("");
            }


            //viewholder.message.setText(chat.getText());
            viewholder.datetxt.setText(Show_Message_Time(chat.getTimestamp()));

            if (position != 0) {

                Chat_GetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(14, 16).equals(chat.getTimestamp().substring(14, 16))) {

                } else {
                    viewholder.datetxt.setVisibility(View.VISIBLE);

                }

            }else {
                viewholder.datetxt.setVisibility(View.VISIBLE);
            }

            viewholder.bind(position,mDataSet.get(position),adapter_clickListener);
        }


        else if(chat.getType().equals("gif")){
            final Chatimageviewholder chatimageholder=(Chatimageviewholder) holder;

            chatimageholder.message_seen.setTextColor(context.getResources().getColor(R.color.gray));
            chatimageholder.datetxt.setVisibility(View.VISIBLE);
            chatimageholder.datetxt.setText(Show_Message_Time(chat.getTimestamp()));
            // check if the message is from sender or receiver
            if(chat.getSender_id().equals(myID)){
                if(chat.getStatus().equals("1")){
                    chatimageholder.message_seen.setText("Seen at "+ChangeDate_to_time(chat.getTime()));
                }
                else{
                    chatimageholder.message_seen.setText("Sent");
                    chatimageholder.datetxt.setText(Show_Message_Time(chat.getTimestamp()));
                }

            }else {
                chatimageholder.message_seen.setText("" + ChangeDate_to_time(chat.getTime()));
            }

            if (position != 0) {
                Chat_GetSet chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(0, 2).equals(chat.getTimestamp().substring(0, 2))) {

                } else {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                    chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                }
                Glide.with(context)
                        .load(Variables.gif_firstpart_chat+chat.getPic_url()+Variables.gif_secondpart_chat)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(chatimageholder.chatimage);
            }else {
                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText(ChangeDate(chat.getTimestamp()));
                Glide.with(context)
                        .load(Variables.gif_firstpart_chat+chat.getPic_url()+Variables.gif_secondpart_chat)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(chatimageholder.chatimage);

            }

            chatimageholder.bind(position,mDataSet.get(position),adapter_clickListener);
        }


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    @Override
    public int getItemViewType(int position) {

        if(mDataSet.get(position).getType().equals("text")){
            if (mDataSet.get(position).getSender_id().equals(myID)) {
                return mychat;
            }
            return friendchat;
        }

        else if(mDataSet.get(position).getType().equals("image") ) {

            if (mDataSet.get(position).getSender_id().equals(myID)) {
                return mychatimage;
            }
            return otherchatimage;
        }

        else if(mDataSet.get(position).getType().equals("video") ) {
            if (mDataSet.get(position).getSender_id().equals(myID)) {
                return my_video_message;
            }

            return other_video_message;
        }



        else if(mDataSet.get(position).getType().equals("gif")) {
            if (mDataSet.get(position).getSender_id().equals(myID)) {
                return mygifimage;
            }

            return othergifimage;
        }
        else {
            return alert_message;
        }
    }

    public String ChangeDate(String date){
        //current date in millisecond
        long currenttime= System.currentTimeMillis();

        //database date in millisecond
        long databasedate = 0;
        Date d = null;
        try {
            d = Variables.df.parse(date);
            databasedate = d.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference=currenttime-databasedate;
        if(difference<86400000){
            int chatday= Integer.parseInt(date.substring(0,2));
            if(today_day==chatday)
                return "Today";
            else if((today_day-chatday)==1)
                return "Yesterday";
        }
        else if(difference<172800000){
            int chatday= Integer.parseInt(date.substring(0,2));
            if((today_day-chatday)==1)
                return "Yesterday";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");

        if(d!=null)
            return sdf.format(d);
        else
            return "";
    }


    public String Show_Message_Time(String date){

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        Date d = null;
        try {
            d = Variables.df.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(d!=null)
            return sdf.format(d);
        else
            return "null";
    }


    public String ChangeDate_to_time(String date){


        Date d = null;
        try {
            d = Variables.df2.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        if(d!=null)
            return sdf.format(d);
        else
            return "";
    }


    public String getfileduration(Uri uri) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            long second = (file_duration / 1000) % 60;
            long minute = (file_duration / (1000 * 60)) % 60;

            return String.format("%02d:%02d", minute, second);
        }
        catch (Exception e){

        }
        return null;
    }

}
