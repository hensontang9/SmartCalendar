package com.xiaobo.smartcalendar.activity.MainActivity;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Public.AnimatedExpandableListView;
import com.xiaobo.smartcalendar.Public.PublicFunction;
import com.xiaobo.smartcalendar.R;

import java.security.acl.Group;
import java.util.ArrayList;

public class MyBaseExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private Context mContext;
    private ArrayList<Calendar> groupData;
    private ArrayList<ArrayList<MyEvent>> itemData;

    public MyBaseExpandableListAdapter(Context context, ArrayList<Calendar> groupData, ArrayList<ArrayList<MyEvent>> itemData) {
        this.mContext = context;
        this.groupData = groupData;
        this.itemData = itemData;
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return itemData.get(groupPosition).size();
//    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return itemData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentView) {

        ViewHolderGroup groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_list_group, parentView, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.textViewGroupName = convertView.findViewById(R.id.main_list_group_name);
            convertView.setTag(groupHolder);
        }
        else {
            groupHolder = (ViewHolderGroup)convertView.getTag();
        }

        String cal = PublicFunction.formatDate(mContext, groupData.get(groupPosition).getMonth(), groupData.get(groupPosition).getDay());
        groupHolder.textViewGroupName.setText(cal);

        return convertView;
    }

    
    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parentView) {

        ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.main_list_item, parentView, false);
            itemHolder = new ViewHolderItem();
            itemHolder.textViewItemTitle = (TextView) convertView.findViewById(R.id.main_list_item_title);
            itemHolder.textViewItemContent = (TextView)convertView.findViewById(R.id.main_list_item_content) ;
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        MyEvent myEvent = itemData.get(groupPosition).get(childPosition);
        itemHolder.textViewItemTitle.setText(myEvent.getmActivityTitle().getmTitle());
        StringBuilder describe = new StringBuilder();
        describe.append(mContext.getResources().getString(R.string.EVENT_TITLE) + ":" + myEvent.getmActivityTitle().getmTitle() + "\n");
        describe.append(mContext.getResources().getString(R.string.EVENT_HOST) + ":" + myEvent.getmHost() + "\n");
        describe.append(mContext.getResources().getString(R.string.EVENT_PARTICIPANT) + ":" + myEvent.getmParticipant().getmParticipant() + "\n");
        describe.append(mContext.getResources().getString(R.string.EVENT_TYPE) + ":" + myEvent.getmTypeOfEvent().getmKind().toString() + "\n");
        describe.append(mContext.getResources().getString(R.string.EVENT_LOCATION) + ":" + myEvent.getmTargetLocation().getTargetName() + "\n");
        describe.append(mContext.getResources().getString(R.string.EVENT_PERIODICITY) + ":" + myEvent.getmPeriodicity().getmTypeOfPeriodicity().getTypeName() + "\n");
        describe.append(mContext.getResources().getString(R.string.EVENT_DATE) + ":" + myEvent.getmDateOfEvent().getDate() + "\n");
        describe.append(mContext.getResources().getString(R.string.DURATION) + ":" + PublicFunction.formatDate(mContext, myEvent.getmDateOfEvent().getDuration()) + "\n");

        itemHolder.textViewItemContent.setText(describe.toString());
        return convertView;
    }

    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolderGroup {
        private TextView textViewGroupName;
    }

    private static class ViewHolderItem {
        private TextView textViewItemTitle;
        private TextView textViewItemContent;
    }
}
