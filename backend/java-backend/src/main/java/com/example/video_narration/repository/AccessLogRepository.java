
package com.example.video_narration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.video_narration.entity.AccessLog;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    
    long countByLastSeenAtAfter(java.time.LocalDateTime time);
}
