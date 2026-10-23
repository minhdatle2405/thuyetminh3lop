package com.example.video_narration.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.video_narration.dto.ProcessingConfigRequest;
import com.example.video_narration.dto.ProcessingConfigResponse;
import com.example.video_narration.entity.ProcessingConfig;
import com.example.video_narration.exception.ResourceConflictException;
import com.example.video_narration.exception.ResourceNotFoundException;
import com.example.video_narration.repository.ProcessingConfigRepository;

@Service
public class ProcessingConfigService {

    private final ProcessingConfigRepository processingConfigRepository;

    public ProcessingConfigService(ProcessingConfigRepository processingConfigRepository) {
        this.processingConfigRepository = processingConfigRepository;
    }

    @Transactional
    public ProcessingConfigResponse create(ProcessingConfigRequest request) {
        ProcessingConfig entity = new ProcessingConfig();
        applyRequest(entity, request);
        ProcessingConfig saved = processingConfigRepository.save(entity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProcessingConfigResponse> findAll() {
        return processingConfigRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProcessingConfigResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Transactional
    public ProcessingConfigResponse update(Long id, ProcessingConfigRequest request) {
        ProcessingConfig entity = getById(id);
        applyRequest(entity, request);
        ProcessingConfig saved = processingConfigRepository.save(entity);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        ProcessingConfig entity = getById(id);
        try {
            processingConfigRepository.delete(entity);
            processingConfigRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException(
                    "ProcessingConfig is in use and cannot be deleted: " + id, ex);
        }
    }

    private ProcessingConfig getById(Long id) {
        return processingConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ProcessingConfig not found: " + id));
    }

    private void applyRequest(ProcessingConfig entity, ProcessingConfigRequest request) {
        entity.setConfigName(request.getConfigName());
        entity.setSourceLanguage(request.getSourceLanguage());
        entity.setTargetLanguage(request.getTargetLanguage());
        entity.setSubtitleFormat(request.getSubtitleFormat());
        entity.setAudioFormat(request.getAudioFormat());
    }

    private ProcessingConfigResponse toResponse(ProcessingConfig entity) {
        ProcessingConfigResponse response = new ProcessingConfigResponse();
        response.setConfigId(entity.getConfigId());
        response.setConfigName(entity.getConfigName());
        response.setSourceLanguage(entity.getSourceLanguage());
        response.setTargetLanguage(entity.getTargetLanguage());
        response.setSubtitleFormat(entity.getSubtitleFormat());
        response.setAudioFormat(entity.getAudioFormat());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}
