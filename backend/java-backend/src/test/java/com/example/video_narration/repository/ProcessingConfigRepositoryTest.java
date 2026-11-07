
package com.example.video_narration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.video_narration.entity.ProcessingConfig;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class ProcessingConfigRepositoryTest {

    @Autowired
    private ProcessingConfigRepository processingConfigRepository;

    @Test
    void saveAndFindProcessingConfig() {
        ProcessingConfig config = new ProcessingConfig();

        config.setConfigName("Test Config");
        config.setSourceLanguage("English");
        config.setTargetLanguage("Vietnamese");
        config.setSubtitleFormat("SRT");
        config.setAudioFormat("MP3");

        ProcessingConfig saved =
                processingConfigRepository.saveAndFlush(config);

        assertThat(saved.getConfigId()).isNotNull();

        ProcessingConfig found = processingConfigRepository
                .findById(saved.getConfigId())
                .orElseThrow();

        assertThat(found.getConfigName()).isEqualTo("Test Config");
        assertThat(found.getSourceLanguage()).isEqualTo("English");
        assertThat(found.getTargetLanguage()).isEqualTo("Vietnamese");
        assertThat(found.getSubtitleFormat()).isEqualTo("SRT");
        assertThat(found.getAudioFormat()).isEqualTo("MP3");
    }
        @Test
    void updateProcessingConfig() {
        // 1. Tạo cấu hình ban đầu
        ProcessingConfig config = new ProcessingConfig();
        config.setConfigName("Old Config");
        config.setSourceLanguage("English");
        config.setTargetLanguage("Vietnamese");
        config.setSubtitleFormat("SRT");
        config.setAudioFormat("MP3");

        config = processingConfigRepository.saveAndFlush(config);

        // 2. Cập nhật cấu hình
        config.setConfigName("New Config");
        config.setTargetLanguage("Chinese");
        config.setAudioFormat("WAV");

        processingConfigRepository.saveAndFlush(config);

        // 3. Truy vấn lại dữ liệu
        ProcessingConfig updated = processingConfigRepository
                .findById(config.getConfigId())
                .orElseThrow();

        // 4. Kiểm tra kết quả
        assertThat(updated.getConfigName()).isEqualTo("New Config");
        assertThat(updated.getTargetLanguage()).isEqualTo("Chinese");
        assertThat(updated.getAudioFormat()).isEqualTo("WAV");
    }
}
