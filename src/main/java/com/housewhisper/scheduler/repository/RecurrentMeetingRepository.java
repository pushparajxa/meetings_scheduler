package com.housewhisper.scheduler.repository;

import com.housewhisper.scheduler.model.RecurrentMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurrentMeetingRepository extends JpaRepository<RecurrentMeeting, String> {
} 