package com.housewhisper.scheduler.repository;

import com.housewhisper.scheduler.model.UserMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMeetingRepository extends JpaRepository<UserMeeting, String> {
 
    
    @Query("SELECT m FROM UserMeeting m WHERE m.agentId = :agentId AND " +
           "((m.startTime < :endTime AND m.endTime > :startTime) OR " +
           "(m.startTime <= :startTime AND m.endTime >= :endTime) OR " +
           "(:startTime <= m.startTime AND :endTime >= m.endTime))")
    List<UserMeeting> findOverlappingMeetings(String agentId, long startTime, long endTime);

    @Query("SELECT m FROM UserMeeting m WHERE m.agentId = :agentId " +
           "AND ((m.startTime >= :rangeStart AND m.startTime <= :rangeEnd) OR " +
           "(m.endTime >= :rangeStart AND m.endTime <= :rangeEnd) OR " +
           "(m.startTime <= :rangeStart AND m.endTime >= :rangeEnd))")
    List<UserMeeting> findMeetingsInRange(String agentId, long rangeStart, long rangeEnd);
} 