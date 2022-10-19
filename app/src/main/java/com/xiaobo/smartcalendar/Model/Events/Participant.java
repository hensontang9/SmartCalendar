package com.xiaobo.smartcalendar.Model.Events;


public class Participant {
    public String getmParticipant() {
        return mParticipant;
    }

    public void setmParticipant(String mParticipant) {
        this.mParticipant = mParticipant;
    }

    String mParticipant;

    
    public Participant(String part) {
        this.mParticipant = part;
    }

    public Participant() {
        this.mParticipant = "未设定参与者";
    }
}
