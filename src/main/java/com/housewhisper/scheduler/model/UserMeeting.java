package com.housewhisper.scheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_meetings")
public class UserMeeting {
    
    @Id
    private String meetingId;
    
    private String agentId;
    private long startTime;
    private long endTime;
    private String location;
    private String recurrenceRuleId;

    // Default constructor
    public UserMeeting() {}

    // Getters and Setters
    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRecurrenceRuleId() {
        return recurrenceRuleId;
    }

    public void setRecurrenceRuleId(String recurrenceRuleId) {
        this.recurrenceRuleId = recurrenceRuleId;
    }
} 