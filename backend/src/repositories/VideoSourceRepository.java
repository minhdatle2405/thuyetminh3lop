package com.example.video_narration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.video_narration.entity.VideoSource;

@Repository
public interface VideoSourceRepository
        extends JpaRepository<VideoSource, Long> {
}
