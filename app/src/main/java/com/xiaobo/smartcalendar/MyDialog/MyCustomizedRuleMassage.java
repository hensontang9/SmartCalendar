package com.xiaobo.smartcalendar.MyDialog;

import android.util.Log;

import com.xiaobo.smartcalendar.R;

public class MyCustomizedRuleMassage {

    private static MyCustomizedRuleMassage sMyCustomzedRuleMassage;

    public static MyCustomizedRuleMassage get() {
        if (sMyCustomzedRuleMassage == null) {
            sMyCustomzedRuleMassage = new MyCustomizedRuleMassage();
        }
        return sMyCustomzedRuleMassage;
    }

    private MyCustomizedRuleMassage() {

    }

    String firstInconType;
    String secondInconType;

    public enum firstInconType {
        Before(R.string.incon_type_first_before, "Before"),
        Meets(R.string.incon_type_first_meets, "Meets"),
        Overlaps(R.string.incon_type_first_overlaps, "Overlaps"),
        Starts(R.string.incon_type_first_starts, "Starts"),
        Equals(R.string.incon_type_first_equals, "Equals"),
        During(R.string.incon_type_first_during, "During"),
        Finishes(R.string.incon_type_first_finishes, "Finishes");

        private int typeID;
        private String typeName;

        firstInconType(int typeID, String typeName) {
            this.typeID = typeID;
            this.typeName = typeName;
        }
    }

    public enum secondInconType {
        is_happening,
        just_ended,
        already_ended,
        not_need_commute,
        commute_from_event2_location_to_event1_location,
        commute_from_event1_to_event2,
        commute_from_event1_location_to_event2_location
    }


    public String getFirstInconType() {
        String tempFirstType = new String(firstInconType);
        firstInconType = null;
        return tempFirstType;
    }

    public void setFirstInconType(String firstInconType) {
        this.firstInconType = firstInconType;

        Log.d("MyCustomizedRuleMassage", "first type" + firstInconType);
    }

    public String getSecondInconType() {
        String tempSecondType = secondInconType;
        secondInconType = null;
        return tempSecondType;
    }

    public void setSecondInconType(String secondInconType) {
        this.secondInconType = secondInconType;

        Log.d("MyCustomizedRuleMassage", "second type" + secondInconType);
    }

    public void clearn() {
        this.firstInconType = null;
        this.secondInconType = null;
    }

}
