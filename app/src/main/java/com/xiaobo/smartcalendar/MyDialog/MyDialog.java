package com.xiaobo.smartcalendar.MyDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.activity.AdjustActivity.AdjustActivity;

import java.util.ArrayList;
import java.util.List;

public class MyDialog extends Dialog implements MyDialogMemberAdapter.OnItemClickListener{

    private Context mContext;
    private List<MyTemporalInconsistency> data = new ArrayList<>();
    private RecyclerView myDialogRecyclerView;
    private MyDialogMemberAdapter myDialogMemberAdapter;

    public MyDialog(@NonNull Context context, List<MyTemporalInconsistency> data) {
        super(context);
        this.mContext = context;
        this.data = data;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.mydialog_member);
        
        this.setCanceledOnTouchOutside(true);

        Window window = this.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (attributes != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    attributes.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                }
                else {
                    attributes.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                }
            }
            window.setAttributes(attributes);
        }

        initViews();
    }

    
    private void initViews() {
        myDialogRecyclerView = findViewById(R.id.dialog_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        myDialogRecyclerView.setLayoutManager(layoutManager);
        myDialogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myDialogRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        myDialogRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(10, 10, 10, 10);
            }
        });
        myDialogMemberAdapter = new MyDialogMemberAdapter(data);
        myDialogMemberAdapter.setOnItemClickListener(this);
        myDialogRecyclerView.setAdapter(myDialogMemberAdapter);
    }

    @Override
    public void onItemClick(int position) {
        MyTemporalInconsistency tempIncon = data.get(position);
        
        Intent i = new Intent(mContext, AdjustActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("InconID", tempIncon.getmID());
        bundle.putString("Event1ID", tempIncon.getUuid_event1().toString());
        bundle.putString("Event2ID", tempIncon.getUuid_event2().toString());
        this.dismiss();
        i.putExtras(bundle);

        tempIncon.setHandled(MyTemporalInconsistency.Handled.postponed);

        mContext.startActivity(i);
    }

    @Override
    public void onIgnoreButtonClick(int position) {
        
        MyTemporalInconsistencyManager.getInstance().setInconHandled(data.get(position).getmID(), MyTemporalInconsistency.Handled.postponed);
        this.data.remove(position);
        if (this.data.size() < 1) {
            this.dismiss();
        }

        
        myDialogMemberAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditButtonClick(int position) {
        MyTemporalInconsistency tempIncon = data.get(position);
        
        Intent i = new Intent(mContext, AdjustActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("InconID", tempIncon.getmID());
        bundle.putString("Event1ID", tempIncon.getUuid_event1().toString());
        bundle.putString("Event2ID", tempIncon.getUuid_event2().toString());
        this.dismiss();
        i.putExtras(bundle);

        tempIncon.setHandled(MyTemporalInconsistency.Handled.postponed);

        mContext.startActivity(i);
    }
}
