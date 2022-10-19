package com.xiaobo.smartcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.xiaobo.smartcalendar.DataBase.MyDataBaseManager;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.Location;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Model.Events.TempEventManager;
import com.xiaobo.smartcalendar.Public.AnimatedExpandableListView;
import com.xiaobo.smartcalendar.Public.CurrentLocation;
import com.xiaobo.smartcalendar.Public.ItemClickSupport;
import com.xiaobo.smartcalendar.Public.ProfileManager;
import com.xiaobo.smartcalendar.Public.PublicVaribale;
import com.xiaobo.smartcalendar.Service.BackgroundService.ReceivedBroadcastService;
import com.xiaobo.smartcalendar.Service.HttpRequest.HttpRequestIntentService;
import com.xiaobo.smartcalendar.Service.Location.LocationService;
import com.xiaobo.smartcalendar.Service.MainService.MainService;
import com.xiaobo.smartcalendar.activity.AddEventActivity.AddEventActivity;
import com.xiaobo.smartcalendar.activity.AddRuleActivity.AddRuleActivity;
import com.xiaobo.smartcalendar.activity.MainActivity.MyBaseExpandableListAdapter;
import com.xiaobo.smartcalendar.activity.MainActivity.MyBaseListViewAdapter;
import com.xiaobo.smartcalendar.activity.SetingActivity.SetingActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements CalendarView.OnViewChangeListener{

    CalendarView mCalendarView;
    String TAG = "MainActivity";

    
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    
    MyDataBaseManager myDataBaseManager;
    List<MyEvent> myEventList;
    ArrayList<Calendar> mCalendarList;
    ArrayList<ArrayList<MyEvent>> mMyEventList;
    
    AnimatedExpandableListView mainList;
    MyBaseExpandableListAdapter mainListAdapter;
    ListView mainListView;
    MyBaseListViewAdapter myMainListViewAdapter;
    
    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        startService(new Intent(getApplicationContext(), MainService.class));
        startService(new Intent(getApplicationContext(), ReceivedBroadcastService.class));
        
        ProfileManager.get(this).getLanguage();
        
        try {
            CurrentLocation.getInstance().setPoint(ProfileManager.get(getApplicationContext()).getHomeLocation().getEndPoint().getLatitude(), ProfileManager.get(getApplicationContext()).getHomeLocation().getEndPoint().getLongitude());
        }
        catch (Exception e) {
            Log.e("MainActivity", "获取默认位置错误");
        }
        
        requestPermission();
        
        requestDrawOverLays();

        setContentView(R.layout.activity_main);

        mCalendarView = findViewById(R.id.calendarView);

        
        initDatabase();
        
        initData();
        

        
        initUI();
        initListener();
        
        calendarViewClicked();
        
        initReceiver();
    }
    
    private void initDatabase() {
        Log.d("MainActivity", "初始化数据库");
        myDataBaseManager = MyDataBaseManager.getInstance();
        myDataBaseManager.initMyDataBaseManager(getApplicationContext());
        TempEventManager.getInstance().initTempEventManager(getApplicationContext());
        MyEventManager.getInstance().initMyEventManager(getApplicationContext());

    }
    private void initData() {
        Log.d("MainActivity", "初始化数据");
        myEventList = MyEventManager.getInstance().getAllEvents();
        
        mCalendarList = MyEventManager.getInstance().getAllCalendar();
        Log.d("MainActivity", mCalendarList.toString());
        mMyEventList = new ArrayList<>();
        for (int i = 0; i < mCalendarList.size(); i++) {
            ArrayList<MyEvent> tempArray = new ArrayList<>();
            for (MyEvent e: MyEventManager.getInstance().getEventsWithCalendar(mCalendarList.get(i))) {
                Log.d("MainActivity", "日期:" + mCalendarList.get(i) + " 事件:" + e.describeMyEvent());
                tempArray.add(e);
            }
            mMyEventList.add(tempArray);
        }
    }

    
    private void initUI() {
        
        mainList = findViewById(R.id.main_list);
        mainListAdapter = new MyBaseExpandableListAdapter(this, mCalendarList, mMyEventList);
        mainList.setAdapter(mainListAdapter);
        for (int i = 0; i < mainListAdapter.getGroupCount(); i++) {
            mainList.expandGroup(i);
        }

        
        




        



//








//

//


//

//


//







//





        









    }

    private void initListener() {
        
        mainList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Log.d("MainActivity", "点击了第" + groupPosition + "组 第" + childPosition + "项ITEM");

                Intent i = new Intent(MainActivity.this, AddEventActivity.class);
                i.putExtra("eventID", mMyEventList.get(groupPosition).get(childPosition).getmId());
                Log.d("MainActivity", "选择了事件:" + mMyEventList.get(groupPosition).get(childPosition).getmId());
                startActivity(i);
                return false;
            }
        });
        
        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (ExpandableListView.getPackedPositionType(l) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    long packPos = mainList.getExpandableListPosition(position);
                    int groupPosition = mainList.getPackedPositionGroup(packPos);
                    int childPosition = mainList.getPackedPositionChild(packPos);
                    Log.d("MainActivity", "长按了第" + groupPosition + "组 第" + childPosition + "项ITEM");

                    
                    final String[] items = { getApplicationContext().getResources().getString(R.string.Delete) + "<" + mMyEventList.get(groupPosition).get(childPosition).getmActivityTitle().getmTitle() + ">?"};
                    android.app.AlertDialog.Builder listDialog = new android.app.AlertDialog.Builder(MainActivity.this);
                    listDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:

                                    UUID eventID = mMyEventList.get(groupPosition).get(childPosition).getmId();
                                    MyEventManager.getInstance().deleteMyEvent(eventID);

                                    
                                    MyTemporalInconsistencyManager.getInstance().deleteMyInconWithEventID(mMyEventList.get(groupPosition).get(childPosition).getmId());

                                    
                                    Intent intent = HttpRequestIntentService.newIntent(MainActivity.this);
                                    Bundle httpBundle = new Bundle();
                                    httpBundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.DELETE_EVENT));
                                    httpBundle.putString("EVENT_ID", String.valueOf(eventID));
                                    intent.putExtras(httpBundle);
                                    startService(intent);

                                    mMyEventList.get(groupPosition).remove(childPosition);

                                    update();
                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    listDialog.show();
                }
                
                return true;
            }
        });
        
        mainList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {

                if (mainList.isGroupExpanded(groupPosition)) {
                    mainList.collapseGroupWithAnimation(groupPosition);
                }
                else {
                    mainList.expandGroupWithAnimation(groupPosition);
                }

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        update();
        mCalendarView.update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        unregisterReceiver(myReceiver);
    }

    
    private void notifyDataSetChanged() {

        mainListAdapter.notifyDataSetInvalidated();
    }

    
    private void update() {

        initData();
        initUI();
        notifyDataSetChanged();
        setSignOnCalendar();

    }

    
    private void setSignOnCalendar() {
        Map<String, Calendar> schemes = new HashMap<>();
        for (MyEvent event : myEventList) {
            int year = event.getmDateOfEvent().getCalendar().getYear();
            int month = event.getmDateOfEvent().getCalendar().getMonth();
            int day = event.getmDateOfEvent().getCalendar().getDay();
            schemes.put(getSchemeCalendar(year, month, day, MainActivity.this.getResources().getColor(R.color.colorPrimary), this.getResources().getString(R.string.thing)).toString(),
                        getSchemeCalendar(year, month, day, MainActivity.this.getResources().getColor(R.color.colorPrimary), this.getResources().getString(R.string.thing)));
        }
        mCalendarView.setSchemeDate(schemes);
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                showPopupMenu(findViewById(R.id.menu_item_add));
                break;
            case R.id.menu_item_today:

                
                try {
                    Intent i = HttpRequestIntentService.newIntent(MainActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.UP_INCON));
                    bundle.putString("INCON_ID", String.valueOf(MyTemporalInconsistencyManager.getInstance().getAllMyIncons().get(0).getmID()));
                    i.putExtras(bundle);
                    startService(i);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.menu_item_set:
                Intent intent = new Intent(MainActivity.this, SetingActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        
        popupMenu.getMenuInflater().inflate(R.menu.main_popup_menu, popupMenu.getMenu());
        popupMenu.show();
        
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.add_event_menubutton:
                        
                        Intent ievent = new Intent(MainActivity.this, AddEventActivity.class);
                        ievent.putExtra("selectedDate", mCalendarView.getSelectedCalendar());
                        startActivity(ievent);
                        break;
                    case R.id.add_rule_menubutton:
                        
                        Intent irule = new Intent(MainActivity.this, AddRuleActivity.class);
                        startActivity(irule);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {

            }
        });
    }

    
    @Override
    public void onViewChange(boolean isMonthView) {

    }

    
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String city = bundle.getString("city");
            String district = bundle.getString("district");
            String latitude = bundle.getString("latitude");
            String longitude = bundle.getString("longitude");
            Log.d("MainActivity", "接收到定位广播:" + city + " " + district + " " + latitude + " " + longitude);
            Location location = new Location();
            location.setEndPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            ProfileManager.get(getApplicationContext()).saveHomeLocation(location);
        }
    }

    private void initReceiver() {
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xiaobo.locationservice");
        registerReceiver(myReceiver, filter);
        
        IntentFilter filter_saveEvent = new IntentFilter();
        filter_saveEvent.addAction("com.save_event_message");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("MainActivity", "保存好了事件, 刷新页面");
                update();
            }
        }, filter_saveEvent);
    }

    
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            StringBuffer str = new StringBuffer();
            if (aMapLocation != null) {
                
                str.append("定位成功");
            }
            else {
                
                str.append("定位失败");
            }

            Log.d("MainActivity", str.toString());
        }
    };
    private Calendar getSchemeCalendar(int year, int month, int day, int color,String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);     
        calendar.setScheme(text);



        return calendar;
    }


    
    private void calendarViewClicked() {
        mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                Log.d("Alert", "您点击了日期:" + mCalendarView.getSelectedCalendar().toString() + " 列表更新数据");
            }
        });
    }

    
    @SuppressLint("WrongConstant")
    
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "未获得定位权限");
            AndPermission.with(MainActivity.this)
                    .runtime()
                    .permission(Permission.Group.LOCATION)
                    .onGranted(permissions -> {
                        
                        Log.d(TAG, "用户同意了权限申请,开启定位服务");
                        
                        startService(new Intent(getApplicationContext(), LocationService.class));
                    })
                    .onDenied(permissions -> {
                        
                        Log.e(TAG, "用户拒绝了权限申请");
                    })
                    .start();
        }
        else {
            Log.d(TAG, "已获得了定位权限");
            
            startService(new Intent(getApplicationContext(), LocationService.class));
        }
    }
    
    
    public static int OVERLAY_PERMISSION_REQ_CODE = 1002;
    public void requestDrawOverLays() {
        if (!Settings.canDrawOverlays(MainActivity.this)) {
            Toast.makeText(this, "您还没有打开悬浮窗权限", Toast.LENGTH_SHORT).show();
            
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setTitle("悬浮窗权限")
                    .setMessage("您还没有打开悬浮窗权限")
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface  dialog, int which) {
                            
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + MainActivity.this.getPackageName()));
                            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                        }
                    }).create();
            alert.show();

        } else {
            
            Log.d(TAG, "您已经打开了悬浮窗权限");
        }
    }
}