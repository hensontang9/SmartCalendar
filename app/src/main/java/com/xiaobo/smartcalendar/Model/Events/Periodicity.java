package com.xiaobo.smartcalendar.Model.Events;

import java.util.ArrayList;


public class Periodicity {

    public enum TypeOfPeriodicity {
        never(0, "从不"),
        everyDay(1, "每天"),
        everyWeek(2, "每周"),
        everyMonth(3, "每月"),
        everyYear(4, "每年"),
        custom(5, "自定义");

        private int typeID;
        private String typeName;

        TypeOfPeriodicity(int typeID, String typeName) {
            this.typeID = typeID;
            this.typeName = typeName;
        }

        public int getTypeID() {
            return typeID;
        }

        public String getTypeName() {
            return typeName;
        }

        public static TypeOfPeriodicity getTypeFromStr(String string) {
            switch (string) {
                case "从不":
                    return never;
                case "每天":
                    return everyDay;
                case "每周":
                    return everyWeek;
                case "每月":
                    return everyMonth;
                case "每年":
                    return everyYear;
                case "自定义":
                    return custom;
                default:
                    return never;

            }
        }

        public static String[] TypesOfPeriodicityStrings() {
            ArrayList<String> tempList = new ArrayList<>();
            for (TypeOfPeriodicity t: TypeOfPeriodicity.values()) {
                tempList.add(t.getTypeName());
            }
            String[] tempString = new String[tempList.size()];
            tempList.toArray(tempString);
            return tempString;
        }
    }

    TypeOfPeriodicity mTypeOfPeriodicity;

    public TypeOfPeriodicity getmTypeOfPeriodicity() {
        return mTypeOfPeriodicity;
    }

    public Periodicity(TypeOfPeriodicity typeOfPeriodicity) {
        this.mTypeOfPeriodicity = typeOfPeriodicity;
    }

    public Periodicity() {
        this.mTypeOfPeriodicity = TypeOfPeriodicity.never;
    }


}
