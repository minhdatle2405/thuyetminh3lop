
package com.example.video_narration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.video_narration.entity.ProcessingConfig;
import com.example.video_narration.entity.Translation;
import com.example.video_narration.entity.TranslationJob;
import com.example.video_narration.entity.VideoSource;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class TranslationRepositoryTest {

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private VideoSourceRepository videoSourceRepository;

    @Autowired
    private ProcessingConfigRepository processingConfigRepository;

    @Autowired
    private TranslationJobRepository translationJobRepository;

    @Test
    void saveAndFindTranslation() {
        // 1. Tạo nguồn video
        VideoSource video = new VideoSource();
        video.setSourceType(VideoSource.SourceType.UPLOAD);
        video.setOriginalName("test-video.mp4");
        video.setSourceFilePath("uploads/test-video.mp4");
        video = videoSourceRepository.saveAndFlush(video);

        // 2. Tạo cấu hình xử lý
        ProcessingConfig config = new ProcessingConfig();
        config.setConfigName("Test Config");
        config.setSourceLanguage("English");
        config.setTargetLanguage("Vietnamese");
        config.setSubtitleFormat("SRT");
        config.setAudioFormat("MP3");
        config = processingConfigRepository.saveAndFlush(config);

        // 3. Tạo công việc dịch
        TranslationJob job = new TranslationJob();
        job.setVideoSource(video);
        job.setProcessingConfig(config);
        job.setStatus(TranslationJob.JobStatus.PENDING);
        job.setProgressPercent((short) 0);
        job.setErrorMessage(null);
        job = translationJobRepository.saveAndFlush(job);

        // 4. Tạo bản dịch
        Translation translation = new Translation();
        translation.setTranslationJob(job);
        translation.setSourceLanguage("English");
        translation.setTargetLanguage("Vietnamese");
        translation.setStatus(Translation.TranslationStatus.PENDING);

        Translation saved =
                translationRepository.saveAndFlush(translation);

        // 5. Truy vấn lại và kiểm tra
        assertThat(saved.getTranslationId()).isNotNull();

        Translation found = translationRepository
                .findById(saved.getTranslationId())
                .orElseThrow();

        assertThat(found.getSourceLanguage()).isEqualTo("English");
        assertThat(found.getTargetLanguage()).isEqualTo("Vietnamese");
        assertThat(found.getStatus())
                .isEqualTo(Translation.TranslationStatus.PENDING);
        assertThat(found.getTranslationJob().getJobId())
                .isEqualTo(job.getJobId());
    }
}
