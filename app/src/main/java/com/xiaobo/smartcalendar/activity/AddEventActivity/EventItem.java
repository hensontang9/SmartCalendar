package com.xiaobo.smartcalendar.activity.AddEventActivity;

import android.content.Context;

import com.xiaobo.smartcalendar.Public.ProfileManager;
import com.xiaobo.smartcalendar.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventItem {

    Context mContext;
    String itemTitle;
    String itemDetail;
    Date itemDate;
    int checkedItem = 0;
    boolean importance = false;
    long Minimumduration = 0;
    enum TypeItem {
        item_type_1,
        item_type_2,
        item_type_3,
        item_type_4
    }
    TypeItem itemType;

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    public int getCheckedItem() {
        return checkedItem;
    }

    public Boolean getImportance() {
        return importance;
    }

    public void setCheckedItem(int checkedItem) {
        this.checkedItem = checkedItem;
    }

    public Date getItemDate() {
        return itemDate;
    }

    public long getMinimumduration() {
        return Minimumduration;
    }

    public void setMinimumduration(long minimumduration) {
        Minimumduration = minimumduration;
    }

    public EventItem(String itemTitle, String itemDetail, TypeItem itemType) {
        this.itemTitle = itemTitle;
        this.itemDetail = itemDetail;
        this.itemType = itemType;
    }

    public EventItem(String itemTitle, Date itemDate, TypeItem itemType, Context context) {
        this.itemTitle = itemTitle;
        this.itemDate = itemDate;
        this.itemType = itemType;
        this.mContext = context;

        SimpleDateFormat HHmm = new SimpleDateFormat("MM月dd日 HH:mm");

        if (ProfileManager.get(mContext).getLanguage() == 2) {
            HHmm = new SimpleDateFormat("MMM.dd HH:mm", Locale.ENGLISH);
        }
        if (itemDate.after(new Date(0))) {
            this.itemDetail = HHmm.format(itemDate);
        }
        else {

            this.itemDetail = this.mContext.getResources().getString(R.string.UNDEFINED);
        }
    }

    public EventItem(String itemTitle, Boolean importance, TypeItem itemType) {
        this.itemTitle = itemTitle;
        this.importance = importance;
        this.itemType = itemType;
    }

}
