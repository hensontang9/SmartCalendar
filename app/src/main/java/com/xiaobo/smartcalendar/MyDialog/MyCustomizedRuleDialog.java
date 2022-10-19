package com.xiaobo.smartcalendar.MyDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaobo.smartcalendar.R;

import java.util.ArrayList;
import java.util.List;

public class MyCustomizedRuleDialog extends Dialog implements MyDialogCustiomizedRuleAdapter.OnItemClickListener {

    private Context context;
    private List<String> data;
    private List<String> dataSub;
    private RecyclerView dialogRecyclerView;
    private MyDialogCustiomizedRuleAdapter mDialogCustiomAdapter;
    private MyDialogManager.OnManagerSelectorListener selectorListener;

    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options22Items = new ArrayList<>();

    
    private static boolean isFirstType = true;

    public MyCustomizedRuleDialog(@NonNull Context context, List<String> data, List<String> dataSub) {
        super(context);
        this.context = context;
        this.data = data;
        this.dataSub = dataSub;

        getOptionData();
    }

    public MyCustomizedRuleDialog(@NonNull Context context) {
        super(context);

        this.context = context;
        this.data = data;
        this.dataSub = dataSub;

        getOptionData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.mydialog_member);
        
        this.setCanceledOnTouchOutside(true);

        initViews();
    }

    
    private void initViews() {
        dialogRecyclerView = findViewById(R.id.dialog_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dialogRecyclerView.setLayoutManager(layoutManager);
        dialogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dialogRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        dialogRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(10, 10, 10, 10);
            }
        });
        mDialogCustiomAdapter = new MyDialogCustiomizedRuleAdapter(this.data, this.dataSub);
        mDialogCustiomAdapter.setOnItemClickListener(this);
        dialogRecyclerView.setAdapter(mDialogCustiomAdapter);

    }

    public void setSelectorListener(MyDialogManager.OnManagerSelectorListener selectorListener) {
        this.selectorListener = selectorListener;
    }

    @Override
    public void onItemClick(int position) {

        String item = data.get(position);
        if (position != 0) {
            if (isFirstType) {
                MyCustomizedRuleMassage.get().setFirstInconType(item);
                this.isFirstType = false;

                position -= 1;
                
                MyDialogManager.get(context).setDataCon(options2Items.get(position), options22Items.get(position)).showCuntomRuleRecyclerDialog();

            }
            else {

                MyCustomizedRuleMassage.get().setSecondInconType(item);
                this.isFirstType = true;
            }

            this.dismiss();
        }

    }

    private void getOptionData() {

        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("Before");
        options2Items_01.add("a. is happening");
        options2Items_01.add("b. just ended");
        options2Items_01.add("c. already ended");
        ArrayList<String> options22Items_01 = new ArrayList<>();
        options22Items_01.add("when commuting from event1's location to event2's location, event2 ...");
        options22Items_01.add("");
        options22Items_01.add("");
        options22Items_01.add("");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("Meets");
        options2Items_02.add("a. is happening");
        options2Items_02.add("b. just ended");
        options2Items_02.add("c. already ended");
        ArrayList<String> options22Items_02 = new ArrayList<>();
        options22Items_02.add("when commuting from event1's location to event2's location, event2 ...");
        options22Items_02.add("");
        options22Items_02.add("");
        options22Items_02.add("");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("Overlaps");
        options2Items_03.add("a. not need commute");
        options2Items_03.add("b. is happening");
        options2Items_03.add("c. just ended");
        options2Items_03.add("d. already ended");
        ArrayList<String> options22Items_03 = new ArrayList<>();
        options22Items_03.add("when commuting from event1's location to event2's location, event2 ...");
        options22Items_03.add("");
        options22Items_03.add("");
        options22Items_03.add("");
        options22Items_03.add("");
        ArrayList<String> options2Items_04 = new ArrayList<>();
        options2Items_04.add("Starts");
        options2Items_04.add("a. not need commute");
        options2Items_04.add("b. is happening");
        options2Items_04.add("c. just ended");
        options2Items_04.add("d. already ended");
        options2Items_04.add("e. commute from event2's location to event1's location");
        ArrayList<String> options22Items_04 = new ArrayList<>();
        options22Items_04.add("when commuting from event1's location to event2's location, event2 ...");
        options22Items_04.add("");
        options22Items_04.add("");
        options22Items_04.add("");
        options22Items_04.add("");
        options22Items_04.add("");
        ArrayList<String> options2Items_05 = new ArrayList<>();
        options2Items_05.add("Equals");
        options2Items_05.add("a. not need commute");
        options2Items_05.add("b. commute from event1 to event2");
        options2Items_05.add("c. commute from event1 to event2");
        ArrayList<String> options22Items_05 = new ArrayList<>();
        options22Items_05.add("");
        options22Items_05.add("");
        options22Items_05.add("");
        options22Items_05.add("");
        ArrayList<String> options2Items_06 = new ArrayList<>();
        options2Items_06.add("During");
        options2Items_06.add("a. not need commute");
        options2Items_06.add("b. commute from event2's location to event1's location");
        ArrayList<String> options22Items_06 = new ArrayList<>();
        options22Items_06.add("");
        options22Items_06.add("");
        options22Items_06.add("");
        ArrayList<String> options2Items_07 = new ArrayList<>();
        options2Items_07.add("Finishes");
        options2Items_07.add("a. not need commute");
        options2Items_07.add("b. commute from event2's location to event1's location");
        ArrayList<String> options22Items_07 = new ArrayList<>();
        options22Items_07.add("");
        options22Items_07.add("");
        options22Items_07.add("");


        options2Items.add(options2Items_01);
        options22Items.add(options22Items_01);
        options2Items.add(options2Items_02);
        options22Items.add(options22Items_02);
        options2Items.add(options2Items_03);
        options22Items.add(options22Items_03);
        options2Items.add(options2Items_04);
        options22Items.add(options22Items_04);
        options2Items.add(options2Items_05);
        options22Items.add(options22Items_05);
        options2Items.add(options2Items_06);
        options22Items.add(options22Items_06);
        options2Items.add(options2Items_07);
        options22Items.add(options22Items_07);
    }
}
