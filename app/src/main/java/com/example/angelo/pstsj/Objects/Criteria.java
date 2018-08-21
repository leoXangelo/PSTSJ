package com.example.angelo.pstsj.Objects;

/**
 * Created by MastahG on 2/26/2018.
 */

public class Criteria {
    public int Criteria_ID;
    public String Criteria_Name;
    public int Criteria_Percentage;
    public String Criteria_Type;
    public String Event_Name;
    public String Criteria_Status;

    public Criteria() {
    }

    public Criteria(int criteria_ID, String criteria_Name, int criteria_Percentage, String criteria_Type, String event_Name, String criteria_Status) {
        Criteria_ID = criteria_ID;
        Criteria_Name = criteria_Name;
        Criteria_Percentage = criteria_Percentage;
        Criteria_Type = criteria_Type;
        Event_Name = event_Name;
        Criteria_Status = criteria_Status;
    }

    public Criteria(String criteria_Name, int criteria_Percentage, String criteria_Type, String event_Name, String criteria_Status) {
        Criteria_Name = criteria_Name;
        Criteria_Percentage = criteria_Percentage;
        Criteria_Type = criteria_Type;
        Event_Name = event_Name;
        Criteria_Status = criteria_Status;
    }

    public int getCriteria_ID() {
        return Criteria_ID;
    }

    public void setCriteria_ID(int criteria_ID) {
        Criteria_ID = criteria_ID;
    }

    public String getCriteria_Name() {
        return Criteria_Name;
    }

    public void setCriteria_Name(String criteria_Name) {
        Criteria_Name = criteria_Name;
    }

    public int getCriteria_Percentage() {
        return Criteria_Percentage;
    }

    public void setCriteria_Percentage(int criteria_Percentage) {
        Criteria_Percentage = criteria_Percentage;
    }

    public String getCriteria_Type() {
        return Criteria_Type;
    }

    public void setCriteria_Type(String criteria_Type) {
        Criteria_Type = criteria_Type;
    }

    public String getEvent_Name() {
        return Event_Name;
    }

    public void setEvent_Name(String event_Name) {
        Event_Name = event_Name;
    }

    public String getCriteria_Status() {
        return Criteria_Status;
    }

    public void setCriteria_Status(String criteria_Status) {
        Criteria_Status = criteria_Status;
    }
}
