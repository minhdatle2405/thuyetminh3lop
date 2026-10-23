
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
}
