package com.xiaobo.smartcalendar.activity.TestActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.activity.MainActivity.MyBaseListViewAdapter;

import java.util.List;

public class TableListViewAdapter extends BaseAdapter {

    Context context;

    public enum TableType{
        myEvent,
        myTemporalInconsistency
    }

    List data;

    public TableListViewAdapter (Context context, TableType tableType) {
        this.context = context;
        if (tableType == TableType.myEvent) {
            data = MyEventManager.getInstance().getAllEvents();
        }
        else {
            data = MyTemporalInconsistencyManager.getInstance().getAllMyIncons();
        }
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
        TableListViewAdapter.ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.main_list_item, parentView, false);
            itemHolder = new TableListViewAdapter.ViewHolderItem();
            itemHolder.textViewItemTitle = (TextView) convertView.findViewById(R.id.main_list_item_title);
            itemHolder.textViewItemContent = (TextView)convertView.findViewById(R.id.main_list_item_content) ;
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (TableListViewAdapter.ViewHolderItem) convertView.getTag();
        }
        if(data.get(position).getClass() == MyEvent.class) {
            MyEvent item = (MyEvent) data.get(position);
            itemHolder.textViewItemContent.setText(item.describeMyEvent());
        }
        else {
            MyTemporalInconsistency item = (MyTemporalInconsistency) data.get(position);
            itemHolder.textViewItemContent.setText(item.getmID());
        }

        return convertView;
    }

    private static class ViewHolderItem {
        private TextView textViewItemTitle;
        private TextView textViewItemContent;
    }
}
