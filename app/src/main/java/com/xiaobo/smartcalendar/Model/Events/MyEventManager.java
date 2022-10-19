package com.xiaobo.smartcalendar.Model.Events;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.xiaobo.smartcalendar.DataBase.MyDataBaseManager;
import com.xiaobo.smartcalendar.DataBase.MyEventCursorWrapper;
import com.xiaobo.smartcalendar.DataBase.MyEventDbSchema;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MyEventManager {

    private static MyEventManager sMyEventManager;
    private Context mContext;
    private MyDataBaseManager myDataBaseManager;

    public static MyEventManager getInstance() {
        if (sMyEventManager == null) {
            sMyEventManager = new MyEventManager();

        }
        return sMyEventManager;
    }
    public void initMyEventManager(Context mContext) {
        this.mContext = mContext;
    }

    private MyEventManager() {
        myDataBaseManager = MyDataBaseManager.getInstance();
    }

    public void addMyEventToTable(MyEvent e) {
        Log.d("MyEventManager", "将要添加的事件信息\n" + e.describeMyEvent());
        ContentValues values = MyEventManager.getContentValues(e);
        MyDataBaseManager.getInstance().insertData(MyEventDbSchema.MyEventTable.NAME, values);
    }

    
    private static ContentValues getContentValues(MyEvent myEvent) {

        ContentValues values = new ContentValues();

        values.put(MyEventDbSchema.MyEventTable.Cols.UUID, myEvent.getmId().toString());
        values.put(MyEventDbSchema.MyEventTable.Cols.TITLE, myEvent.getmActivityTitle().getmTitle());
        values.put(MyEventDbSchema.MyEventTable.Cols.START_TIME, myEvent.getmDateOfEvent().getDate().getTime());
        values.put(MyEventDbSchema.MyEventTable.Cols.CALENDAR, myEvent.getmDateOfEvent().getCalendar().toString());
        values.put(MyEventDbSchema.MyEventTable.Cols.DURATION, myEvent.getmDateOfEvent().getDuration());
        values.put(MyEventDbSchema.MyEventTable.Cols.TYPE, myEvent.getmTypeOfEvent().mKind.getTypeName());
        values.put(MyEventDbSchema.MyEventTable.Cols.LOCATION, myEvent.getmTargetLocation().getTargetName());
        values.put(MyEventDbSchema.MyEventTable.Cols.LONGITUDE, String.valueOf(myEvent.getmTargetLocation().getTargetPoint().getLongitude()));
        values.put(MyEventDbSchema.MyEventTable.Cols.LATITUDE, String.valueOf(myEvent.getmTargetLocation().getTargetPoint().getLatitude()));
        values.put(MyEventDbSchema.MyEventTable.Cols.HOST, myEvent.getmHost());
        values.put(MyEventDbSchema.MyEventTable.Cols.PARTICIPANT, myEvent.getmParticipant().getmParticipant());
        values.put(MyEventDbSchema.MyEventTable.Cols.PERIODICITY, myEvent.getmPeriodicity().mTypeOfPeriodicity.getTypeName());
        values.put(MyEventDbSchema.MyEventTable.Cols.COMMUTING_TIME, myEvent.getCommutingTime());
        values.put(MyEventDbSchema.MyEventTable.Cols.IMPORTANT, myEvent.getmImportance().toString());

        values.put(MyEventDbSchema.MyEventTable.Cols.MINIMUM_DURATION, myEvent.getMinimum_duration());
        values.put(MyEventDbSchema.MyEventTable.Cols.EARLIEST_START_TIME, myEvent.getEarliest_start_time().getTime());
        values.put(MyEventDbSchema.MyEventTable.Cols.LATEST_START_TIME, myEvent.getLatest_start_time().getTime());
        values.put(MyEventDbSchema.MyEventTable.Cols.EARLIEST_END_TIME, myEvent.getEarliest_end_time().getTime());
        values.put(MyEventDbSchema.MyEventTable.Cols.LATEST_END_TIME, myEvent.getLatest_end_time().getTime());

        values.put(MyEventDbSchema.MyEventTable.Cols.CITY, myEvent.getmTargetLocation().getmCity());

        return values;
    }

    
    public MyEvent getMyEvent(UUID uuid) {
        MyEventCursorWrapper cursorWrapper = this.myDataBaseManager.queryData(
                MyEventDbSchema.MyEventTable.NAME,
                MyEventDbSchema.MyEventTable.Cols.UUID + "=?",
                new String[] {uuid.toString()}
        );
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getEvent();
        } finally {
            cursorWrapper.close();
        }
    }

    
    public JSONObject getJSONWithID(UUID uuid) {
        JSONObject jsonObject = new JSONObject();
        MyEvent myEvent = getMyEvent(uuid);
        try {
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols._id, myEvent.getmId());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.UUID, myEvent.getmId().toString());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.TITLE, myEvent.getmActivityTitle().getmTitle());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.START_TIME, myEvent.getmDateOfEvent().getDate().getTime());

            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.CALENDAR, sdf.format(myEvent.getmDateOfEvent().getDate()));

            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.DURATION, myEvent.getmDateOfEvent().getDuration());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.TYPE, myEvent.getmTypeOfEvent().mKind.getTypeName());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.LOCATION, myEvent.getmTargetLocation().getTargetName());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.LONGITUDE, myEvent.getmTargetLocation().getTargetPoint().getLongitude());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.LATITUDE, myEvent.getmTargetLocation().getTargetPoint().getLatitude());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.HOST, myEvent.getmHost());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.PARTICIPANT, myEvent.getmParticipant().getmParticipant());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.PERIODICITY, myEvent.getmPeriodicity().mTypeOfPeriodicity.getTypeName());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.IMPORTANT, myEvent.getmImportance().toString());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.COMMUTING_TIME, myEvent.getCommutingTime());

            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.MINIMUM_DURATION, myEvent.getMinimum_duration());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.EARLIEST_START_TIME, myEvent.getEarliest_start_time().getTime());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.LATEST_START_TIME, myEvent.getLatest_start_time().getTime());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.EARLIEST_END_TIME, myEvent.getEarliest_end_time().getTime());
            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.LATEST_END_TIME, myEvent.getLatest_end_time().getTime());

            jsonObject.put(MyEventDbSchema.MyEventTable.Cols.CITY, myEvent.getmTargetLocation().getmCity());
        }
        catch (Exception e) {
            Log.e("MyEventManager", "获取事件json编码错误" + e);
        }

        return jsonObject;
    }

    
    public List<MyEvent> getAllEvents() {
        List<MyEvent> myEvents = new ArrayList<>();
        MyEventCursorWrapper cursor = this.myDataBaseManager.queryData(MyEventDbSchema.MyEventTable.NAME, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                myEvents.add(cursor.getEvent());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return myEvents;
    }

    
    public ArrayList<Calendar> getAllCalendar() {
        List<MyEvent> myEvents = this.getAllEvents();
        ArrayList<Calendar> calendarArrayList = new ArrayList<>();
        Set<String> setCalendarList = new HashSet<>();
        for (MyEvent e : myEvents) {
            if (setCalendarList.add(e.getmDateOfEvent().getCalendar().toString())) {
                calendarArrayList.add(e.getmDateOfEvent().getCalendar());
            }
        }
        Collections.sort(calendarArrayList);
        return calendarArrayList;
    }

    
    public List<MyEvent>  getEventsWithCalendar(Calendar calendar) {

        List<MyEvent> tempList = new ArrayList<>();

        MyEventCursorWrapper cursor = this.myDataBaseManager.queryData(
                MyEventDbSchema.MyEventTable.NAME, MyEventDbSchema.MyEventTable.Cols.CALENDAR + " = ? ", new String[]{ calendar.toString()}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tempList.add(cursor.getEvent());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return tempList;

    }

    
    public void resetMyEvent(UUID oldEventID, MyEvent newEvent) {
        
        this.myDataBaseManager.deleteData(MyEventDbSchema.MyEventTable.NAME, oldEventID.toString());
        
        this.addMyEventToTable(newEvent);
        sendMessage();
    }

    public Long deleteMyEvent(UUID id) {
        Log.e("MyEventManager", "删除事件" + id);
        Long result = this.myDataBaseManager.deleteData(MyEventDbSchema.MyEventTable.NAME, id.toString());

        

        sendMessage();
        return result;
    }

    
    public MyEvent getRecentEvent(MyEvent e) {
        Date date = e.getmDateOfEvent().getDate();
        Calendar cal = e.getmDateOfEvent().getCalendar();
        List<MyEvent> eventsArr = this.getListEventsWithCalendar(cal);
        if (eventsArr.size() > 0) {
            long a = date.getTime();
            long b = eventsArr.get(0).getmDateOfEvent().getDate().getTime();
            long dValue = Math.abs(a - b);
            for (int i = 0; i < eventsArr.size(); i++) {
                b = eventsArr.get(i).getmDateOfEvent().getDate().getTime();
                if (e.getmId().equals(eventsArr.get(i).getmId()) || b > a) {
                    continue;
                }
                dValue = dValue < Math.abs(a - b) ? dValue : Math.abs(a - b);
            }
            for (int j = 0; j < eventsArr.size(); j++) {
                b = eventsArr.get(j).getmDateOfEvent().getDate().getTime();
                if (dValue == Math.abs(a - b)) {
                    return eventsArr.get(j);
                }
            }
        }
        return null;
    }
    
    public List<MyEvent> getListEventsWithCalendar(Calendar cal) {
        List<MyEvent> tempList = new ArrayList<>();
        MyEventCursorWrapper cursor = this.myDataBaseManager.queryData(
                MyEventDbSchema.MyEventTable.NAME, MyEventDbSchema.MyEventTable.Cols.CALENDAR + " = ? ", new String[] { cal.toString()}
        );
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                tempList.add(cursor.getEvent());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return tempList;
    }

    
    private void sendMessage() {
        Log.d("MyEventManager", "通知首页刷新页面");
        Intent i = new Intent();
        i.setAction("com.save_event_message");
        this.mContext.sendBroadcast(i);
    }

}
