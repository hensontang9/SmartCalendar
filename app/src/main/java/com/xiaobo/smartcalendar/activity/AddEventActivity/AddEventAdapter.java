package com.xiaobo.smartcalendar.activity.AddEventActivity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaobo.smartcalendar.R;

import java.util.LinkedList;

public class AddEventAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<EventItem> mData;

    
    private static final int TYPE_ITEM_1 = 0;
    private static final int TYPE_ITEM_2 = 1;
    private static final int TYPE_ITME_3 = 2;
    private static final int TYPE_ITEM_4 = 3;

    public AddEventAdapter(Context context, LinkedList<EventItem> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
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
    public int getItemViewType(int position) {
        if (mData.get(position).itemType == EventItem.TypeItem.item_type_1) {
            return TYPE_ITEM_1;
        }
        else if (mData.get(position).itemType == EventItem.TypeItem.item_type_2) {
            return TYPE_ITEM_2;
        }
        else if (mData.get(position).itemType == EventItem.TypeItem.item_type_3) {
            return TYPE_ITME_3;
        }
        else if (mData.get(position).itemType == EventItem.TypeItem.item_type_4) {
            return TYPE_ITEM_4;
        }
        else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        ViewHolder4 holder4 = null;

        if (convertView == null) {

            switch (type) {
                case TYPE_ITEM_1:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_event_type1, parent, false);
//                    if (position == 13 || position == 14) {
//                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_event_empty, parent, false);
//                    }
                    holder1 = new ViewHolder1();
                    holder1.itemTitle = convertView.findViewById(R.id.item_title_type1);
                    holder1.itemDetail = convertView.findViewById(R.id.item_detail_textview);
                    convertView.setTag(R.id.Tag_AddEvent_Item_Type1, holder1);
                    break;
                case TYPE_ITEM_2:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_event_type2, parent, false);
                    holder2 = new ViewHolder2(convertView, position);
//                    holder2.itemTitle = view.findViewById(R.id.item_title_type2);
//                    holder2.itemDetail = view.findViewById(R.id.item_detail_edittext);
                    convertView.setTag(R.id.Tag_AddEvent_Item_Type2, holder2);


                    break;
                case TYPE_ITME_3:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_event_type3, parent, false);
                    holder3 = new ViewHolder3(convertView, position);
                    holder3.itemTitle = convertView.findViewById(R.id.item_title_type3);
                    holder3.itemDetail = convertView.findViewById(R.id.item_detail_checkbox);
                    convertView.setTag(R.id.Tag_AddEvent_Item_Type3, holder3);
                    break;
                case TYPE_ITEM_4:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_event_type4, parent, false);
                    holder4 = new ViewHolder4(convertView, position);
                    holder4.itemTitle = convertView.findViewById(R.id.item_title_type4);
                    convertView.setTag(R.id.Tag_ShowEvent_Item_Type4, holder4);
            }
        }
        else {
            holder1 = (ViewHolder1)convertView.getTag(R.id.Tag_AddEvent_Item_Type1);
            holder2 = (ViewHolder2)convertView.getTag(R.id.Tag_AddEvent_Item_Type2);
            holder3 = (ViewHolder3)convertView.getTag(R.id.Tag_AddEvent_Item_Type3);
            holder4 = (ViewHolder4)convertView.getTag(R.id.Tag_ShowEvent_Item_Type4);
        }

        switch (type) {
            case TYPE_ITEM_1:
                holder1.itemTitle.setText(mData.get(position).getItemTitle());
                holder1.itemDetail.setText(mData.get(position).getItemDetail());
                break;
            case TYPE_ITEM_2:
                holder2.itemTitle.setText(mData.get(position).getItemTitle());
                holder2.itemDetail.setHint(mData.get(position).getItemDetail());
                break;
            case TYPE_ITME_3:
                holder3.itemTitle.setText(mData.get(position).getItemTitle());
                break;
            case TYPE_ITEM_4:
                holder4.itemTitle.setText(mData.get(position).getItemTitle());
                break;
        }




        return convertView;
    }

    static class ViewHolder1 {
        TextView itemTitle;
        TextView itemDetail;
    }
    class ViewHolder2 {
        TextView itemTitle;
        EditText itemDetail;

        public ViewHolder2(View view, int position) {
            itemTitle = view.findViewById(R.id.item_title_type2);
            itemDetail = view.findViewById(R.id.item_detail_edittext);
            itemDetail.setTag(position);
            itemDetail.addTextChangedListener(new TextSwitcher(this));
        }
    }
    class ViewHolder3 {
        TextView itemTitle;
        CheckBox itemDetail;

        private ViewHolder3(View view, int position) {
            itemTitle = view.findViewById(R.id.item_title_type3);
            itemDetail = view.findViewById(R.id.item_detail_checkbox);
            itemDetail.setTag(position);
            itemDetail.setOnCheckedChangeListener(new CheckBoxChecked(this));
        }
    }
    class ViewHolder4 {
        TextView itemTitle;

        private ViewHolder4(View view, int position) {
            itemTitle = view.findViewById(R.id.item_title_type4);
        }
    }

    class TextSwitcher implements TextWatcher {

        private ViewHolder2 viewHolder2;

        private TextSwitcher(ViewHolder2 viewHolder2) {
            this.viewHolder2 = viewHolder2;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int position = (int)viewHolder2.itemDetail.getTag();
            ((AddEventActivity)mContext).saveEditData(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    class CheckBoxChecked implements CompoundButton.OnCheckedChangeListener {

        private ViewHolder3 viewHolder3;

        private CheckBoxChecked(ViewHolder3 viewHolder3) {
            this.viewHolder3 = viewHolder3;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            int position = (int)viewHolder3.itemDetail.getTag();
            ((AddEventActivity)mContext).saveCheckBoxData(position, checked);
        }
    }
}
