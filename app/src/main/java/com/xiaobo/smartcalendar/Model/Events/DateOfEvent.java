package com.xiaobo.smartcalendar.Model.Events;

import android.util.Log;

import com.haibin.calendarview.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOfEvent {

    Date startPoint;    
    Date endPoint;      

    long duration;      

    Date date;          

    com.haibin.calendarview.Calendar calendar;  

    public Date getStartPoint() {
        return startPoint;
    }

    public Date getEndPoint() {
        return endPoint;
    }

    public long getDuration() {
        return duration;
    }

    public com.haibin.calendarview.Calendar getCalendar() {
        return calendar;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        this.startPoint = date;
        this.duration = this.endPoint.getTime() > this.startPoint.getTime() ? this.endPoint.getTime() - this.startPoint.getTime() : this.duration;
        this.endPoint = new Date(this.startPoint.getTime() + this.duration);

    }

    public void setCalendar(String calendar) {
        char[] cal = calendar.toCharArray();
        if (cal.length < 6) {
            return;
        }
        int y = Integer.parseInt(String.valueOf(cal[0])) * 1000 + Integer.parseInt(String.valueOf(cal[1])) * 100 + Integer.parseInt(String.valueOf(cal[2])) * 10 + Integer.parseInt(String.valueOf(cal[3]));
        int m = Integer.parseInt(String.valueOf(cal[4])) * 10 + Integer.parseInt(String.valueOf(cal[5]));
        int d = Integer.parseInt(String.valueOf(cal[6])) * 10 + Integer.parseInt(String.valueOf(cal[7]));
        com.haibin.calendarview.Calendar c = new com.haibin.calendarview.Calendar();
        c.setYear(y);
        c.setMonth(m);
        c.setDay(d);
        this.calendar = c;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        this.endPoint = new Date(this.getDate().getTime() + duration);
    }

    public void setEndPoint(Date date) {
        this.endPoint = date;
        this.duration = this.endPoint.getTime() - this.startPoint.getTime();
    }

    public DateOfEvent(Date sP, Date eP, com.haibin.calendarview.Calendar ca) {
        this.startPoint = sP;
        this.endPoint = eP;
        this.calendar = ca;
        this.duration = eP.getTime() - sP.getTime();
        this.date = sP;
    }
    public DateOfEvent() {
        this.startPoint = new Date(0);
        this.endPoint = new Date(0);
        this.date = new Date(0);
        this.calendar = new Calendar();
        this.duration = 0;
    }

    public String showDate() {
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss ");
        String str = String.format("日期:%s月%s日, 时间段为%s ~ %s", calendar.getMonth(), calendar.getDay(), ft.format(startPoint), ft.format(endPoint));
        return str;
//        return this.date.toString();
    }

}
