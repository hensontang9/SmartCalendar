package com.xiaobo.smartcalendar.Model.Events;


public class Schedulability {

    
    boolean allowStartPointForward;
    boolean allowStartPointDelay;
    
    boolean definiteStartPoint;

   
    boolean allowEndPointForward;
    boolean allowEndPointDelay;
    
    boolean definiteEndPoint;

    
    
    public boolean postponeWithMinute(long postponetime) {
        if (postponetime == 0) {
            
            return false;
        }
        else if (postponetime >0) {
            

            return true;
        }
        else {
            

            return true;
        }
    }

}
