package com.xiaobo.smartcalendar.activity.TestActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.MyDialog.MyDialogManager;
import com.xiaobo.smartcalendar.MyDialog.MyDialogOfInconResponse;
import com.xiaobo.smartcalendar.R;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    ListView tableListView;
    TableListViewAdapter tableListViewAdapter;
    TableListViewAdapter.TableType mTableType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                String tableType = bundle.getString("TABLE_TYPE");
                if ("MYEVENT".equals(tableType)) {
                    mTableType = TableListViewAdapter.TableType.myEvent;
                }
                else {
                    mTableType = TableListViewAdapter.TableType.myTemporalInconsistency;
                }

                tableListView = findViewById(R.id.test_listview);
                tableListViewAdapter = new TableListViewAdapter(TableActivity.this, mTableType);
                tableListView.setAdapter(tableListViewAdapter);
            }
            catch (Exception e) {
                Log.e("TableActivity", "获取上级信息错误");
            }
        }

        tableListView = findViewById(R.id.test_listview);
        tableListViewAdapter = new TableListViewAdapter(TableActivity.this, mTableType);
        tableListView.setAdapter(tableListViewAdapter);

        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setClickAction(i);
            }
        });
    }

    
    private void setClickAction(int position) {
        
        if (mTableType == TableListViewAdapter.TableType.myEvent) {
            
            List<MyEvent> tempList = MyEventManager.getInstance().getAllEvents();
            MyEvent tempEvent = tempList.get(position);
            Log.d("TableActivity", "将事件:" + tempEvent.getmId().toString() + "开始时间推迟1小时, 结束时间推迟0.5小时");
            Log.d("TableActivity", "调整前的事件为:" + tempEvent.describeMyEvent());
            tempEvent.adjustStartTime(MyTemporalInconsistency.EventAction.postpone, 1*60*60*1000);
            tempEvent.adjustEndTime(MyTemporalInconsistency.EventAction.postpone, 30*60*1000);
            Log.d("TableActivity", "调整后的事件为:" + tempEvent.describeMyEvent());
            Log.d("TableActivity", "调整后的事件时间为:" + tempEvent.getmDateOfEvent().showDate());
            Log.d("TableActivity", "调整没有保存");

        }
        
        if (mTableType == TableListViewAdapter.TableType.myTemporalInconsistency) {
            List<MyTemporalInconsistency> temp = new ArrayList<>();
            for (MyTemporalInconsistency item : MyTemporalInconsistencyManager.getInstance().getAllMyIncons()) {
                if (item.getHandled() == MyTemporalInconsistency.Handled.original) {
                    temp.add(item);
                }
            }
            if (temp.size() > 0) {
                Log.d("TableActivity", "弹出冲突Dialog");
                MyDialogManager.get(this).setData(temp).showRecyclerDialog();

            }
        }

    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tableoption, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                Log.d("TableActivity", "点击了清空列表按钮(但是现在没有清空)");
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}