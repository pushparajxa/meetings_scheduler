package com.housewhisper.scheduler.model;

import java.util.Set;

public class AvailabilityRequest {
    private String userId;
    private int numberOfSlots;
    private Set<TimeRange> timeRanges;

    // Default constructor
    public AvailabilityRequest() {
    }

    // Constructor with all fields
    public AvailabilityRequest(String userId, int numberOfSlots, Set<TimeRange> timeRanges) {
        this.userId = userId;
        this.numberOfSlots = numberOfSlots;
        this.timeRanges = timeRanges;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public Set<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public void setTimeRanges(Set<TimeRange> timeRanges) {
        this.timeRanges = timeRanges;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "AvailabilityRequest{" +
                "userId='" + userId + '\'' +
                ", numberOfSlots=" + numberOfSlots +
                ", timeRanges=" + timeRanges +
                '}';
    }
} 