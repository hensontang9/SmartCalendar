package com.xiaobo.smartcalendar.Model.Events;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TempEventManager {

    private static TempEventManager sTempEventManager;
    private Context mContext;
    private boolean completeCalDriveRoute;
    
    private List<TempEvent> tempEvents;
    
    private List<TempEvent> withoutCommutingTimeEvents;
    
    private TempEvent currentEvent;

    enum OperationCode {
        success,
        fail
    }

    public TempEvent getCurrentEvent() {
        return currentEvent;
    }

    private TempEventManager() {
        this.tempEvents = new ArrayList<>();
        this.withoutCommutingTimeEvents = new ArrayList<>();
        this.completeCalDriveRoute = false;
    }
    public void initTempEventManager(Context mContext) {
        this.mContext = mContext;
    }
    public static TempEventManager getInstance() {
        if (sTempEventManager == null) {
            sTempEventManager = new TempEventManager();
        }
        return sTempEventManager;
    }

    
    public TempEvent createEvent() {

        currentEvent = new TempEvent();

        withoutCommutingTimeEvents.add(currentEvent);
        Log.d("TempEventManager", "创建了一个新的临时事件, 事件ID为" + currentEvent.getmId());

        return currentEvent;
    }
    





    
    public OperationCode setCompleteEdit(TempEvent event) {
        if (event.getmId() == this.currentEvent.getmId()) {
            this.currentEvent.setCompleteEdit(true);
        }
        return OperationCode.success;
    }
    
    public OperationCode clearnEvents() {
        
        this.tempEvents.clear();
        return OperationCode.fail;
    }
    
    public boolean uploadEvent(MyEvent myEvent) {

        if (currentEvent.isCompleteCalDriveRoute() && currentEvent.isCompleteEdit()) {
            
        }

        return true;
    }
    
    public class TempEvent extends MyEvent{
        private boolean completeCalDriveRoute;
        private boolean completeEdit = false;

        public boolean isCompleteCalDriveRoute() {
            return completeCalDriveRoute;
        }

        public void setCompleteCalDriveRoute(boolean completeCalDriveRoute) {
            this.completeCalDriveRoute = completeCalDriveRoute;
        }

        public boolean isCompleteEdit() {
            return completeEdit;
        }

        
        public void setCompleteEdit(boolean completeEdit) {
            this.completeEdit = completeEdit;
            if (completeEdit) {
                
                saveEvents();
            }

        }












//





//

//



    }

    
    public TempEvent getTempEvent(UUID uuid) {
        try {
            for (TempEvent e : this.tempEvents) {
                if (e.getmId().equals(uuid)) {
                    return e;
                }
            }
        }
        catch (Exception e) {
            Log.e("TempEventManager", "fun:getTempEvent()获取事件失败:" + e);
        }
        return null;
    }

    
    public TempEvent getWithoutCommutingTimeEvent(UUID uuid) {
        try {
            for (TempEvent e : this.withoutCommutingTimeEvents) {
                if (e.getmId().equals(uuid)) {
                    return e;
                }
            }
        }
        catch (Exception e) {
            Log.e("TempEventManager", "fun:getWithoutCommutingTimeEvent() 获取事件失败:" + e);
        }
        return null;
    }

    private void removeWithoutCommutingTimeEvent(UUID uuid) {
        try {
            for (TempEvent e : this.withoutCommutingTimeEvents) {
                if (e.getmId().equals(uuid)) {
                    this.withoutCommutingTimeEvents.remove(e);
                    Log.d("TempEventManager", "fun:removeWithoutCommutingTimeEvent() 未计算通勤时间组中移除了事件" + e.getmId());
                }
            }
        }
        catch (Exception e) {
            Log.e("TempEventManager", "fun:removeWithoutCommutingTimeEvent() 获取事件失败:" + e);
        }
    }

    
    public void setCommutingTime(UUID uuid, int commutingTime) {
        TempEvent e = getWithoutCommutingTimeEvent(uuid);
        e.setCommutingTime(commutingTime);
        e.setCompleteCalDriveRoute(true);
        this.tempEvents.add(e);

        Log.d("TempEventManager", "修改了事件" + uuid + "的通勤时间,并修改了路径计算的标识");
        currentEvent.setCompleteCalDriveRoute(true);

    }
    
    public OperationCode saveEvents() {
        try {
            Log.d("TempEventManager", "准备保存事件列表");
            if (this.tempEvents.size() < 1) {
                Log.d("TempEventManager", "事件列表为空");
                return OperationCode.success;
            }
            for (MyEvent tEvent : this.tempEvents) {
                if (MyEventManager.getInstance().getMyEvent(tEvent.getmId()) != null) {
                    Log.d("TempEventManager", "此事件已经保存过了 当前应该是修改事件");
                    MyEventManager.getInstance().resetMyEvent(tEvent.getmId(), tEvent);
                    sendMessage();

                }
                else {
                    if (tEvent.getmDateOfEvent().getDate().before(new Date(946656000))) {

                        Log.d("TempEventManager", tEvent.getmDateOfEvent().getDate() + "找到事件日期非法");
                        continue;
                    }
                    Log.d("TempEventManager", "找到事件并开始保存");
                    MyEventManager.getInstance().addMyEventToTable(tEvent);
                    removeWithoutCommutingTimeEvent(tEvent.getmId());
                    
                    sendMessage();
                }
            }

            
            this.currentEvent = null;

            this.clearnEvents();

            return OperationCode.success;
        }
        catch (Exception e) {
            return OperationCode.fail;
        }
    }

    
    private void sendMessage() {
        Log.d("TempEventManager", "通知首页刷新页面");
        Intent i = new Intent();
        i.setAction("com.save_event_message");
        this.mContext.sendBroadcast(i);
    }

}
