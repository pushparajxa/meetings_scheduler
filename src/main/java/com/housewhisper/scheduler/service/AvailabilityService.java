package com.housewhisper.scheduler.service;

import com.housewhisper.scheduler.model.UserMeeting;
import com.housewhisper.scheduler.repository.UserMeetingRepository;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {
    private static final Logger logger = LoggerFactory.getLogger(AvailabilityService.class);

    @Autowired
    private UserMeetingRepository userMeetingRepository;

    public boolean isTimeSlotAvailable(String agentId, long startTime, int durationMinutes) {
        // Convert duration to milliseconds and calculate end time
        long endTime = startTime + (durationMinutes * 60L * 1000L);
                
        List<UserMeeting> overlappingMeetings = userMeetingRepository.findOverlappingMeetings(agentId, startTime,
                endTime);
                

        overlappingMeetings.forEach(meeting -> {
            logger.info("Found overlapping meeting: {} from {} to {}",
                        meeting.getMeetingId(),
                        Instant.ofEpochMilli(meeting.getStartTime()),
                        Instant.ofEpochMilli(meeting.getEndTime()));
        });

        return overlappingMeetings.isEmpty();
    }

    public List<ZonedDateTime> findAvailableSlots(String userId,
                                                  ZonedDateTime rangeStart,   
                                                  ZonedDateTime rangeEnd,
                                                  int durationMinutes,
                                                  int numberOfSlots) {

        long rangeStartMillis = rangeStart.toInstant().toEpochMilli();
        long rangeEndMillis = rangeEnd.toInstant().toEpochMilli();

        // Get all meetings within the range
        List<UserMeeting> meetings = userMeetingRepository.findMeetingsInRange(
                    userId, rangeStartMillis, rangeEndMillis);


        List<ZonedDateTime> availableSlots = new ArrayList<>();
        long currentTime = rangeStartMillis;
        long slotDuration = durationMinutes * 60L * 1000L;

        List<Range> ranges = meetings.stream().map(meeting -> new Range(meeting.getStartTime(), meeting.getEndTime())).collect(Collectors.toList());
        List<Range> mergedRanges = mergeOverlappingRanges(ranges);

        // Go over merged ranges and find available slots
        for (int i = 0; i <= mergedRanges.size() && availableSlots.size() < numberOfSlots; i++) {
            long nextRangeStart = (i < mergedRanges.size()) ? mergedRanges.get(i).start : rangeEndMillis;
            
            // Find available slots in the current gap
            while (currentTime + slotDuration <= nextRangeStart && availableSlots.size() < numberOfSlots) {
                availableSlots.add(ZonedDateTime.ofInstant(
                    Instant.ofEpochMilli(currentTime), 
                    rangeStart.getZone()
                ));
                currentTime += slotDuration;
            }
            
            // Move current time to the end of the current meeting range
            if (i < mergedRanges.size()) {
                currentTime = mergedRanges.get(i).end;
            }
        }

        return availableSlots;
    }

    private record  Range(long start, long end) {};

    private List<Range> mergeOverlappingRanges(List<Range> intervals) {

        Collections.sort(intervals, new Comparator<Range>() {
            @Override
            public int compare(Range o1, Range o2) {
                if(o1.start == o2.start)
                    return 0;
                else if (o1.start > o2.start)
                    return 1;
                else
                    return -1;
            }
        });

        PriorityQueue<Range> queue = new PriorityQueue<>(new Comparator<Range>() {
            @Override
            public int compare(Range o1, Range o2) {
                if(o1.end == o2.end)
                    return 0;
                else if (o1.end > o2.end)
                    return 1;
                else
                    return -1;
            }
        });

        List<Range> res = new ArrayList<>();
        
        queue.add(intervals.get(0));


        for (int i = 1; i < intervals.size() ; i++) {
            long start = intervals.get(i).start;
            long end   = intervals.get(i).end;

            Range top  = queue.peek();
            
            if (top.end >= start) {
                if(top.end >= end){
                    // do nothing
                }
                else{
                    
                    queue.poll();
                    queue.add(new Range(top.start, end));
                }
            }
            else {
            //  count++;
                res.add(queue.poll());
                queue.add(intervals.get(i));
                
            }
        }

    
        res.add(queue.poll());
        return res;
    }
}
