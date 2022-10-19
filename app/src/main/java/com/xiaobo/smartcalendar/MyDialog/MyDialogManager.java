package com.xiaobo.smartcalendar.MyDialog;

import android.content.Context;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;

import java.util.List;

public class MyDialogManager {

    private Context context;
    private MyDialog myDialog;
    private MyCustomizedRuleDialog myCustomizedRuleDialog;
    private OnManagerSelectorListener onManagerSelectorListener;
    private List<MyTemporalInconsistency> data;
    private List<String> dataCon;
    private List<String> dataConSub;

    private MyDialogManager(Context context) {
        this.context = context;
    }

    public static MyDialogManager get(Context context) {
        return new MyDialogManager(context);
    }

    public MyDialogManager setSelectorListener(OnManagerSelectorListener onManagerSelectorListener) {
        this.onManagerSelectorListener = onManagerSelectorListener;
        return this;
    }

    
    public MyDialogManager setData(List<MyTemporalInconsistency> data) {

        this.data = data;

        return this;
    }
    public MyDialogManager setDataCon(List<String> data, List<String> dataSub) {
        this.dataCon = data;
        this.dataConSub = dataSub;
        return this;
    }

    
    public void showRecyclerDialog() {
        myDialog = new MyDialog(context, data);
        myDialog.show();
    }
    public void showCuntomRuleRecyclerDialog() {
        myCustomizedRuleDialog = new MyCustomizedRuleDialog(context, dataCon, dataConSub);
        myCustomizedRuleDialog.show();
    }

    
    public void dissRecyclerDialog() {
        if (myDialog != null) {
            myDialog.dismiss();
        }
        if (myCustomizedRuleDialog != null) {
            myCustomizedRuleDialog.dismiss();
        }
    }

    
    public interface OnManagerSelectorListener {
        void getSelectorPosition(int position);
    }
}
