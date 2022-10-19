package com.xiaobo.smartcalendar.DataBase;

public class MyEventDbSchema {

    
    public static final class MyEventTable {
        public static final String NAME = "myevents";     

        
        public static final class  Cols {
            public static final String _id = "_id";
            public static final String UUID = "uuid";                   
            public static final String TITLE = "title";                 
            public static final String START_TIME = "date";             
            public static final String CALENDAR = "calendar";           
            public static final String DURATION = "duration";           
            public static final String TYPE = "type";                   
            public static final String LOCATION = "location";           
            public static final String LONGITUDE = "longitude";         
            public static final String LATITUDE = "latitude";           
            public static final String HOST = "host";                   
            public static final String PARTICIPANT = "participant";     
            public static final String PERIODICITY = "periodicity";     
            public static final String IMPORTANT = "important";         
            public static final String COMMUTING_TIME = "commuting";    
            public static final String MINIMUM_DURATION = "minimum_duration";           
            public static final String EARLIEST_START_TIME = "earliest_start_time";     
            public static final String LATEST_START_TIME = "latest_start_time";         
            public static final String EARLIEST_END_TIME = "earliest_end_time";         
            public static final String LATEST_END_TIME = "latest_end_time";             
            public static final String CITY = "city";                   
        }
    }

}
