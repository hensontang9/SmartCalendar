package com.xiaobo.smartcalendar.Model.Events;


public class PropertyOfEvent {

    int timesOfImportant = 0;
    int timesOfUnimportant = 0;
    int totalTimes = 0;

    boolean isImportant;

    
    public void updateDistribution() {
        if (isImportant) {
            timesOfImportant += 1;
        }
        else {
            timesOfUnimportant += 1;
        }

        totalTimes += 1;
    }

}
