package com.xiaobo.smartcalendar.Model.Events;

import java.util.ArrayList;

public class TypeOfEvent {

    public enum Kind {
        work(1, "工作"),
        study(2, "学习"),
        personal(3, "个人"),
        social(4, "社交"),
        entertainment(5, "娱乐"),
        rest(6, "休息"),
        sport(7, "运动"),
        Other(8, "其他");

        private int typeID;
        private String typeName;

        Kind(int typeID, String typeName) {
            this.typeID = typeID;
            this.typeName = typeName;
        }

        public int getTypeID() {
            return typeID;
        }

        public String getTypeName() {
            return typeName;
        }

        public static Kind getTypeFromName(String str) {
            switch (str) {
                case "工作":
                    return work;
                case "学习":
                    return study;
                case "个人":
                    return personal;
                case "社交":
                    return social;
                case "娱乐":
                    return entertainment;
                case "休息":
                    return rest;
                case "运动":
                    return sport;
                case "其他":
                    return Other;
                default:
                    return Other;
            }
        }

        public static String[] KindStrings() {
            ArrayList<String> tempList = new ArrayList<>();
            for (Kind temp: Kind.values()) {
                tempList.add(temp.getTypeName());
            }
            String[] tempString = new String[tempList.size()];
            tempList.toArray(tempString);
            return tempString;
        }
    }

    Kind mKind;

    public Kind getmKind() {
        return mKind;
    }

    public TypeOfEvent(Kind k) {
        this.mKind = k;
    }

    public TypeOfEvent() {
        this.mKind = Kind.Other;
    }

}
