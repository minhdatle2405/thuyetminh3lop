package com.example.video_narration.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.video_narration.dto.ProcessingConfigRequest;
import com.example.video_narration.dto.ProcessingConfigResponse;
import com.example.video_narration.entity.ProcessingConfig;
import com.example.video_narration.exception.ResourceConflictException;
import com.example.video_narration.exception.ResourceNotFoundException;
import com.example.video_narration.repository.ProcessingConfigRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessingConfigServiceTest {

    @Mock
    private ProcessingConfigRepository processingConfigRepository;

    @InjectMocks
    private ProcessingConfigService processingConfigService;

    private ProcessingConfigRequest request;

    @BeforeEach
    void setUp() {
        request = new ProcessingConfigRequest();
        request.setConfigName("en-to-vi-default");
        request.setSourceLanguage("en");
        request.setTargetLanguage("vi");
        request.setSubtitleFormat("srt");
        request.setAudioFormat("mp3");
    }

    @Test
    void createSavesMappedEntity() {
        when(processingConfigRepository.save(any(ProcessingConfig.class)))
                .thenAnswer(invocation -> {
                    ProcessingConfig entity = invocation.getArgument(0);
                    entity.setConfigId(1L);
                    return entity;
                });

        ProcessingConfigResponse response = processingConfigService.create(request);

        ArgumentCaptor<ProcessingConfig> captor = ArgumentCaptor.forClass(ProcessingConfig.class);
        verify(processingConfigRepository).save(captor.capture());
        ProcessingConfig saved = captor.getValue();
        assertThat(saved.getConfigName()).isEqualTo("en-to-vi-default");
        assertThat(saved.getSourceLanguage()).isEqualTo("en");
        assertThat(saved.getTargetLanguage()).isEqualTo("vi");
        assertThat(saved.getSubtitleFormat()).isEqualTo("srt");
        assertThat(saved.getAudioFormat()).isEqualTo("mp3");
        assertThat(response.getConfigId()).isEqualTo(1L);
    }

    @Test
    void findAllReturnsMappedResponses() {
        ProcessingConfig entity = existingConfig(1L);
        when(processingConfigRepository.findAll()).thenReturn(List.of(entity));

        List<ProcessingConfigResponse> result = processingConfigService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConfigId()).isEqualTo(1L);
        assertThat(result.get(0).getConfigName()).isEqualTo("en-to-vi-default");
    }

    @Test
    void findByIdWhenMissingThrowsNotFound() {
        when(processingConfigRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> processingConfigService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("ProcessingConfig not found: 99");
    }

    @Test
    void updateWhenExistsAppliesRequest() {
        ProcessingConfig entity = existingConfig(1L);
        when(processingConfigRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(processingConfigRepository.save(entity)).thenReturn(entity);

        request.setConfigName("updated");
        ProcessingConfigResponse response = processingConfigService.update(1L, request);

        assertThat(entity.getConfigName()).isEqualTo("updated");
        assertThat(response.getConfigName()).isEqualTo("updated");
        verify(processingConfigRepository).save(entity);
    }

    @Test
    void updateWhenMissingThrowsNotFound() {
        when(processingConfigRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> processingConfigService.update(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("ProcessingConfig not found: 99");
        verify(processingConfigRepository, never()).save(any());
    }

    @Test
    void deleteWhenExistsRemovesEntity() {
        ProcessingConfig entity = existingConfig(1L);
        when(processingConfigRepository.findById(1L)).thenReturn(Optional.of(entity));

        processingConfigService.delete(1L);

        verify(processingConfigRepository).delete(entity);
        verify(processingConfigRepository).flush();
    }

    @Test
    void deleteWhenMissingThrowsNotFound() {
        when(processingConfigRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> processingConfigService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("ProcessingConfig not found: 99");
        verify(processingConfigRepository, never()).delete(any());
    }

    @Test
    void deleteWhenForeignKeyViolatedThrowsConflict() {
        ProcessingConfig entity = existingConfig(1L);
        when(processingConfigRepository.findById(1L)).thenReturn(Optional.of(entity));
        doThrow(new DataIntegrityViolationException("fk"))
                .when(processingConfigRepository).flush();

        assertThatThrownBy(() -> processingConfigService.delete(1L))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessage("ProcessingConfig is in use and cannot be deleted: 1");
    }

    private static ProcessingConfig existingConfig(Long id) {
        ProcessingConfig entity = new ProcessingConfig();
        entity.setConfigId(id);
        entity.setConfigName("en-to-vi-default");
        entity.setSourceLanguage("en");
        entity.setTargetLanguage("vi");
        entity.setSubtitleFormat("srt");
        entity.setAudioFormat("mp3");
        return entity;
    }
}
