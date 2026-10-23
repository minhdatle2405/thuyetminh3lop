package com.example.video_narration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.video_narration.entity.ProcessingConfig;

@Repository
public interface ProcessingConfigRepository
        extends JpaRepository<ProcessingConfig, Long> {
}
