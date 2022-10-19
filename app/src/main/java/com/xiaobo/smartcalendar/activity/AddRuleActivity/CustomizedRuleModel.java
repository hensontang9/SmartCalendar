package com.xiaobo.smartcalendar.activity.AddRuleActivity;

import com.xiaobo.smartcalendar.Model.Events.*;

public class CustomizedRuleModel {

    String rule_event1Title;
    TypeOfEvent rule_event1Type;
    Location rule_event1Location;
    String rule_event1Host;
    Participant rule_event1Participant;
    Periodicity rule_event1Periodicity;
    String rule_event2Title;
    TypeOfEvent rule_event2Type;
    Location rule_event2Location;
    String rule_event2Host;
    Participant rule_event2Participant;
    Periodicity rule_event2Periodicity;




    Boolean rule_orderImpart;


    
    public CustomizedRuleModel(String event1Title, String event1Type, String event1Location, String event1Host, String event1Participant, String event1Periodicity
            , String event2Title, String event2Type, String event2Location, String event2Host, String event2Participant, String event2Periodicity
            ,  Boolean symmetric) {
        this.rule_event1Title = event1Title;
        this.rule_event1Type = new TypeOfEvent(TypeOfEvent.Kind.getTypeFromName(event1Type));
        Location temp_event1Location = new Location();
        temp_event1Location.setEndName(event1Location);
        this.rule_event1Location = temp_event1Location;
        this.rule_event1Host = event1Host;
        this.rule_event1Participant = new Participant(event1Participant);
        this.rule_event1Periodicity = new Periodicity(Periodicity.TypeOfPeriodicity.getTypeFromStr(event1Periodicity));
        this.rule_event2Title = event2Title;
        this.rule_event2Type = new TypeOfEvent(TypeOfEvent.Kind.getTypeFromName(event2Type));
        Location temp_event2Location = new Location();
        temp_event2Location.setEndName(event2Location);
        this.rule_event2Location = temp_event2Location;
        this.rule_event2Host = event2Host;
        this.rule_event2Participant = new Participant(event2Participant);
        this.rule_event2Periodicity = new Periodicity(Periodicity.TypeOfPeriodicity.getTypeFromStr(event2Periodicity));
//        this.rule_eventAction1 = eventAction1;
//        this.rule_eventAction2 = eventAction2;
//        this.rule_eventAction3 = eventAction3;
//        this.rule_eventAction4 = eventAction4;
//        this.rule_CTC = MyIdentify.CTC.valueOf(CTC);
        this.rule_orderImpart = symmetric;
    }

    public String getRule_event1Title() {
        return rule_event1Title;
    }

    public void setRule_event1Title(String rule_event1Title) {
        this.rule_event1Title = rule_event1Title;
    }

    public TypeOfEvent getRule_event1Type() {
        return rule_event1Type;
    }

    public void setRule_event1Type(TypeOfEvent rule_event1Type) {
        this.rule_event1Type = rule_event1Type;
    }

    public Location getRule_event1Location() {
        return rule_event1Location;
    }

    public void setRule_event1Location(Location rule_event1Location) {
        this.rule_event1Location = rule_event1Location;
    }

    public String getRule_event1Host() {
        return rule_event1Host;
    }

    public void setRule_event1Host(String rule_event1Host) {
        this.rule_event1Host = rule_event1Host;
    }

    public Participant getRule_event1Participant() {
        return rule_event1Participant;
    }

    public void setRule_event1Participant(Participant rule_event1Participant) {
        this.rule_event1Participant = rule_event1Participant;
    }

    public Periodicity getRule_event1Periodicity() {
        return rule_event1Periodicity;
    }

    public void setRule_event1Periodicity(Periodicity rule_event1Periodicity) {
        this.rule_event1Periodicity = rule_event1Periodicity;
    }

    public String getRule_event2Title() {
        return rule_event2Title;
    }

    public void setRule_event2Title(String rule_event2Title) {
        this.rule_event2Title = rule_event2Title;
    }

    public TypeOfEvent getRule_event2Type() {
        return rule_event2Type;
    }

    public void setRule_event2Type(TypeOfEvent rule_event2Type) {
        this.rule_event2Type = rule_event2Type;
    }

    public Location getRule_event2Location() {
        return rule_event2Location;
    }

    public void setRule_event2Location(Location rule_event2Location) {
        this.rule_event2Location = rule_event2Location;
    }

    public String getRule_event2Host() {
        return rule_event2Host;
    }

    public void setRule_event2Host(String rule_event2Host) {
        this.rule_event2Host = rule_event2Host;
    }

    public Participant getRule_event2Participant() {
        return rule_event2Participant;
    }

    public void setRule_event2Participant(Participant rule_event2Participant) {
        this.rule_event2Participant = rule_event2Participant;
    }

    public Periodicity getRule_event2Periodicity() {
        return rule_event2Periodicity;
    }

    public void setRule_event2Periodicity(Periodicity rule_event2Periodicity) {
        this.rule_event2Periodicity = rule_event2Periodicity;
    }

    public Boolean getRule_orderImpart() {
        return rule_orderImpart;
    }

    public void setRule_orderImpart(Boolean rule_orderImpart) {
        this.rule_orderImpart = rule_orderImpart;
    }
}
