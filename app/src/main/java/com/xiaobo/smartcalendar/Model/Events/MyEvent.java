package com.xiaobo.smartcalendar.Model.Events;

import android.util.Log;

import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;
import com.xiaobo.smartcalendar.Public.PublicFunction;

import java.util.Date;
import java.util.UUID;

public class MyEvent {

    private UUID mId;
    private int _id;

    private ActivityTitle mActivityTitle;           
    private TypeOfEvent mTypeOfEvent;               
    private DateOfEvent mDateOfEvent;               
    private TargetLocation mTargetLocation;         
    private Participant mParticipant;               
    private Periodicity mPeriodicity;               
    private Schedulability mSchedulability;         
    private Boolean mImportance;                    
    String mHost;                                   

    long minimum_duration;                          
    Date earliest_start_time;                       
    Date latest_start_time;                         
    Date earliest_end_time;                         
    Date latest_end_time;                           

    private long commutingTime = -1;

    private boolean isClashWithOther = false;       
    private boolean isEdited = false;               

    
    public MyEvent() {
        this.mId = UUID.randomUUID();
        this.isEdited = false;

        this.mTypeOfEvent = new TypeOfEvent();
        this.mActivityTitle = new ActivityTitle();
        this.mDateOfEvent = new DateOfEvent();
        this.mHost = "未设定主持人";
        this.mParticipant = new Participant();
        this.mTargetLocation = new TargetLocation();
        this.mPeriodicity = new Periodicity();
        this.mImportance = false;

        this.minimum_duration = 0;
        this.earliest_start_time = new Date(0);
        this.latest_start_time = new Date(0);
        this.earliest_end_time = new Date(0);
        this.latest_end_time = new Date(0);
    }

    
    public String describeMyEvent() {
        StringBuilder str = new StringBuilder();
        str.append("事件标题:" + this.mActivityTitle.getmTitle() + "\n");
        str.append("主持人:" + this.mHost + "\n");
        str.append("参与人:" + this.mParticipant.mParticipant + "\n");
        str.append("事件类型:" + this.mTypeOfEvent.mKind.toString() + "\n");
        str.append("目标地点:" + this.mTargetLocation.getTargetName() + "\n");

        str.append("周期性:" + this.mPeriodicity.mTypeOfPeriodicity.getTypeName() + "\n");
        str.append("时间:" + this.mDateOfEvent.getDate() + "\n");


        str.append("持续时间:" + PublicFunction.formatDate(this.mDateOfEvent.getDuration()) + "\n");




        return str.toString();
    }
    
    public String getCommutingDate() {
        if (this.commutingTime < 0) {
            return "未能得到通勤时间";
        }
        else if (this.commutingTime == 0) {
            return "0小时0分钟";
        }
        else {
            
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.commutingTime / 60 / 60 + "小时");
            stringBuilder.append(((this.commutingTime % 3600) / 60 + 1) + "分钟");

            return stringBuilder.toString();
        }
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public ActivityTitle getmActivityTitle() {
        return mActivityTitle;
    }

    public void setmActivityTitle(ActivityTitle mActivityTitle) {
        this.mActivityTitle = mActivityTitle;
    }

    public TypeOfEvent getmTypeOfEvent() {
        return mTypeOfEvent;
    }

    public void setmTypeOfEvent(TypeOfEvent mTypeOfEvent) {
        this.mTypeOfEvent = mTypeOfEvent;
    }

    public DateOfEvent getmDateOfEvent() {
        return mDateOfEvent;
    }

    public void setmDateOfEvent(DateOfEvent mDateOfEvent) {
        this.mDateOfEvent = mDateOfEvent;
    }

    public TargetLocation getmTargetLocation() {
        return mTargetLocation;
    }

    public void setmTargetLocation(TargetLocation mTargetLocation) {
        this.mTargetLocation = mTargetLocation;
    }

    public Participant getmParticipant() {
        return mParticipant;
    }

    public void setmParticipant(Participant mParticipant) {
        this.mParticipant = mParticipant;
    }

    public Periodicity getmPeriodicity() {
        return mPeriodicity;
    }

    public void setmPeriodicity(Periodicity mPeriodicity) {
        this.mPeriodicity = mPeriodicity;
    }

    public Schedulability getmSchedulability() {
        return mSchedulability;
    }

    public void setmSchedulability(Schedulability mSchedulability) {
        this.mSchedulability = mSchedulability;
    }

    public Boolean getmImportance() {
        return mImportance;
    }

    public void setmImportance(Boolean mImportance) {
        this.mImportance = mImportance;
    }

    public String getmHost() {
        return mHost;
    }

    public void setmHost(String mHost) {
        this.mHost = mHost;
    }

    public long getMinimum_duration() {
        return minimum_duration;
    }

    public void setMinimum_duration(long minimum_duration) {
        this.minimum_duration = minimum_duration;
    }

    public Date getEarliest_start_time() {
        return earliest_start_time;
    }

    public void setEarliest_start_time(Date earliest_start_time) {
        this.earliest_start_time = earliest_start_time;
    }

    public Date getLatest_start_time() {
        return latest_start_time;
    }

    public void setLatest_start_time(Date latest_start_time) {
        this.latest_start_time = latest_start_time;
    }

    public Date getEarliest_end_time() {
        return earliest_end_time;
    }

    public void setEarliest_end_time(Date earliest_end_time) {
        this.earliest_end_time = earliest_end_time;
    }

    public Date getLatest_end_time() {
        return latest_end_time;
    }

    public void setLatest_end_time(Date latest_end_time) {
        this.latest_end_time = latest_end_time;
    }

    public long getCommutingTime() {
        return commutingTime;
    }

    public void setCommutingTime(int commutingTime) {
        this.commutingTime = commutingTime;
    }

    
    public boolean adjustStartTime(MyTemporalInconsistency.EventAction action, long range) {
        try {
            Date startDate = this.getmDateOfEvent().getDate();
            switch (action) {
                case advance:
                    this.getmDateOfEvent().setDate(new Date(startDate.getTime() - range));
                    Log.d("MyEvent", "事件:" + this.getmId() + "开始时间提前了\n" + "新开始时间为:" + this.getmDateOfEvent().getDate());
                    Log.d("MyEvent", "事件:" + this.getmId() + this.getmDateOfEvent().showDate());
                    break;
                case postpone:
                    this.getmDateOfEvent().setDate(new Date(startDate.getTime() + range));
                    Log.d("MyEvent", "事件:" + this.getmId() + "开始时间推迟了了\n" + "新开始时间为:" + this.getmDateOfEvent().getDate());
                    Log.d("MyEvent", "事件:" + this.getmId() + this.getmDateOfEvent().showDate());
                    break;
                default:
                    break;
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean adjustEndTime(MyTemporalInconsistency.EventAction action, long range) {
        try {
            long eventDuration = this.getmDateOfEvent().getDuration();
            Date endDate = this.getmDateOfEvent().getEndPoint();
            switch (action) {
                case advance:

                    this.getmDateOfEvent().setEndPoint(new Date(endDate.getTime() - range));
                    Log.d("MyEvent", "事件;" + this.getmId() + "结束时间提前了\n" + "新结束时间为:" + this.getmDateOfEvent().getEndPoint());
                    Log.d("MyEvent", "事件:" + this.getmId() + this.getmDateOfEvent().showDate());
                    break;
                case postpone:

                    this.getmDateOfEvent().setEndPoint(new Date(endDate.getTime() + range));
                    Log.d("MyEvent", "事件;" + this.getmId() + "结束时间推迟了\n" + "新结束时间为:" + this.getmDateOfEvent().getEndPoint());
                    Log.d("MyEvent", "事件:" + this.getmId() + this.getmDateOfEvent().showDate());
                    break;
                default:
                    break;
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
