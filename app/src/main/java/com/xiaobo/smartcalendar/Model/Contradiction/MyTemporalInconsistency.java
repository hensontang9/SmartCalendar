package com.xiaobo.smartcalendar.Model.Contradiction;

import android.util.Log;

import com.xiaobo.smartcalendar.Model.Events.MyEvent;
import com.xiaobo.smartcalendar.Model.Events.MyEventManager;

import java.util.UUID;

public class MyTemporalInconsistency {

    private MyEventManager eventManager = MyEventManager.getInstance();

    public enum Handled {
        original("原始的", 1),
        postponed("延后的", 2),
        settled("解决的", 3);

        String describe;
        int index;

        Handled(String describe, int index) {
            this.describe = describe;
            this.index = index;
        }

        @Override
        public String toString() {
            return this.describe;
        }
        public static Handled getStateWithString(String describe) {
            switch (describe) {
                case "原始的":
                    return original;
                case "延后的":
                    return postponed;
                case "解决的":
                    return settled;
                default:
                    return original;

            }
        }

    }

    public enum EventAction {
        hold,
        abandon,    
        advance,    
        postpone;   
    }

    String mID;
    UUID uuid_event1;
    UUID uuid_event2;
    long commuting;
    Handled handled = Handled.original;
    MyIdentify.CTC mCTC;

    int _id;
    long conflicting_length;
    EventAction sys_action1;
    EventAction sys_action2;
    EventAction sys_action3;
    EventAction sys_action4;
    long sys_time_for_action1;
    long sys_time_for_action2;
    long sys_time_for_action3;
    long sys_time_for_action4;
    EventAction user_action1;
    EventAction user_action2;
    EventAction user_action3;
    EventAction user_action4;
    long user_time_for_action1;
    long user_time_for_action2;
    long user_time_for_action3;
    long user_time_for_action4;

    long error;

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public UUID getUuid_event1() {
        return uuid_event1;
    }

    public void setUuid_event1(UUID uuid_event1) {
        this.uuid_event1 = uuid_event1;
    }

    public UUID getUuid_event2() {
        return uuid_event2;
    }

    public void setUuid_event2(UUID uuid_event2) {
        this.uuid_event2 = uuid_event2;
    }

    public long getCommuting() {
        return commuting;
    }

    public void setCommuting(long commuting) {
        this.commuting = commuting;
    }

    public MyIdentify.CTC getmCTC() {
        return mCTC;
    }

    public void setmCTC(MyIdentify.CTC mCTC) {
        this.mCTC = mCTC;
    }

    public long getConflicting_length() {
        return conflicting_length;
    }

    public void setConflicting_length(long conflicting_length) {
        this.conflicting_length = conflicting_length;
    }

    public Handled getHandled() {
        return handled;
    }

    public void setHandled(Handled handled) {
        this.handled = handled;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public static EventAction getEvetnActionWithStr(String eventAction) {
        switch (eventAction) {
            case "hold":
                return EventAction.hold;
            case "abandon":
                return EventAction.abandon;
            case "advance":
                return EventAction.advance;
            case "postpone":
                return EventAction.postpone;
            default:
                return EventAction.hold;

        }
    }

    public void set_system_advice(String sys_action1, long sys_time_for_action1,
                              String sys_action2, long sys_time_for_action2,
                              String sys_action3, long sys_time_for_action3,
                              String sys_action4, long sys_time_for_action4) {
        this.sys_action1 = getEvetnActionWithStr(sys_action1);
        this.sys_time_for_action1 = sys_time_for_action1;
        this.sys_action2 = getEvetnActionWithStr(sys_action2);
        this.sys_time_for_action2 = sys_time_for_action2;
        this.sys_action3 = getEvetnActionWithStr(sys_action3);
        this.sys_time_for_action3 = sys_time_for_action3;
        this.sys_action4 = getEvetnActionWithStr(sys_action4);
        this.sys_time_for_action4 = sys_time_for_action4;
    }

    
    
    public void accept_sys_advice() {
        MyEvent myEvent1 = MyEventManager.getInstance().getMyEvent(this.uuid_event1);
        MyEvent myEvent2 = MyEventManager.getInstance().getMyEvent(this.uuid_event2);

        if (this.sys_action1 == EventAction.abandon || this.sys_action2 == EventAction.abandon ||
                this.sys_action3 == EventAction.abandon || this.sys_action4 == EventAction.abandon) {
            if (this.sys_action1 == EventAction.abandon || this.sys_action2 == EventAction.abandon) {
                MyEventManager.getInstance().deleteMyEvent(this.uuid_event1);
            }
            if (this.sys_action3 == EventAction.abandon || this.sys_action4 == EventAction.abandon) {
                MyEventManager.getInstance().deleteMyEvent(this.uuid_event2);
            }
            return;
        }

        myEvent1.adjustStartTime(this.sys_action1, this.sys_time_for_action1);
        myEvent1.adjustEndTime(this.sys_action2, this.sys_time_for_action2);
        MyEventManager.getInstance().resetMyEvent(this.uuid_event1, myEvent1);

        myEvent2.adjustStartTime(this.sys_action3, this.sys_time_for_action3);
        myEvent2.adjustEndTime(this.sys_action4, this.sys_time_for_action4);
        MyEventManager.getInstance().resetMyEvent(this.uuid_event2, myEvent2);
        
    }

    
    public MyTemporalInconsistency (UUID uuid_event1, UUID uuid_event2) {
        this.mID = uuid_event1 + "_" + uuid_event2;
        this.uuid_event1 = uuid_event1;
        this.uuid_event2 = uuid_event2;
    }
    public MyTemporalInconsistency() {

    }
}
