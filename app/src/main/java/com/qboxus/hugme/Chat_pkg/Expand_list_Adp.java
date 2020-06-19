package com.qboxus.hugme.Chat_pkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.qboxus.hugme.R;
import com.qboxus.hugme.Shared_pref.SharedPrefrence;

import static com.qboxus.hugme.Chat_pkg.Inbox_F.ex;

public class Expand_list_Adp extends BaseExpandableListAdapter {

    Context mcontext;
    private ArrayList<Object> childtems;
    private ArrayList<String> parentItems, child;
    private LayoutInflater inflater;
    public static TextView listTitleTextView;
    private CallBack_click mCallBack;
    public interface CallBack_click{
        void Sort_by_date_new(String child);
    }


    public Expand_list_Adp(Context ctx,ArrayList<String> parents, ArrayList<Object> childern, CallBack_click callback) {
        this.mcontext = ctx;
        this.parentItems = parents;
        this.childtems = childern;
        this.mCallBack = callback;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<String>) childtems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            convertView = layoutInflater.inflate(R.layout.expand_group_item, null);
        }
        listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgTitle);
        String filter = SharedPrefrence.get_string(mcontext,"" + SharedPrefrence.share_filter_inbox_key);

        if(filter != null){
            listTitleTextView.setText(filter);
        }else{

            listTitleTextView.setText("All Connections");
        }


        if (isExpanded){
            imageView.setImageResource(R.drawable.ic_up_arrow);
        }else {
            imageView.setImageResource(R.drawable.ic_down_arrow);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        child = (ArrayList<String>) childtems.get(groupPosition);

//        final TextView expandedListTextView = null;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            convertView = layoutInflater.inflate(R.layout.expand_list_item, null);
        }
        final TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expand_list_item_id);


        expandedListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listTitleTextView.setText(child.get(childPosition));
                ex.collapseGroup(0);
                mCallBack.Sort_by_date_new(child.get(childPosition));

            }
        });


        expandedListTextView.setText(child.get(childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}


