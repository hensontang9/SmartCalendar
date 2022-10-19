package com.xiaobo.smartcalendar.DataBase;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.xiaobo.smartcalendar.Model.Contradiction.MyIdentify;
import com.xiaobo.smartcalendar.Model.Contradiction.MyTemporalInconsistency;

import java.util.UUID;

public class MyTemporalInconsistencyCursorWrapper extends CursorWrapper {

    public MyTemporalInconsistencyCursorWrapper (Cursor cursor) {
        super(cursor);
    }

    public MyTemporalInconsistency getMyTemporalInconsistency() {
        String id = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols._id));
        String idString = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols._UUID));                                
        String uuidForEvent1 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.UUID_FOR_EVENT1));                 
        String uuidForEvent2 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.UUID_FOR_EVENT2));                 
        String CTC = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.CTC));                                       
        String commutingTime = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.COMMUTING_TIME));                  
        String conflicting_length = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.CONFLICTING_LENGTH));         //
        String SYSAction1 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION1));                        
        String SYSAction2 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION2));                        
        String SYSAction3 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION3));                        
        String SYSAction4 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_ACTION4));                        
        String SYSTimeForAction1 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION1));        
        String SYSTimeForAction2 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION2));        
        String SYSTimeForAction3 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION3));        
        String SYSTimeForAction4 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.SYS_TIME_FOR_ACTION4));        
        String USERAction1 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION1));                      
        String USERAction2 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION2));                      
        String USERAction3 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION3));                      
        String USERAction4 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_ACTION4));                      
        String USERTimeForAction1 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION1));      
        String USERTimeForAction2 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION2));      
        String USERTimeForAction3 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION3));      
        String USERTimeForAction4 = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.USER_TIME_FOR_ACTION4));      
        String error = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.ERROR));                                   
        String handled = getString(getColumnIndex(MyTemporalInconsistencyDbSchema.MyTemporalInconsistencyTable.Cols.HANDLED));                               

        MyTemporalInconsistency myTemporalInconsistency = new MyTemporalInconsistency();
        try {
            myTemporalInconsistency.set_id(Integer.parseInt(id));
            myTemporalInconsistency.setmID(idString);
            myTemporalInconsistency.setUuid_event1(UUID.fromString(uuidForEvent1));
            myTemporalInconsistency.setUuid_event2(UUID.fromString(uuidForEvent2));
            myTemporalInconsistency.setHandled(MyTemporalInconsistency.Handled.getStateWithString(handled));
            myTemporalInconsistency.setCommuting(Long.parseLong(commutingTime));
            myTemporalInconsistency.setmCTC(MyIdentify.getCTCFromName(CTC));
        }
        catch (Exception e) {
            Log.e("提取冲突错误", e.toString());
        }

        return myTemporalInconsistency;
    }

}
