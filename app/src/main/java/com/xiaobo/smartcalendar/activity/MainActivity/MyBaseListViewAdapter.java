package com.xiaobo.smartcalendar.activity.MainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.R;

import java.util.ArrayList;
import java.util.List;

public class MyBaseListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyEvent> data;

    public MyBaseListViewAdapter (Context mContext, List<MyEvent> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.main_list_item, parentView, false);
            itemHolder = new ViewHolderItem();
            itemHolder.textViewItemTitle = (TextView) convertView.findViewById(R.id.main_list_item_title);
            itemHolder.textViewItemContent = (TextView)convertView.findViewById(R.id.main_list_item_content) ;
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (MyBaseListViewAdapter.ViewHolderItem) convertView.getTag();
        }
        itemHolder.textViewItemContent.setText(data.get(position).describeMyEvent());
        return convertView;
    }

    private static class ViewHolderItem {
        private TextView textViewItemTitle;
        private TextView textViewItemContent;
    }
}
