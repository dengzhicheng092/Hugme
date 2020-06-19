package com.qboxus.hugme.All_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.qboxus.hugme.All_Model_Classes.List_GetSet;
import com.qboxus.hugme.Code_Classes.Adapter_ClickListener;
import com.qboxus.hugme.R;

public class List_CustomAdapter extends BaseAdapter {

    private Context context;
    public static ArrayList<List_GetSet> modelArrayList;
    Adapter_ClickListener adapter_clickListener;

    public List_CustomAdapter(Context context, ArrayList<List_GetSet> modelArrayList, Adapter_ClickListener adapter_clickListener) {

        this.context = context;
        this.modelArrayList = modelArrayList;
        this.adapter_clickListener = adapter_clickListener;

    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        List_GetSet list_getSet = modelArrayList.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_listview, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_id);
            holder.tvAnimal = (TextView) convertView.findViewById(R.id.textview_id);
            holder.ll = (LinearLayout) convertView.findViewById(R.id.linearlayout_id);

            convertView.setTag(holder);
        }else {
          holder = (ViewHolder)convertView.getTag();
        }

        holder.tvAnimal.setText(modelArrayList.get(position).getText());

        holder.checkBox.setChecked(modelArrayList.get(position).getSelected());

        holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.setTag( position);
        holder.bind(position,list_getSet,adapter_clickListener);

        return convertView;
    }

    private class ViewHolder {

        public CheckBox checkBox;
        public TextView tvAnimal;
        public LinearLayout ll;

        public void bind(final  int pos ,final  List_GetSet item , final Adapter_ClickListener onClickListner){

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListner.On_Item_Click(pos,item,view);
                }
            });
        }
    }
}
