package com.example.video_narration.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.video_narration.dto.ProcessingConfigRequest;
import com.example.video_narration.dto.ProcessingConfigResponse;
import com.example.video_narration.service.ProcessingConfigService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/configs")
public class ProcessingConfigController {

    private final ProcessingConfigService processingConfigService;

    public ProcessingConfigController(ProcessingConfigService processingConfigService) {
        this.processingConfigService = processingConfigService;
    }

    @PostMapping
    public ResponseEntity<ProcessingConfigResponse> create(
            @Valid @RequestBody ProcessingConfigRequest request) {
        ProcessingConfigResponse created = processingConfigService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getConfigId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public List<ProcessingConfigResponse> findAll() {
        return processingConfigService.findAll();
    }

    @GetMapping("/{id}")
    public ProcessingConfigResponse findById(@PathVariable Long id) {
        return processingConfigService.findById(id);
    }

    @PutMapping("/{id}")
    public ProcessingConfigResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProcessingConfigRequest request) {
        return processingConfigService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        processingConfigService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
