package com.example.video_narration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProcessingConfigRequest {

    @NotBlank
    @Size(max = 100)
    private String configName;

    @NotBlank
    @Size(max = 20)
    private String sourceLanguage;

    @NotBlank
    @Size(max = 20)
    private String targetLanguage;

    @NotBlank
    @Size(max = 20)
    private String subtitleFormat;

    @NotBlank
    @Size(max = 20)
    private String audioFormat;

    public ProcessingConfigRequest() {
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getSubtitleFormat() {
        return subtitleFormat;
    }

    public void setSubtitleFormat(String subtitleFormat) {
        this.subtitleFormat = subtitleFormat;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }
}
