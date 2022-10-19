package com.xiaobo.smartcalendar.activity.AddEventActivity;

import static com.xiaobo.smartcalendar.Public.PublicVaribale.ENDPOINT_LOCATION;
import static com.xiaobo.smartcalendar.Public.PublicVaribale.POISEARCHACTIVITY_REQUEST;
import static com.xiaobo.smartcalendar.Public.PublicVaribale.POISEARCHACTIVITY_RESULT;
import static com.xiaobo.smartcalendar.Public.PublicVaribale.STARTPOINT_LOTATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;

import com.amap.api.services.core.LatLonPoint;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistencyManager;
import com.xiaobo.smartcalendar.Model.Events.ActivityTitle;
import com.xiaobo.smartcalendar.Model.Events.DateOfEvent;
import com.xiaobo.smartcalendar.Model.Events.Location;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;
import com.xiaobo.smartcalendar.Model.Events.Participant;
import com.xiaobo.smartcalendar.Model.Events.Periodicity;
import com.xiaobo.smartcalendar.Model.Events.TargetLocation;
import com.xiaobo.smartcalendar.Model.Events.TempEventManager;
import com.xiaobo.smartcalendar.Model.Events.TypeOfEvent;
import com.xiaobo.smartcalendar.Public.CurrentLocation;
import com.xiaobo.smartcalendar.Public.PublicVaribale;
import com.xiaobo.smartcalendar.R;
import com.xiaobo.smartcalendar.Service.BackgroundService.ReceivedBroadcastService;
import com.xiaobo.smartcalendar.Service.CalRoute.CalRouteIntentService;
import com.xiaobo.smartcalendar.Service.HttpRequest.HttpRequestIntentService;
import com.xiaobo.smartcalendar.Service.Location.LocationService;
import com.xiaobo.smartcalendar.activity.PoiSearchWithoutMap.PoiInfoModel;
import com.xiaobo.smartcalendar.activity.PoiSearchWithoutMap.PoiSearchWithoutMapActivity;
import com.xiaobo.smartcalendar.activity.SearchLocationActivity.SearchLocationActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView itemListView;
    private AddEventAdapter mAddEventAdapter;
    private LinkedList<EventItem> mData = null;

    private Date mDate;
    private TempEventManager.TempEvent tempEvent;
    private LatLonPoint mTargetPoint = ENDPOINT_LOCATION;
    private String mTargetName = "新城吾悦广场";
    private LatLonPoint mStartPoint = STARTPOINT_LOTATION;
    TargetLocation perTargetLocation = new TargetLocation();

    private MyReceiver myReceiver = null;
    
    private boolean sendCalRoute = false;

    private com.haibin.calendarview.Calendar mCalendar;

    private ActivityType activityType = ActivityType.AddEventActivity;

    enum ActivityType {
        AddEventActivity,
        ShowEventActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mDate = new Date(new Date().getTime() + (5 * 60 * 60 * 1000));
        
        tempEvent = TempEventManager.getInstance().createEvent();

        
        Intent i = getIntent();

        mCalendar = (com.haibin.calendarview.Calendar)i.getSerializableExtra("selectedDate");

        try {
            UUID eventID = (UUID)i.getSerializableExtra("eventID");
            if (eventID != null) {
                this.activityType = ActivityType.ShowEventActivity;
                Log.d("AddEventAcitivity", "修改事件页面");
                MyEvent e = MyEventManager.getInstance().getMyEvent(eventID);
                tempEvent.setmId(e.getmId());
                mData = new LinkedList<>();
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_TYPE), e.getmTypeOfEvent().getmKind().getTypeName(), EventItem.TypeItem.item_type_1));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_TITLE), e.getmActivityTitle().getmTitle(), EventItem.TypeItem.item_type_2));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_STARTTIME), e.getmDateOfEvent().getDate(), EventItem.TypeItem.item_type_1, this));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_ENDTIME), new Date(e.getmDateOfEvent().getDate().getTime() + e.getmDateOfEvent().getDuration()), EventItem.TypeItem.item_type_1, this));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_HOST), e.getmHost(), EventItem.TypeItem.item_type_2));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_PARTICIPANT), e.getmParticipant().getmParticipant(), EventItem.TypeItem.item_type_2));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_PERVIOUSLOCATION), e.getmTargetLocation().getmCity(), EventItem.TypeItem.item_type_1));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_LOCATION), e.getmTargetLocation().getTargetName(), EventItem.TypeItem.item_type_1));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_PERIODICITY), e.getmPeriodicity().getmTypeOfPeriodicity().getTypeName(), EventItem.TypeItem.item_type_1));
                mData.add(new EventItem(this.getResources().getString(R.string.EVENT_IMPORTANCE), e.getmImportance(), EventItem.TypeItem.item_type_3));

                mData.add(new EventItem(this.getResources().getString(R.string.NOT_NECESSARY),"", EventItem.TypeItem.item_type_4));

                mData.add(new EventItem(this.getResources().getString(R.string.MINIMUM_DURATION), "--小时--分钟", EventItem.TypeItem.item_type_1));
                mData.add(new EventItem(this.getResources().getString(R.string.EARLIEST_START_TIME), new Date(0), EventItem.TypeItem.item_type_1, this));
                mData.add(new EventItem(this.getResources().getString(R.string.LATEST_END_TIME), new Date(0), EventItem.TypeItem.item_type_1, this));


                mCalendar = tempEvent.getmDateOfEvent().getCalendar();

            }
        }catch (Exception e) {

        }

        
        initUI();

        


        
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xiaobo.locationservice");
        registerReceiver(myReceiver, filter);
    }

    
    private void initUI() {
        itemListView = findViewById(R.id.add_event_listview);
        if (this.activityType == ActivityType.AddEventActivity) {
            setData();
        }
        mAddEventAdapter = new AddEventAdapter(this, mData);
        itemListView.setAdapter(mAddEventAdapter);
        itemListView.setOnItemClickListener(this);

        try {
            String city = CurrentLocation.getInstance().getCity();
            setAddress(city);
            mStartPoint = new LatLonPoint(CurrentLocation.getInstance().getLatitude(), CurrentLocation.getInstance().getLongitude());
            setPerEventLocation(mDate);
            Log.d("AddEventActivity", "当前城市为" + city);
        }
        catch (Exception e) {
            Log.e("AddEventActivity", "获取本地地理信息错误" + e);
        }
    }

    
    private void setPerEventLocation(Date date) {
        try {

            
            Date sT = mData.get(2).getItemDate();
            Date eT = mData.get(3).getItemDate();
            Calendar tempCal = Calendar.getInstance();
            tempCal.setTime(sT);
            int year = tempCal.get(Calendar.YEAR);
            int month = tempCal.get(Calendar.MARCH) + 1;
            int day = tempCal.get(Calendar.DATE);
            mCalendar.setYear(year);
            mCalendar.setMonth(month);
            mCalendar.setDay(day);
            DateOfEvent dateOfEvent = new DateOfEvent(sT, eT, mCalendar);
            tempEvent.setmDateOfEvent(dateOfEvent);
            Log.d("AddEventActivity", "获取上一事件地址" + sT);
            perTargetLocation = MyEventManager.getInstance().getRecentEvent(tempEvent).getmTargetLocation();
            Log.d("AddEventActivity", "上一事件的地址信息" + perTargetLocation.describeLocation());
            LatLonPoint perEventPoint = CurrentLocation.getInstance().existStartPoint(tempEvent);
            mStartPoint = (perEventPoint != null) ? perEventPoint : mStartPoint;
        }
        catch (Exception e) {
            Log.e("AddEventActivity", "获取上一事件地理信息错误" + e);
        }
    }

    
    private void setData() {
        mData = new LinkedList<>();
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_TYPE), this.getResources().getString(R.string.EVENT_TYPE), EventItem.TypeItem.item_type_1));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_TITLE), this.getResources().getString(R.string.EVENT_TITLE), EventItem.TypeItem.item_type_2));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_STARTTIME), mDate, EventItem.TypeItem.item_type_1, this));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_ENDTIME), new Date(mData.get(2).getItemDate().getTime() + 7200000), EventItem.TypeItem.item_type_1, this));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_HOST), "张三", EventItem.TypeItem.item_type_2));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_PARTICIPANT), "李四", EventItem.TypeItem.item_type_2));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_PERVIOUSLOCATION), "定位中", EventItem.TypeItem.item_type_1));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_LOCATION), "目标地点", EventItem.TypeItem.item_type_1));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_PERIODICITY), "从不", EventItem.TypeItem.item_type_1));
        mData.add(new EventItem(this.getResources().getString(R.string.EVENT_IMPORTANCE), "重要(单选框)", EventItem.TypeItem.item_type_3));

        mData.add(new EventItem(this.getResources().getString(R.string.NOT_NECESSARY),"", EventItem.TypeItem.item_type_4));

        mData.add(new EventItem(this.getResources().getString(R.string.MINIMUM_DURATION), "--" + this.getResources().getString(R.string.HOUR) + "--" + this.getResources().getString(R.string.MINUTE), EventItem.TypeItem.item_type_1));
        mData.add(new EventItem(this.getResources().getString(R.string.EARLIEST_START_TIME), new Date(0), EventItem.TypeItem.item_type_1, this));
        mData.add(new EventItem(this.getResources().getString(R.string.LATEST_END_TIME), new Date(0), EventItem.TypeItem.item_type_1, this));



        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(mDate);
        int year = tempCal.get(Calendar.YEAR);
        int month = tempCal.get(Calendar.MARCH) + 1;
        int day = tempCal.get(Calendar.DATE);
        mCalendar.setYear(year);
        mCalendar.setMonth(month);
        mCalendar.setDay(day);
    }

    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("AddEventActivity", "点击了第" + position + "项");

        switch (position) {
            case 0:
                
                showSingleElectionAlert(0, TypeOfEvent.Kind.KindStrings());
                break;
            case 1:
                
                break;
            case 2:
                
                showTimePickerView(new Date(), 2);
                break;
            case 3:
                
                showTimePickerView(new Date(System.currentTimeMillis() + 7200000), 3);
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                

                break;
            case 7:
                
                Intent i = new Intent(this, PoiSearchWithoutMapActivity.class);
                
                Bundle bundleInfo = new Bundle();

                i.putExtras(bundleInfo);
                startActivityForResult(i, POISEARCHACTIVITY_REQUEST);
                break;
            case 8:
                
                showSingleElectionAlert(8, Periodicity.TypeOfPeriodicity.TypesOfPeriodicityStrings());
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mData.set(11, new EventItem("最短持续时间", String.format("%02d", hourOfDay) + "小时" + String.format("%02d", minute) + "分钟", EventItem.TypeItem.item_type_1));
                        mData.get(11).setMinimumduration(hourOfDay*60*60*1000 + minute*60*1000);
                        refresh();
                    }
                }, 0, 0, true);
                timePickerDialog.show();
                break;
            case 12:
                
                if (mData.get(12).getItemDate().compareTo(new Date(0)) == 0) {
                    showTimePickerView(mData.get(2).getItemDate(), 12);
                }
                else {
                    showTimePickerView(mData.get(12).getItemDate(), 12);
                }
                break;
            case 13:
                
                if (mData.get(13).getItemDate().compareTo(new Date(0)) == 0) {
                    showTimePickerView(mData.get(3).getItemDate(), 13);
                }
                else {
                    showTimePickerView(mData.get(13).getItemDate(), 13);
                }
                break;
            default:
                break;

        }
    }

    
    private void refresh() {
        setPerEventLocation(mDate);
        mAddEventAdapter.notifyDataSetChanged();
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_event:
                Log.d("AddEventActivity", "点击了保存确认按钮");

                
                TypeOfEvent type = new TypeOfEvent(TypeOfEvent.Kind.getTypeFromName(mData.get(0).getItemDetail()));
                
                ActivityTitle title = new ActivityTitle(mData.get(1).getItemDetail());
                
                Date sT = mData.get(2).getItemDate();
                Date eT = mData.get(3).getItemDate();
                Calendar tempCal = Calendar.getInstance();
                tempCal.setTime(sT);
                int year = tempCal.get(Calendar.YEAR);
                int month = tempCal.get(Calendar.MARCH) + 1;
                int day = tempCal.get(Calendar.DATE);
                mCalendar.setYear(year);
                mCalendar.setMonth(month);
                mCalendar.setDay(day);
                DateOfEvent dateOfEvent = new DateOfEvent(sT, eT, mCalendar);
                
                String host = mData.get(4).getItemDetail();
                
                Participant participant = new Participant(mData.get(5).getItemDetail());
                
                Periodicity periodicity = new Periodicity(Periodicity.TypeOfPeriodicity.getTypeFromStr(mData.get(8).getItemDetail()));

                
                TargetLocation location = new TargetLocation(mTargetPoint, mTargetName);

                tempEvent.setmTypeOfEvent(type);
                tempEvent.setmActivityTitle(title);
                tempEvent.setmTargetLocation(location);
                tempEvent.setmDateOfEvent(dateOfEvent);
                tempEvent.setmParticipant(participant);
                tempEvent.setmPeriodicity(periodicity);
                tempEvent.setmHost(host);

                tempEvent.setCompleteEdit(true);



                if (!sendCalRoute) {
                    calRoute(mStartPoint, mTargetPoint, tempEvent.getmId());
                }
                if (this.activityType == ActivityType.ShowEventActivity) {
                    Log.d("AddEventActivity", "确定按钮 修改事件");
                    Log.d("AddEventActivity", tempEvent.describeMyEvent());
                    MyEventManager.getInstance().resetMyEvent(tempEvent.getmId(), tempEvent);
                }
                if (tempEvent.isCompleteCalDriveRoute()) {
                    Intent i = HttpRequestIntentService.newIntent(AddEventActivity.this);
                    if (activityType == ActivityType.ShowEventActivity) {
                        Log.d("AddEventActivity", "准备修改事件");
                        Bundle httpBundle = new Bundle();
                        httpBundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.REVISE_EVENT));
                        httpBundle.putString("EVENT_ID", String.valueOf(tempEvent.getmId()));
                        i.putExtras(httpBundle);
                    }
                    else {
                        if (tempEvent.isCompleteEdit() && tempEvent.isCompleteCalDriveRoute()) {
                            TempEventManager.getInstance().saveEvents();
                        }
                        Log.d("AddEventActivity", "准备上传事件");
                        Bundle httpBundle = new Bundle();
                        httpBundle.putString("REQUEST_TYPE", String.valueOf(PublicVaribale.RequestType.UP_EVENT));
                        httpBundle.putString("EVENT_ID", String.valueOf(tempEvent.getmId()));
                        i.putExtras(httpBundle);
                    }
                    startService(i);
                    













                }

                


                
                this.finish();
                break;

            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    
    public void saveEditData(int position, String str) {
        Log.d("SmartCalendar", "编辑了第" + position + "项, 内容:" + str);

        mData.set(position, new EventItem(mData.get(position).getItemTitle(), str, EventItem.TypeItem.item_type_2));
    }

    
    public void saveCheckBoxData(int position, Boolean checked) {
        String str = null;
        Boolean isImportant = false;
        if (checked) {
            str = "被选中";
            isImportant = true;
        }
        else {
            str = "未被选中";
            isImportant = false;
        }



        mData.set(position, new EventItem(mData.get(position).getItemTitle(), isImportant, EventItem.TypeItem.item_type_3));
    }

    
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String city = bundle.getString("city");
            String latitude = bundle.getString("latitude");
            String longitude = bundle.getString("longitude");

            Log.d("AddEventAcitvity", "接受到广播city" + city);
        }
    }
    private void setAddress(String address) {
        mData.set(6, new EventItem(this.getResources().getString(R.string.EVENT_PERVIOUSLOCATION), address, EventItem.TypeItem.item_type_1));
        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        stopService(new Intent(getApplicationContext(), LocationService.class));
        
        unregisterReceiver(myReceiver);
    }

    
    private void showSingleElectionAlert(int index, String[] mArray) {
        EventItem eventItem = mData.get(index);
        if (mArray.length > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
            builder.setTitle(eventItem.getItemTitle());

            builder.setSingleChoiceItems(mArray, eventItem.getCheckedItem(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mData.set(index, new EventItem(eventItem.getItemTitle(), mArray[which], eventItem.itemType));
                    mData.get(index).setCheckedItem(which);

                    refresh();
                }
            });
            builder.show();
        }
    }

    
    private void showTimePickerView(Date oldDate, int selectedItem) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        TimePickerView pvTime = new TimePickerBuilder(AddEventActivity.this, new OnTimeSelectListener() {
            
            @Override
            public void onTimeSelect(Date date, View v) {
                
                date = new Date(date.getTime() / 100000 * 100000);
                Log.d("时间选择器", "选择了时间" + date.getTime());
                mData.set(selectedItem, new EventItem(mData.get(selectedItem).getItemTitle(), date, EventItem.TypeItem.item_type_1, AddEventActivity.this) );
                switch (selectedItem) {
                    case 2:










                }
                refresh();
            }
        })
                .setType(new boolean[]{false, true, true, true, true, false})
                .setOutSideCancelable(true)
                .setCancelText(this.getResources().getString(R.string.CANCLE))
                .setSubmitText(this.getResources().getString(R.string.DONE))
                .setItemVisibleCount(5)
                .setDate(calendar)
                .isDialog(true)
                .build();


        pvTime.show();


    }

    
    private void calRoute(LatLonPoint startPoint, LatLonPoint endPoint, UUID uuid) {
        Intent i = CalRouteIntentService.newIntent(this);
        Bundle bundle = new Bundle();
        bundle.putString("start_lat", String.valueOf(startPoint.getLatitude()));
        bundle.putString("start_lng", String.valueOf(startPoint.getLongitude()));
        bundle.putString("lat", String.valueOf(endPoint.getLatitude()));
        bundle.putString("lng", String.valueOf(endPoint.getLongitude()));
        bundle.putString("UUID", String.valueOf(uuid));
        i.putExtras(bundle);
        startService(i);
        sendCalRoute = true;
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case POISEARCHACTIVITY_REQUEST:
                if (resultCode == POISEARCHACTIVITY_RESULT) {
                    
                    Bundle bundle = data.getExtras();
                    String lon = bundle.getString("lon");
                    String lat = bundle.getString("lat");
                    String title = bundle.getString("title");
                    String address = bundle.getString("address");
                    PoiInfoModel model = new PoiInfoModel(lon, lat, title, address);
                    mData.set(7, new EventItem(this.getResources().getString(R.string.EVENT_LOCATION), model.getTitle(), EventItem.TypeItem.item_type_1));
                    mTargetPoint = new LatLonPoint(Double.valueOf(lat), Double.valueOf(lon));
                    mTargetName = String.valueOf(model.getTitle());

                    
                    if (mTargetName.equals(perTargetLocation.getTargetName())) {
                        Log.d("AddEventActivity", "事件地点与上一事件地点相同, 不再计算通勤时间, 直接设置为0");
                        TempEventManager.getInstance().setCommutingTime(tempEvent.getmId(), 0);

                    }
                    else {
                        
                        calRoute(mStartPoint, mTargetPoint, tempEvent.getmId());
                    }
                    sendCalRoute = true;
                    Log.d("AddEventActivity", "修改计算通勤时间标识为:" + sendCalRoute);
                    refresh();
                }
                else {
                    Log.e("AddEventActivity", "POI搜索页面没有正确的返回数据");
                }
                break;

            default:
                break;
        }
    }
}