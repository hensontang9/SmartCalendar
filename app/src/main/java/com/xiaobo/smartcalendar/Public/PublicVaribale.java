package com.xiaobo.smartcalendar.Public;

import com.amap.api.services.core.LatLonPoint;

public class PublicVaribale {



    

    public static final String IP = "http://114.132.248.252:5000/";

    
    public static final String REGISTER_URL = IP + "user/register/";
    public static final String LOGIN_URL = IP + "user/register/";


    public static final String uploadFileURL = "http://114.132.248.252:5000/upload";

    public static final String upload_new_jsonURL = IP + "user/event/?";
    public static final String revise_eventURL = IP + "user/event/?";
    public static final String delete_eventURL = IP + "user/event/?";
    public static final String upload_old_jsonURL = IP + "user/event/?";
    public static final String upload_new_TI = IP + "user/TI/?";
    public static final String revise_inconURL = IP + "user/TI/?";
    public static final String delete_old_event = IP + "delete_old_event";
    public static final String upload_customized_rule = IP + "user/rules/?";








    public static final int POISEARCHACTIVITY_REQUEST = 1000;
    public static final int POISEARCHACTIVITY_RESULT = 1001;
    public static final String LOGIN_FAIL = "login fail";
    public static final String LOGIN_SUCCESS = "login success";

    
    public enum RequestType {
        REGISTER,       
        LOGIN,          
        UP_EVENT,       
        REVISE_EVENT,   
        DELETE_EVENT,   
        UP_INCON,       
        REVISE_TI,      
        UP_CSV          
    }

    
    public static final int CONNECT_TIMEOUT = 60;
    
    public static final int READ_TIMEOUT = 100;
    
    public static final int WRITE_TIMEOUT = 60;

    public static final LatLonPoint STARTPOINT_LOTATION = new LatLonPoint(31.838341, 120.543176);   
    public static final LatLonPoint ENDPOINT_LOCATION = new LatLonPoint(31.895321, 120.576157);     

    
    public static final String MYINCONRESPONSE = "{\"msg\": \"ok\",\"status\": 201,\"data\": {\"uuid\": \"957c125e-de65-44cf-a4fa-5011a8bbdbaa_9d8988ae-a254-4b21-8986-7a03bb3ffc52\",\"sys_action_type1\": null,\"sys_action_type2\": null,\"sys_action_type3\": null,\"sys_action_type4\": null, \"sys_time_action1\": 0, \"sys_time_action2\": 0,\"sys_time_action3\": 0,\"sys_time_action4\": 0}}";
}
