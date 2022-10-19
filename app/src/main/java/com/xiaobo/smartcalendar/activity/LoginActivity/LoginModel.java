package com.xiaobo.smartcalendar.activity.LoginActivity;

public class LoginModel {

    /**
     * msg : login success
     * status : 200
     * token : calendar_user1c0e90bbb3b64732acd5b44a4d3cb2a9
     */

    private String msg;
    private int status;
    private String token;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
