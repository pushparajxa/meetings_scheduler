package com.housewhisper.scheduler.controller;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.housewhisper.scheduler.model.UserMeeting;
import com.housewhisper.scheduler.repository.UserMeetingRepository;
import com.housewhisper.scheduler.service.AvailabilityService;
import com.housewhisper.scheduler.model.AvailabilityRequest;
import com.housewhisper.scheduler.model.TimeRange;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private UserMeetingRepository userMeetingRepository;

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityController.class);

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestParam String userId,
            @RequestParam String startTime, // Expecting ISO-8601 format with timezone
            @RequestParam int durationMinutes) {
                
        
        // Parse the ISO datetime string to ZonedDateTime and convert to epoch milliseconds
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(startTime);
        long startTimeEpoch = zonedDateTime.toInstant().toEpochMilli(); // Changed to milliseconds
        long endTimeEpoch = startTimeEpoch + (durationMinutes * 60L * 1000L);
       
        
        logger.info("Checking availability - userId: {}, startTime: {} , in millis: {}, durationMinutes: {}", 
            userId, startTime, startTimeEpoch, durationMinutes);

        boolean isAvailable = availabilityService.isTimeSlotAvailable(userId, startTimeEpoch, durationMinutes);
        
        return ResponseEntity.ok(Map.of(
            "isAvailable", isAvailable,
            "requestedStartTimeMillis", startTimeEpoch,
            "requestedEndTimeMillis", endTimeEpoch,
            "requestedStartTime", Instant.ofEpochMilli(startTimeEpoch),
            "requestedEndTime", Instant.ofEpochMilli(endTimeEpoch),
            "durationMinutes", durationMinutes,
            "userId", userId
        )); 
        
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!*!";
    }

    @GetMapping("/meetings")
    public ResponseEntity<List<UserMeeting>> getAllMeetings() {
        return ResponseEntity.ok(userMeetingRepository.findAll());
    }

    @GetMapping("/find-slots")
    public ResponseEntity<List<Map<String, Object>>> findAvailableSlots(
            @RequestParam String userId,
            @RequestParam String startTime,  // "2024-03-22T09:00:00Z"
            @RequestParam String endTime,    // "2024-03-22T17:00:00Z"
            @RequestParam List<Integer> durations,  // "30,45,60"
            @RequestParam int numberOfSlots) {
        
        // Convert string times to ZonedDateTime
        ZonedDateTime start = ZonedDateTime.parse(startTime);
        ZonedDateTime end = ZonedDateTime.parse(endTime);
        
        // Create a single TimeRange
        TimeRange range = new TimeRange();
        range.setStartTime(start);
        range.setEndTime(end);
        range.setDurations(durations);
        
        // Create request
        AvailabilityRequest request = new AvailabilityRequest();
        request.setUserId(userId);
        request.setNumberOfSlots(numberOfSlots);
        request.setTimeRanges(Set.of(range));
        
        // Use existing logic
        return findAvailableSlots(request);
    }

    @PostMapping("/find-slots")
    public ResponseEntity<List<Map<String, Object>>> findAvailableSlots(@RequestBody AvailabilityRequest request) {
        List<Map<String, Object>> response = new ArrayList<>();
        
        for (TimeRange range : request.getTimeRanges()) {
            Map<Integer, List<Map<String, Object>>> durationSlots = new HashMap<>();
            
            // Initialize the map for each duration
            for (Integer duration : range.getDurations()) {
                durationSlots.put(duration, new ArrayList<>());
            }
            
            // Find slots for each duration
            for (Integer duration : range.getDurations()) {
                List<ZonedDateTime> slotsInRange = availabilityService.findAvailableSlots(
                    request.getUserId(),
                    range.getStartTime(),
                    range.getEndTime(),
                    duration,
                    request.getNumberOfSlots()
                );
                
                // Convert slots to start/end time pairs
                for (ZonedDateTime startTime : slotsInRange) {
                    ZonedDateTime endTime = startTime.plusMinutes(duration);
                    durationSlots.get(duration).add(Map.of(
                        "startTime", startTime,
                        "endTime", endTime
                    ));
                }
            }
            
            // Create the response structure for this time range
            response.add(Map.of(
                "rangeStartTime", range.getStartTime(),
                "rangeEndTime", range.getEndTime(),
                "durationSlots", durationSlots
            ));
        }
        
        return ResponseEntity.ok(response);
    }
} 