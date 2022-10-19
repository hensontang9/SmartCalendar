package com.xiaobo.smartcalendar.DataBase;

public class MyTemporalInconsistencyDbSchema {

    
    public static final class MyTemporalInconsistencyTable {
        public static final String NAME = "myincontable";     

        
        public static final class  Cols {
            public static final String _id = "_id";
            public static final String _UUID = "uuid";                                      
            public static final String UUID_FOR_EVENT1 = "uuid_event1";                     
            public static final String UUID_FOR_EVENT2 = "uuid_event2";                     
            public static final String CTC = "ctc";                                         
            public static final String COMMUTING_TIME = "commuting";                        
            public static final String CONFLICTING_LENGTH = "conflicting_length";
            public static final String SYS_ACTION1 = "sys_action1";                         
            public static final String SYS_ACTION2 = "sys_action2";                         
            public static final String SYS_ACTION3 = "sys_action3";                         
            public static final String SYS_ACTION4 = "sys_action4";                         
            public static final String SYS_TIME_FOR_ACTION1 = "sys_time_for_action1";       
            public static final String SYS_TIME_FOR_ACTION2 = "sys_time_for_action2";       
            public static final String SYS_TIME_FOR_ACTION3 = "sys_time_for_action3";       
            public static final String SYS_TIME_FOR_ACTION4 = "sys_time_for_action4";       
            public static final String USER_ACTION1 = "user_action1";                       
            public static final String USER_ACTION2 = "user_action2";                       
            public static final String USER_ACTION3 = "user_action3";                       
            public static final String USER_ACTION4 = "user_action4";                       
            public static final String USER_TIME_FOR_ACTION1 = "user_time_for_action1";     
            public static final String USER_TIME_FOR_ACTION2 = "user_time_for_action2";     
            public static final String USER_TIME_FOR_ACTION3 = "user_time_for_action3";     
            public static final String USER_TIME_FOR_ACTION4 = "user_time_for_action4";     
            public static final String ERROR = "error";
            public static final String HANDLED = "handled";                                 
        }
    }

}
