package com.housewhisper.scheduler.model;

import java.time.ZonedDateTime;
import java.util.List;

public class TimeRange {
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private List<Integer> durations; // List of durations in minutes

    // Getters and Setters
    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getDurations() {
        return durations;
    }

    public void setDurations(List<Integer> durations) {
        this.durations = durations;
    }
} 