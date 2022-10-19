package com.xiaobo.smartcalendar.Model.Contradiction;

import android.content.ContentValues;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.xiaobo.smartcalendar.DataBase.MyDataBaseManager;
import com.xiaobo.smartcalendar.DataBase.MyEventDbSchema;
import com.xiaobo.smartcalendar.DataBase.MyTemporalInconsistencyCursorWrapper;
import com.xiaobo.smartcalendar.DataBase.MyTemporalInconsistencyDbSchema;
import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyTemporalInconsistencyManager {

    private static MyTemporalInconsistencyManager sMyTemporalInconsistencyManager;
    List<MyTemporalInconsistency> mInconList;
    private MyDataBaseManager myDataBaseManager;

    public static MyTemporalInconsistencyManager getInstance() {
        if(sMyTemporalInconsistencyManager == null) {
            sMyTemporalInconsistencyManager = new MyTemporalInconsistencyManager();
        }
        return sMyTemporalInconsistencyManager;
    }

    
    private MyTemporalInconsistencyManager() {
        
        mInconList = new ArrayList<>();
        myDataBaseManager = MyDataBaseManager.getInstance();
    }

    
    public MyTemporalInconsistency getMyIncon(String id) {
        MyTemporalInconsistencyCursorWrapper cursor = MyDataBaseManager.getInstance().queryInconData(
                MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.NAME, MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols._UUID + " = ?", new String[] {id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getMyTemporalInconsistency();
        } finally {
            cursor.close();
        }
    }

    
    public List<MyTemporalInconsistency> getInconWithEventID(UUID id) {

        List<MyTemporalInconsistency> myIncons = new ArrayList<>();
        MyTemporalInconsistencyCursorWrapper cursor = this.myDataBaseManager.queryInconData(
                MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.NAME, MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols._UUID + " = ?", new String[] {"'%" + id.toString()});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                myIncons.add(cursor.getMyTemporalInconsistency());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return myIncons;

    }

    
    public List<MyTemporalInconsistency> getInconWithEvent(MyEvent e) {

        UUID id = e.getmId();

        List<MyTemporalInconsistency> myIncons = new ArrayList<>();
        myIncons = getInconWithEventID(id);

        return myIncons;
    }

    
    public Long deleteMyInconWithEventID(UUID eventID) {
        Log.d("MyTemporalManager", "根据事件ID" + eventID + "删除该事件所在冲突");

        Long result = this.myDataBaseManager.deleteDataAsRule(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.NAME, eventID.toString());

        return result;
    }

    public long deleteMyInconWithInconID(String inconID) {
        Log.e("MyTemporalManager", "根据冲突ID" + inconID + "删除冲突");
        long result = 0;
        try {
            result = this.myDataBaseManager.deleteData(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.NAME, inconID.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    
    public void saveMyInconToTable() {
        for (MyTemporalInconsistency myIncon : this.mInconList) {
            if (getMyIncon(myIncon.getmID()).equals(null)) {
                addMyInconToTable(myIncon);
            }
        }
    }
    
    public void addMyInconToTable(MyTemporalInconsistency myIncon) {
        
        Log.d("MyTIManager", "将冲突" + myIncon.getmID() + "保存到数据库中");
        ContentValues values = getContentValues(myIncon);
        MyDataBaseManager.getInstance().insertData(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.NAME, values);
    }
    
    private static ContentValues getContentValues(MyTemporalInconsistency myIncon) {
        ContentValues values = new ContentValues();

        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols._UUID, myIncon.mID.toString());
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.UUID_FOR_EVENT1, myIncon.uuid_event1.toString());
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.UUID_FOR_EVENT2, myIncon.uuid_event2.toString());
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.CTC, myIncon.getmCTC().toString());
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.COMMUTING_TIME, myIncon.getCommuting());
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.CONFLICTING_LENGTH, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION1, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION2, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION3, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION4, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION1, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION2, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION3, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION4, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION1, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION2, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION3, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION4, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION1, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION2, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION3, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION4, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.ERROR, "");
        values.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.HANDLED, myIncon.handled.toString());
        return values;
    }

    
    public void setInconHandled(String id, MyTemporalInconsistency.Handled handled) {
        MyTemporalInconsistency temp = this.getMyIncon(id);
        temp.setHandled(handled);
        this.resetMyIncon(id, temp);
    }

    
    public void resetMyIncon(String oldMyInconID, MyTemporalInconsistency newIncon) {
        Log.d("MyInconManager", "更新了冲突" + oldMyInconID);
        this.deleteMyInconWithInconID(oldMyInconID);
        this.addMyInconToTable(newIncon);
    }

    
    public List<MyTemporalInconsistency> getAllMyIncons() {
        List<MyTemporalInconsistency> myIncons = new ArrayList<>();
        MyTemporalInconsistencyCursorWrapper cursor = this.myDataBaseManager.queryInconData(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.NAME, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                myIncons.add(cursor.getMyTemporalInconsistency());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return myIncons;

    }

    public JSONObject getJSONWithID (String id) {
        JSONObject jsonObject = new JSONObject();
        MyTemporalInconsistency myIncon = getMyIncon(id);
        try {
            jsonObject.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols._id, myIncon.get_id());
            jsonObject.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols._UUID, myIncon.getmID());
            jsonObject.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.UUID_FOR_EVENT1, myIncon.getUuid_event1());
            jsonObject.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.UUID_FOR_EVENT2, myIncon.getUuid_event2());

            jsonObject.put(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.COMMUTING_TIME, myIncon.getCommuting());
        }
        catch (Exception e) {
            Log.e("MyTIManager", "获取事件json编码错误" + e);
        }

        return jsonObject;
    }

    
    public void allConflictDetection() {
        List<MyEvent> list = sortEvents(MyEventManager.getInstance().getAllEvents());
        if (list.size() != 0) {
            if (list.get(0).getmDateOfEvent().getDate().getTime() <= 1) {
                return;
            }
            int i = 0;
            while (i < (list.size() - 1)) {
                int j = i + 1;
                while (j <= (list.size() - 1)) {
                    conflictDetection(list.get(i), list.get(j));
                    
                    j++;
                }
                i++;
            }
        }
    }

    
    public void theEventsHaveClashWithCalendar(Calendar calendar) {
        if ("00000".equals(calendar.toString())) {
            Log.e("MyTemporalManager", "冲突检测 日期非法");
            Log.e("MyTemporalManager", "日期为 00000 跳过");
            return;
        }
        Log.d("MyTemporalManager", "调用了冲突检测算法, 日期:" + calendar);

        List<MyEvent> myEvents = sortEvents(MyEventManager.getInstance().getListEventsWithCalendar(calendar));
        if (myEvents.size() != 0) {
            if (myEvents.get(0).getmDateOfEvent().getDate().getTime() <= 1) {
                return;
            }
            int i = 0;
            while (i < (myEvents.size() - 1)) {
                int j = i + 1;
                while (j <= (myEvents.size() - 1)) {
                    conflictDetection(myEvents.get(i), myEvents.get(j));
                    
                    j++;
                }
                i++;
            }
        }
        else {
            return;
        }
    }

    
    public List<MyTemporalInconsistency> conflictDetectionWithEvent(MyEvent e) {

        Log.d("TemporalInconsistency", "检测事件" + e.getmId() + "是否含有冲突事件");

        List<MyTemporalInconsistency> myTemporalInconsistencyList = new ArrayList<>();
        List<MyEvent> tList = new ArrayList<>();
        tList.add(e);
        for (MyEvent te :
                MyEventManager.getInstance().getAllEvents()) {
            tList.add(te);
        }
        List<MyEvent>list = sortEvents(tList);





        if (list.size() != 0) {
            if (list.get(0).getmDateOfEvent().getDate().getTime() <= 1) {
                return myTemporalInconsistencyList;
            }
            for (MyEvent tempEvent : list) {
                if (e.getmId().equals(tempEvent.getmId())) {
                    continue;
                }
                if (e.getmDateOfEvent().getDate().before(tempEvent.getmDateOfEvent().getDate())) {
                    if (conflictDetection(e, tempEvent)) {
                        Log.d("TemporalInconsistency", "事件" + e.getmId() + "具有冲突事件");
                        myTemporalInconsistencyList.add(new MyTemporalInconsistency(e.getmId(), tempEvent.getmId()));
                    }
                }
                else {
                    if (conflictDetection(tempEvent, e)) {
                        Log.d("TemporalInconsistency", "事件" + e.getmId() + "具有冲突事件");
                        myTemporalInconsistencyList.add(new MyTemporalInconsistency(tempEvent.getmId(), e.getmId()));
                    }
                }

            }
            return myTemporalInconsistencyList;
        }
        return myTemporalInconsistencyList;
    }

    
    private boolean conflictDetection(MyEvent e1, MyEvent e2) {





        long event_1_endpoint = e1.getmDateOfEvent().getDate().getTime() + e1.getmDateOfEvent().getDuration();
        long event_2_startpoint = e2.getmDateOfEvent().getDate().getTime() - e2.getCommutingTime() * 1000;
        Log.d("MyTIManager", "冲突检测");
        Log.d("事件1的结束时间和事件2的开始时间", ":" + event_1_endpoint + " " + event_2_startpoint);
        
        long e2CommutingTime = (long)e2.getCommutingTime()==-1 ? 0 : e2.getCommutingTime();
        if (event_1_endpoint + e2CommutingTime >= event_2_startpoint) {
            if (getMyIncon(e1.getmId() + "_" + e2.getmId()) != null) {
                Log.d("TemporalInconsistency", "已存在冲突" + e1.getmId() + "_" + e2.getmId() + ", 跳过");
                return false;
            }
            else {
                Log.e("TemporalInconsistency", "存在冲突, 事件1ID为:" + e1.getmId() + " 事件2ID为:" + e2.getmId());
                MyTemporalInconsistency temporalInconsistency = new MyTemporalInconsistency(e1.getmId(), e2.getmId());
                temporalInconsistency.setCommuting(e2CommutingTime);
                temporalInconsistency.setmCTC(MyIdentify.calCTC(e1, e2));
                long conflicting_length = event_1_endpoint+e2CommutingTime - event_2_startpoint;
                temporalInconsistency.setConflicting_length(conflicting_length);
                Log.e("MInconManager", "commuting -> " + temporalInconsistency.getCommuting());
                Log.e("MInconManager", "CTC -> " + temporalInconsistency.getmCTC().toString());
                Log.e("MInconManager", "Conflicting_length -> " + temporalInconsistency.getConflicting_length());
                
                this.mInconList.add(temporalInconsistency);
                
                addMyInconToTable(temporalInconsistency);
                return true;
            }
        }
        else {
            Log.d("MyTIManager", "没有检测到冲突");
            return false;
        }
    }

    
    private List<MyEvent> sortEvents(List<MyEvent> tempArray) {
        List<MyEvent> sortedArray = new ArrayList<>(tempArray);
        Log.d("MyTIManager", "选中的事件列表中个数为" + tempArray.size() + "个");
        for (int i  = 0; i < sortedArray.size(); i++) {
            for (int j = i + 1; j < sortedArray.size(); j++) {
                if (sortedArray.get(i).getmDateOfEvent().getDate().after(sortedArray.get(j).getmDateOfEvent().getDate())) {
                    MyEvent temp = sortedArray.get(i);
                    sortedArray.set(i, sortedArray.get(j));
                    sortedArray.set(j, temp);
                }
            }
        }
        return sortedArray;
    }


}
