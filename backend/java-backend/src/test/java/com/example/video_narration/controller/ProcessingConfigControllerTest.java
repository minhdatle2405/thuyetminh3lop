package com.example.video_narration.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.video_narration.dto.ProcessingConfigResponse;
import com.example.video_narration.exception.ResourceConflictException;
import com.example.video_narration.exception.ResourceNotFoundException;
import com.example.video_narration.service.ProcessingConfigService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcessingConfigController.class)
class ProcessingConfigControllerTest {

    private static final String VALID_BODY = """
            {
              "configName": "en-to-vi-default",
              "sourceLanguage": "en",
              "targetLanguage": "vi",
              "subtitleFormat": "srt",
              "audioFormat": "mp3"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProcessingConfigService processingConfigService;

    @Test
    void createReturns201AndLocation() throws Exception {
        when(processingConfigService.create(any())).thenReturn(sampleResponse(1L));

        mockMvc.perform(post("/api/configs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/configs/1")))
                .andExpect(jsonPath("$.configId").value(1))
                .andExpect(jsonPath("$.configName").value("en-to-vi-default"))
                .andExpect(jsonPath("$.sourceLanguage").value("en"))
                .andExpect(jsonPath("$.targetLanguage").value("vi"))
                .andExpect(jsonPath("$.subtitleFormat").value("srt"))
                .andExpect(jsonPath("$.audioFormat").value("mp3"));
    }

    @Test
    void createWithBlankConfigNameReturns400() throws Exception {
        String body = """
                {
                  "configName": " ",
                  "sourceLanguage": "en",
                  "targetLanguage": "vi",
                  "subtitleFormat": "srt",
                  "audioFormat": "mp3"
                }
                """;

        mockMvc.perform(post("/api/configs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("configName"));
    }

    @Test
    void createWithMissingFieldsReturns400() throws Exception {
        mockMvc.perform(post("/api/configs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void findAllReturns200() throws Exception {
        when(processingConfigService.findAll()).thenReturn(List.of(sampleResponse(1L)));

        mockMvc.perform(get("/api/configs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].configId").value(1))
                .andExpect(jsonPath("$[0].configName").value("en-to-vi-default"));
    }

    @Test
    void findAllWhenEmptyReturns200AndEmptyArray() throws Exception {
        when(processingConfigService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/configs"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void findByIdReturns200() throws Exception {
        when(processingConfigService.findById(1L)).thenReturn(sampleResponse(1L));

        mockMvc.perform(get("/api/configs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configId").value(1));
    }

    @Test
    void findByIdWhenMissingReturns404() throws Exception {
        when(processingConfigService.findById(99L))
                .thenThrow(new ResourceNotFoundException("ProcessingConfig not found: 99"));

        mockMvc.perform(get("/api/configs/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ProcessingConfig not found: 99"));
    }

    @Test
    void updateReturns200() throws Exception {
        when(processingConfigService.update(eq(1L), any())).thenReturn(sampleResponse(1L));

        mockMvc.perform(put("/api/configs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configId").value(1));
    }

    @Test
    void updateWhenMissingReturns404() throws Exception {
        when(processingConfigService.update(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("ProcessingConfig not found: 99"));

        mockMvc.perform(put("/api/configs/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWithInvalidBodyReturns400() throws Exception {
        mockMvc.perform(put("/api/configs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "configName": "ok",
                                  "sourceLanguage": "en",
                                  "targetLanguage": "vi",
                                  "subtitleFormat": "srt",
                                  "audioFormat": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("audioFormat"));
    }

    @Test
    void deleteReturns204() throws Exception {
        doNothing().when(processingConfigService).delete(1L);

        mockMvc.perform(delete("/api/configs/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void deleteWhenMissingReturns404() throws Exception {
        doThrow(new ResourceNotFoundException("ProcessingConfig not found: 99"))
                .when(processingConfigService).delete(99L);

        mockMvc.perform(delete("/api/configs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWhenInUseReturns409() throws Exception {
        doThrow(new ResourceConflictException(
                "ProcessingConfig is in use and cannot be deleted: 1"))
                .when(processingConfigService).delete(1L);

        mockMvc.perform(delete("/api/configs/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("ProcessingConfig is in use and cannot be deleted: 1"));
    }

    private static ProcessingConfigResponse sampleResponse(Long id) {
        ProcessingConfigResponse response = new ProcessingConfigResponse();
        response.setConfigId(id);
        response.setConfigName("en-to-vi-default");
        response.setSourceLanguage("en");
        response.setTargetLanguage("vi");
        response.setSubtitleFormat("srt");
        response.setAudioFormat("mp3");
        response.setCreatedAt(LocalDateTime.of(2026, 10, 21, 23, 0));
        return response;
    }
}
