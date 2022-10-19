package com.xiaobo.smartcalendar.DataBase;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.xiaobo.smartcalendar.Model.Events.*;

import java.util.Date;
import java.util.UUID;

public class MyEventCursorWrapper extends CursorWrapper {

    public MyEventCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public MyEvent getEvent() {
        String _id = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols._id));
        String uuidString = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.UUID));
        String titleStrint = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.TITLE));                
        String START_TIME = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.START_TIME));            
        String CALENDAR = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.CALENDAR));                
        String DURATION = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.DURATION));                
        String TYPE = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.TYPE));                        
        String LOCATION = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.LOCATION));                
        String LONGITUDE = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.LONGITUDE));              
        String LATITUDE = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.LATITUDE));                
        String HOST = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.HOST));                        
        String PARTICIPANT = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.PARTICIPANT));          
        String PERIODICITY = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.PERIODICITY));          
        String COMMUTING_TIME = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.COMMUTING_TIME));    
        String IMPORTANT = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.IMPORTANT));              
        String MINIMUM_DURATION = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.MINIMUM_DURATION));            
        String EARLIEST_START_TIME = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.EARLIEST_START_TIME));      
        String LATEST_START_TIME = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.LATEST_START_TIME));          
        String EARLIEST_END_TIME = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.EARLIEST_END_TIME));          
        String LATEST_END_TIME = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.LATEST_END_TIME));              
        String CITY = getString(getColumnIndex(MyEventDbSchema.MyEventTable.Cols.CITY));                        

        MyEvent event = new MyEvent();

        event.setmId(UUID.fromString(uuidString));
        event.setmActivityTitle(new ActivityTitle(titleStrint));
        DateOfEvent tempDate = new DateOfEvent();
        tempDate.setDate(new Date(Long.valueOf(START_TIME)));
        tempDate.setDuration(new Long(Long.valueOf(DURATION)));
        tempDate.setCalendar(CALENDAR);
        
        event.setmDateOfEvent(tempDate);
        event.setmTypeOfEvent(new TypeOfEvent(TypeOfEvent.Kind.getTypeFromName(TYPE)));
        TargetLocation location = new TargetLocation();
        location.setTargetName(LOCATION);
        location.setTargetPoint(Double.valueOf(LATITUDE), Double.valueOf(LONGITUDE));
        location.setmCity(CITY);
        event.setmTargetLocation(location);
        event.setmHost(HOST);
        event.setmParticipant(new Participant(PARTICIPANT));
        event.setmPeriodicity(new Periodicity(Periodicity.TypeOfPeriodicity.getTypeFromStr(PERIODICITY)));
        event.setCommutingTime(Integer.parseInt(COMMUTING_TIME));
        event.setmImportance(Boolean.parseBoolean(IMPORTANT));

        event.setMinimum_duration(new Long(Long.valueOf(MINIMUM_DURATION)));
        event.setEarliest_start_time(new Date(Long.valueOf(EARLIEST_START_TIME)));
        event.setLatest_start_time(new Date(Long.valueOf(LATEST_START_TIME)));
        event.setEarliest_end_time(new Date(Long.valueOf(EARLIEST_END_TIME)));
        event.setLatest_end_time(new Date(Long.valueOf(LATEST_END_TIME)));

        return event;
    }
}
