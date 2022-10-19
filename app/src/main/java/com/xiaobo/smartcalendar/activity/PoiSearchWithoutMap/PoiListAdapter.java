package com.xiaobo.smartcalendar.activity.PoiSearchWithoutMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaobo.smartcalendar.R;

import java.util.ArrayList;

public class PoiListAdapter extends BaseAdapter {

    Context context;
    ArrayList<PoiInfoModel> data;

    public PoiListAdapter (Context context, ArrayList<PoiInfoModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_poi_info, parent, false);
        TextView titleTextView = convertView.findViewById(R.id.poi_info_title);
        TextView addressTextView = convertView.findViewById(R.id.poi_info_address);
        titleTextView.setText(data.get(position).getTitle());
        addressTextView.setText(data.get(position).getAddress());
        return convertView;
    }
}
