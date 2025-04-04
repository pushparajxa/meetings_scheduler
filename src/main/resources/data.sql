INSERT INTO user_meetings (meeting_id, agent_id, start_time, end_time, location, recurrence_rule_id) 
VALUES 
    -- m1: March 22, 2024 10:00 AM - 11:00 AM UTC
    ('m1', 'agent1', 1711101600000, 1711105200000, 'Virtual Meeting Room 1', null),
    
    -- m2: March 22, 2024 1:00 PM - 2:00 PM UTC
    ('m2', 'agent1', 1711112400000, 1711116000000, 'Conference Room A', null),
    
    -- m3: March 22, 2024 4:00 PM - 5:00 PM UTC
    ('m3', 'agent1', 1711123200000, 1711126800000, 'Virtual Meeting Room 2', null),
    
    -- m4: March 22, 2024 10:00 AM - 11:00 AM UTC
    ('m4', 'agent2', 1711101600000, 1711105200000, 'Conference Room B', null),
    
    -- m5: March 22, 2024 2:00 PM - 3:00 PM UTC
    ('m5', 'agent2', 1711116000000, 1711119600000, 'Virtual Meeting Room 3', null); 

-- Add recurrent meetings
INSERT INTO recurrent_meetings (id, recurrence_rule) 
VALUES 
    ('rec1', 'FREQ=WEEKLY;BYDAY=MO,WE,FR'),
    ('rec2', 'FREQ=DAILY;COUNT=5'),
    ('rec3', 'FREQ=MONTHLY;BYDAY=1MO'); 